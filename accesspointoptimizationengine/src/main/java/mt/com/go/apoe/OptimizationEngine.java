package mt.com.go.apoe;

import mt.com.go.apoe.engineering.PathLossModel;
import mt.com.go.apoe.model.*;
import mt.com.go.apoe.model.grid.GridPoint;
import mt.com.go.apoe.model.grid.Grid;
import mt.com.go.apoe.model.grid.Gridster;
import mt.com.go.apoe.model.plan.Wall;
import mt.com.go.apoe.model.recommendation.EmptyRecommendation;
import mt.com.go.apoe.model.recommendation.Recommendation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OptimizationEngine {

    private static final int MAX_STEPS = 10;
    private static final int MAX_ACCESS_POINTS = 5;
    private static final float AVERAGE_DECIBEL_THRESHOLD = 50;
    private static final int GRID_CELL_SIZE = 20;

    public Recommendation getOptimalSolution(Wall[] uiWalls) {
        Wall[] gridWalls = convertToGridWalls(uiWalls);

        PathLossModel.PathLossModelCache pathLossHeatMap = new PathLossModel(GRID_CELL_SIZE).generateCache(gridWalls);
        Grid usabilityGrid = new Gridster().generateUsabilityGrid(gridWalls);

        int accessPointCount = 0;

        do {
            accessPointCount++;

            AccessPoint[] accessPoints = randomlyPlaceAccessPoints(usabilityGrid, accessPointCount);
            int step = 0;

            while(step < MAX_STEPS) {
                float[][] signalStrengthHeatMap = getSignalStrengthHeatMap(pathLossHeatMap, accessPoints);

                GridPoint gridPoint = getMostAttractiveGridPoint(signalStrengthHeatMap);
                AccessPoint accessPoint = getBestAccessPointToMove(signalStrengthHeatMap, gridPoint, accessPoints);

                accessPoint.moveTowards(signalStrengthHeatMap.length, signalStrengthHeatMap[0].length, gridPoint);

                if(getAreaCoverage(signalStrengthHeatMap) >= AVERAGE_DECIBEL_THRESHOLD) {
                    return new Recommendation(accessPoints, signalStrengthHeatMap);
                }

                step++;
            }
        } while (accessPointCount < MAX_ACCESS_POINTS);

        return new EmptyRecommendation();
    }

    private AccessPoint[] randomlyPlaceAccessPoints(Grid usabilityGrid, int accessPointCount) {
        List<AccessPoint> accessPoints = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < accessPointCount; i++) {
            while(true){ //Pray to God
                int x = random.nextInt(usabilityGrid.getRows() + 1);
                int y = random.nextInt(usabilityGrid.getColumns() + 1);

                if (usabilityGrid.getGridCells()[x][y].isUsable()){
                    accessPoints.add(new AccessPoint(new GridPoint(x, y)));
                    break;
                }
            }
        }

        return accessPoints.toArray(new AccessPoint[0]);
    }

    private GridPoint getMostAttractiveGridPoint(float[][] signalStrengthHeatMap) {
        float lowestDecibel = Float.MAX_VALUE;
        GridPoint gridPoint = new GridPoint(0,0);

        for (int i = 0; i < signalStrengthHeatMap.length; i++) {
            for(int j = 0; j < signalStrengthHeatMap[0].length; j++) {
                float decibel = signalStrengthHeatMap[i][j];
                if (decibel < lowestDecibel) {
                    gridPoint = gridPoint.setRow(i).setColumn(j);
                }
            }
        }

        return gridPoint;
    }

    private float getAreaCoverage(float[][] signalStrengthHeatMap) {
        float sum = 0;

        for(int i = 0; i < signalStrengthHeatMap.length; i++) {
            for(int j = 0; j < signalStrengthHeatMap[0].length; j++){
                sum += signalStrengthHeatMap[i][j];
            }
        }

        return sum / signalStrengthHeatMap.length + signalStrengthHeatMap[0].length;
    }

    private AccessPoint getBestAccessPointToMove(float[][] signalStrengthMap, GridPoint gridPoint, AccessPoint[] accessPoints) {
        if(accessPoints.length == 0) {
            return null;
        }

        float lowestSum = Float.MAX_VALUE;
        AccessPoint bestAccessPoint = accessPoints[0];

        for(AccessPoint accessPoint : accessPoints) {
            List<GridPoint> gridPoints = Grid.findLine(
                    accessPoint.getGridPoint().getColumn(),
                    accessPoint.getGridPoint().getRow(),
                    gridPoint.getColumn(),
                    gridPoint.getRow());

            float currentSum = 0;
            for (GridPoint gp : gridPoints) {
                currentSum += signalStrengthMap[gp.getColumn()][gp.getRow()];
            }

            if(currentSum < lowestSum) {
                bestAccessPoint = accessPoint;
                lowestSum = currentSum;
            }
        }

        return bestAccessPoint;
    }



}
