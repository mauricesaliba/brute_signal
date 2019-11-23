package mt.com.go.apoe;

import mt.com.go.apoe.engineering.PathLossModel;
import mt.com.go.apoe.model.*;
import mt.com.go.apoe.model.grid.CellPosition;
import mt.com.go.apoe.model.grid.GridCell;
import mt.com.go.apoe.model.plan.Wall;
import mt.com.go.apoe.model.recommendation.EmptyRecommendation;
import mt.com.go.apoe.model.recommendation.Recommendation;

import java.util.List;

public class OptimizationEngine {

    private static final int MAX_STEPS = 10;
    private static final int MAX_ACCESS_POINTS = 5;
    private static final float AVERAGE_DECIBEL_THRESHOLD = 50;

    public Recommendation getOptimalSolution(List<Wall> walls) {
        PathLossModel.PathLossModelCache pathLossHeatMap = PathLossModel.generateCache(walls);
        GridCell[][] usabilityGrid = generateUsabilityGrid(walls);

        int accessPointCount = 0;

        do {
            accessPointCount++;

            AccessPoint[] accessPoints = randomlyPlaceAccessPoints(usabilityGrid, accessPointCount);
            int step = 0;

            while(step < MAX_STEPS) {
                float[][] signalStrengthHeatMap = getSignalStrengthHeatMap(pathLossHeatMap, accessPoints);

                CellPosition cellPosition = getMostAttractiveGridCell(signalStrengthHeatMap);
                AccessPoint accessPoint = getBestAccessPointToMove(signalStrengthHeatMap, cellPosition, accessPoints);

                accessPoint.moveTowards(cellPosition);

                if(getAreaCoverage(signalStrengthHeatMap) >= AVERAGE_DECIBEL_THRESHOLD) {
                    return new Recommendation(accessPoints, signalStrengthHeatMap);
                }

                step++;
            }
        } while (accessPointCount < MAX_ACCESS_POINTS);

        return new EmptyRecommendation();
    }

    float getAreaCoverage(float[][] signalStrengthHeatMap) {
        float sum = 0;

        for(int i = 0; i < signalStrengthHeatMap.length; i++) {
            for(int j = 0; j < signalStrengthHeatMap[0].length; j++){
                sum += signalStrengthHeatMap[i][j];
            }
        }

        return sum / signalStrengthHeatMap.length + signalStrengthHeatMap[0].length;
    }

}
