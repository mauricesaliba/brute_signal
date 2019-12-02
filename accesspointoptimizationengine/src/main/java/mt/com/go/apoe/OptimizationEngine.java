package mt.com.go.apoe;

import mt.com.go.apoe.data.PlanCellTypeMap;
import mt.com.go.apoe.data.StepDataMap;
import mt.com.go.apoe.engineering.PathLossModel;
import mt.com.go.apoe.model.AccessPoint;
import mt.com.go.apoe.model.grid.Grid;
import mt.com.go.apoe.model.grid.GridPoint;
import mt.com.go.apoe.model.grid.Gridster;
import mt.com.go.apoe.model.plan.GridWall;
import mt.com.go.apoe.model.plan.UiWall;
import mt.com.go.apoe.model.recommendation.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OptimizationEngine {

    private static final Logger logger = LoggerFactory.getLogger(OptimizationEngine.class);

    private static final boolean DEBUG = true;

    private static final int MAX_STEPS = 200;
    private static final int MAX_ACCESS_POINTS = 4;
    private static final float AVERAGE_DECIBEL_THRESHOLD = -60;
    private static final int GRID_CELL_SIZE = 20; //This is in cm & is set to 20 so it matches the wall size
    private static final float UI_SCALE_FACTOR = 1f;

    private GridWall[] gridWalls;
    private Grid planLayoutGrid;

    private PathLossModel pathLossModel;

    public OptimizationEngine(UiWall[] uiWalls) {
        gridWalls = convertToGridWalls(uiWalls);
        planLayoutGrid = new Gridster().generateUsabilityGrid(gridWalls);

        if (DEBUG) {
            new PlanCellTypeMap().generateHeatMapImage(planLayoutGrid);
        }

        pathLossModel = new PathLossModel(planLayoutGrid);
    }


    public Recommendation optimize() {
        int accessPointCount = 0;

        //Move inside when we fix the Optimal solution
        double[][] signalStrengthHeatMap = null;
        AccessPoint[] accessPoints;

        do {
            accessPointCount++;

            accessPoints = randomlyPlaceAccessPoints(planLayoutGrid, accessPointCount);
            int step = 0;

            while(step < MAX_STEPS) {
                signalStrengthHeatMap = pathLossModel.generateHeatMap(accessPoints, false);

                GridPoint attractiveGridPoint = getMostAttractiveGridPoint(planLayoutGrid, signalStrengthHeatMap);
                AccessPoint accessPoint = getBestAccessPointToMove(signalStrengthHeatMap, attractiveGridPoint, accessPoints);

                accessPoint.moveTowards(signalStrengthHeatMap.length, signalStrengthHeatMap[0].length, attractiveGridPoint);

                logger.debug("Step: " + step);

                if(DEBUG) {
                    new StepDataMap().generateHeatMapImage(signalStrengthHeatMap, step, accessPointCount, attractiveGridPoint, planLayoutGrid);
                }

                logger.debug("Area coverage: " + getAreaCoverage(planLayoutGrid, signalStrengthHeatMap));

                if(getAreaCoverage(planLayoutGrid, signalStrengthHeatMap) >= AVERAGE_DECIBEL_THRESHOLD) {
                    logger.info("Found a solution!!!");
                    return new Recommendation(accessPoints, signalStrengthHeatMap);
                }

                step++;
            }
        } while (accessPointCount < MAX_ACCESS_POINTS);

        return new Recommendation(accessPoints, signalStrengthHeatMap);
    }

    private GridWall[] convertToGridWalls(UiWall[] walls) {
        if (walls == null) {
            return new GridWall[0];
        }

        GridWall[] gridWalls = new GridWall[walls.length];

        for (int i = 0; i < walls.length; i++) {
            UiWall uiWall = walls[i];

            GridPoint startGridPoint = new GridPoint(
                    (int) ((uiWall.getStart().getX() * 100 * UI_SCALE_FACTOR) / GRID_CELL_SIZE),
                    (int) ((uiWall.getStart().getY() * 100 * UI_SCALE_FACTOR) / GRID_CELL_SIZE));

            GridPoint endGridPoint = new GridPoint(
                    (int) ((uiWall.getEnd().getX() * 100 * UI_SCALE_FACTOR) / GRID_CELL_SIZE),
                    (int) ((uiWall.getEnd().getY() * 100 * UI_SCALE_FACTOR) / GRID_CELL_SIZE));

            gridWalls[i] = new GridWall(startGridPoint, endGridPoint, uiWall.getMaterial(), uiWall.getThickness());
        }

        return gridWalls;
    }

    private AccessPoint[] randomlyPlaceAccessPoints(Grid usabilityGrid, int accessPointCount) {
        List<AccessPoint> accessPoints = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < accessPointCount; i++) {
            while(true){ //Pray to God
                int row = random.nextInt(usabilityGrid.getRows());
                int column = random.nextInt(usabilityGrid.getColumns());

                if (usabilityGrid.getGridCells()[row][column].isInside()){
                    accessPoints.add(new AccessPoint(new GridPoint(row, column)));
                    break;
                }
            }
        }

        return accessPoints.toArray(new AccessPoint[0]);
    }

    private GridPoint getMostAttractiveGridPoint(Grid usabilityGrid, double[][] signalStrengthHeatMap) {
        double lowestDecibel = Double.MAX_VALUE;
        GridPoint gridPoint = new GridPoint(0,0);

        for (int i = 0; i < signalStrengthHeatMap.length; i++) {
            for(int j = 0; j < signalStrengthHeatMap[0].length; j++) {
                if (usabilityGrid.getGridCells()[i][j].isInside() && signalStrengthHeatMap[i][j] < lowestDecibel) {
                    gridPoint.setRow(i).setColumn(j);
                    lowestDecibel = signalStrengthHeatMap[i][j];
                }
            }
        }

        return gridPoint;
    }

    private double getAreaCoverage(Grid usabilityGrid, double[][] signalStrengthHeatMap) {
        float sum = 0;
        int usableGridCells = 0;

        for(int i = 0; i < signalStrengthHeatMap.length; i++) {
            for(int j = 0; j < signalStrengthHeatMap[0].length; j++){
                if(usabilityGrid.getGridCells()[i][j].isInside()) {
                    sum += signalStrengthHeatMap[i][j];
                    usableGridCells++;
                }
            }
        }

        float average = sum / usableGridCells;

        return average;
    }

    private AccessPoint getBestAccessPointToMove(double[][] signalStrengthMap, GridPoint gridPoint, AccessPoint[] accessPoints) {
        if(accessPoints.length == 0) {
            return null;
        }

        float lowestAveragedSum = Float.MAX_VALUE;
        AccessPoint bestAccessPoint = accessPoints[0];

        for(AccessPoint accessPoint : accessPoints) {
            List<GridPoint> gridPoints = Grid.findLine(
                    accessPoint.getCurrentGridPoint().getColumn(),
                    accessPoint.getCurrentGridPoint().getRow(),
                    gridPoint.getColumn(),
                    gridPoint.getRow());

            float currentSum = 0;

            for (GridPoint gp : gridPoints) {
                currentSum += Math.abs(signalStrengthMap[gp.getRow()][gp.getColumn()]);
            }

            float currentAveragedSum = currentSum / gridPoints.size();
            if(currentAveragedSum < lowestAveragedSum) {
                bestAccessPoint = accessPoint;
                lowestAveragedSum = currentAveragedSum;
            }
        }

        return bestAccessPoint;
    }

}
