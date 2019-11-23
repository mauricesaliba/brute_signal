package mt.com.go.apoe;

import mt.com.go.apoe.engineering.PathLossModel;
import mt.com.go.apoe.model.*;
import mt.com.go.apoe.model.grid.GridPoint;
import mt.com.go.apoe.model.grid.Grid;
import mt.com.go.apoe.model.grid.Gridster;
import mt.com.go.apoe.model.plan.GridWall;
import mt.com.go.apoe.model.plan.UiWall;
import mt.com.go.apoe.model.plan.Wall;
import mt.com.go.apoe.model.recommendation.EmptyRecommendation;
import mt.com.go.apoe.model.recommendation.Recommendation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class OptimizationEngine {

    private static final int MAX_STEPS = 10;
    private static final int MAX_ACCESS_POINTS = 5;
    private static final float AVERAGE_DECIBEL_THRESHOLD = 50;
    private static final int GRID_CELL_SIZE = 20; //This is in cm

    public Recommendation getOptimalSolution(Wall[] uiWalls) {
        Wall[] gridWalls = convertToGridWalls(uiWalls);

        PathLossModel pathLossModel = new PathLossModel(GRID_CELL_SIZE);
        PathLossModel.PathLossModelCache pathLossHeatMap = pathLossModel.generateCache(gridWalls);
        Grid usabilityGrid = new Gridster(GRID_CELL_SIZE).generateUsabilityGrid(gridWalls);

        int accessPointCount = 0;

        do {
            accessPointCount++;

            AccessPoint[] accessPoints = randomlyPlaceAccessPoints(usabilityGrid, accessPointCount);
            int step = 0;

            while(step < MAX_STEPS) {
                double[][] signalStrengthHeatMap = pathLossModel.generateHeatMap(pathLossHeatMap, accessPoints, true);

                GridPoint gridPoint = getMostAttractiveGridPoint(signalStrengthHeatMap);
                AccessPoint accessPoint = getBestAccessPointToMove(signalStrengthHeatMap, gridPoint, accessPoints);

                accessPoint.moveTowards(signalStrengthHeatMap.length, signalStrengthHeatMap[0].length, gridPoint);

                System.out.println(step);

                for(int i = 0; i < signalStrengthHeatMap.length; i++) {
                    for (int j = 0; j < signalStrengthHeatMap[0].length; j++) {
                        System.out.print(signalStrengthHeatMap[i][j] + ", ");
                    }
                    System.out.println();
                }

                if(getAreaCoverage(signalStrengthHeatMap) >= AVERAGE_DECIBEL_THRESHOLD) {
                    System.out.println("Found a solution!!!");
                    return new Recommendation(accessPoints, signalStrengthHeatMap);
                }

                step++;
            }
        } while (accessPointCount < MAX_ACCESS_POINTS);

        return new EmptyRecommendation();
    }

    private Wall[] convertToGridWalls(Wall[] walls) {
        if (walls == null) {
            return new Wall[0];
        }

        Wall[] gridWalls = new GridWall[walls.length];

        for (int i = 0; i < walls.length; i++) {
            Wall wall = walls[i];

            if(wall instanceof UiWall) {
                UiWall uiWall = (UiWall) wall;

                GridPoint startGridPoint = new GridPoint(
                        (int) ((uiWall.getStart().getY() * 100) / GRID_CELL_SIZE),
                        (int) ((uiWall.getStart().getX() * 100) / GRID_CELL_SIZE));

                GridPoint endGridPoint = new GridPoint(
                        (int) ((uiWall.getEnd().getY() * 100) / GRID_CELL_SIZE),
                        (int) ((uiWall.getEnd().getX() * 100) / GRID_CELL_SIZE));

                gridWalls[i] = new GridWall(startGridPoint, endGridPoint, uiWall.getMaterial(), uiWall.getThickness());
            } else {
                gridWalls[i] = wall;
            }
        }

        return gridWalls;
    }

    private AccessPoint[] randomlyPlaceAccessPoints(Grid usabilityGrid, int accessPointCount) {
        List<AccessPoint> accessPoints = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < accessPointCount; i++) {
            while(true){ //Pray to God
                int x = random.nextInt(usabilityGrid.getRows());
                int y = random.nextInt(usabilityGrid.getColumns());

                if (usabilityGrid.getGridCells()[x][y].isUsable()){
                    accessPoints.add(new AccessPoint(new GridPoint(x, y)));
                    break;
                }
            }
        }

        return accessPoints.toArray(new AccessPoint[0]);
    }

    private GridPoint getMostAttractiveGridPoint(double[][] signalStrengthHeatMap) {
        double lowestDecibel = Double.MAX_VALUE;
        GridPoint gridPoint = new GridPoint(0,0);

        for (int i = 0; i < signalStrengthHeatMap.length; i++) {
            for(int j = 0; j < signalStrengthHeatMap[0].length; j++) {
                double decibel = signalStrengthHeatMap[i][j];
                if (decibel < lowestDecibel) {
                    gridPoint = gridPoint.setRow(i).setColumn(j);
                }
            }
        }

        return gridPoint;
    }

    private double getAreaCoverage(double[][] signalStrengthHeatMap) {
        float sum = 0;

        for(int i = 0; i < signalStrengthHeatMap.length; i++) {
            for(int j = 0; j < signalStrengthHeatMap[0].length; j++){
                sum += signalStrengthHeatMap[i][j];
            }
        }

        return sum / signalStrengthHeatMap.length + signalStrengthHeatMap[0].length;
    }

    private AccessPoint getBestAccessPointToMove(double[][] signalStrengthMap, GridPoint gridPoint, AccessPoint[] accessPoints) {
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
