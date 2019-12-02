package mt.com.go.apoe;

import mt.com.go.apoe.data.PlanCellTypeMap;
import mt.com.go.apoe.data.StepDataMap;
import mt.com.go.apoe.engineering.PathLossModel;
import mt.com.go.apoe.model.AccessPoint;
import mt.com.go.apoe.model.grid.Grid;
import mt.com.go.apoe.model.grid.GridCell;
import mt.com.go.apoe.model.grid.GridPoint;
import mt.com.go.apoe.model.grid.Gridster;
import mt.com.go.apoe.model.plan.GridWall;
import mt.com.go.apoe.model.plan.UiWall;
import mt.com.go.apoe.model.recommendation.EmptyRecommendation;
import mt.com.go.apoe.model.recommendation.Recommendation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OptimizationEngine {

    private static final Logger logger = LoggerFactory.getLogger(OptimizationEngine.class);

    private static final boolean DEBUG = true;

    private static final int MAX_STEPS = 50;
    private static final float MAX_FORCE = 10;
    private static final int MAX_ACCESS_POINTS = 5;
    private static final float MIN_DECIBEL_THRESHOLD = -70;
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

        double[][] bestSignalStrengthHeatMap = null;
        AccessPoint[] bestAccessPoints = null;
        float highestAreaCoverage = 0;

        int bestStep = 0;
        int bestAccessPointCount = 0;

        do {
            accessPointCount++;

            AccessPoint[] accessPoints = randomlyPlaceAccessPoints(planLayoutGrid, accessPointCount);
            int step = 0;

            while(step < MAX_STEPS) {

                for(AccessPoint accessPoint : accessPoints) {
                    GridCell gridCell = findBestNeighbouringCell(planLayoutGrid, accessPoints, accessPoint);

                    accessPoint.jump(planLayoutGrid, MAX_FORCE, gridCell);
                }

                double[][] signalStrengthHeatMap = pathLossModel.generateHeatMap(accessPoints, false);

                if(DEBUG) {
                    new StepDataMap().generateHeatMapImage(signalStrengthHeatMap, step, accessPointCount, planLayoutGrid);
                }

                float areaCoverage = getAreaCoverage(planLayoutGrid, signalStrengthHeatMap);

                logger.debug("Step: " + step + " | Area Coverage: " + areaCoverage);

                if(areaCoverage > highestAreaCoverage) {
                    bestSignalStrengthHeatMap = signalStrengthHeatMap;
                    bestAccessPoints = accessPoints;
                    highestAreaCoverage = areaCoverage;

                    bestAccessPointCount = accessPointCount;
                    bestStep = step;
                }

                step++;
            }
        } while (accessPointCount < MAX_ACCESS_POINTS);

        logger.debug("Best step: " + bestStep + " | Best Access Point Count: " + bestAccessPointCount);
        return new Recommendation(bestAccessPoints, bestSignalStrengthHeatMap);
    }

    private GridCell findBestNeighbouringCell(Grid planLayoutGrid, AccessPoint[] accessPoints, AccessPoint accessPoint) {
        GridCell bestGridCell = planLayoutGrid.getNeighbouringInsideCell(accessPoint.getCurrentGridPoint().getRow(), accessPoint.getCurrentGridPoint().getColumn());
        float highestAreaCoverage = 0;

        GridCell[] neighbouringGridCells = planLayoutGrid.getNeighbouringCells(accessPoint.getCurrentGridPoint().getRow(), accessPoint.getCurrentGridPoint().getColumn());
        GridPoint originalGridPoint = accessPoint.getCurrentGridPoint();

        for(GridCell gridCell : neighbouringGridCells) {
            accessPoint.setCurrentGridPoint(gridCell.getGridPosition());

            double[][] signalStrengthHeatMap = pathLossModel.generateHeatMap(accessPoints, false);

            float areaCoverage = getAreaCoverage(planLayoutGrid, signalStrengthHeatMap);
            if (areaCoverage > highestAreaCoverage) {
                bestGridCell = gridCell;
                highestAreaCoverage = areaCoverage;
            }
        }

        accessPoint.setCurrentGridPoint(originalGridPoint);

        return bestGridCell;
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
                    accessPoints.add(new AccessPoint(usabilityGrid.getGridCells()[row][column].getGridPosition()));
                    break;
                }
            }
        }

        return accessPoints.toArray(new AccessPoint[0]);
    }

    private float getAreaCoverage(Grid usabilityGrid, double[][] signalStrengthHeatMap) {
        int count = 0;
        int usableGridCells = 0;

        for(int i = 0; i < signalStrengthHeatMap.length; i++) {
            for(int j = 0; j < signalStrengthHeatMap[0].length; j++){
                if(usabilityGrid.getGridCells()[i][j].isInside()) {
                    if(signalStrengthHeatMap[i][j] >= MIN_DECIBEL_THRESHOLD) {
                        count++;
                    }
                    usableGridCells++;
                }
            }
        }

        return count / (float) usableGridCells;
    }

}
