package mt.com.go.apoe;

import mt.com.go.apoe.model.*;
import mt.com.go.apoe.model.recommendation.EmptyRecommendation;
import mt.com.go.apoe.model.recommendation.Recommendation;

import java.util.List;

public class OptimizationEngine {

    private static final int MAX_STEPS = 10;
    private static final int MAX_ACCESS_POINTS = 5;
    private static final float AVERAGE_DECIBEL_THRESHOLD = 50;

    public Recommendation getOptimalSolution(List<Wall> walls) {
        float[][] pathLossHeatMap = getPathLossHeatMap(walls);
        GridCell[][] vacancyGrid = generateVacanyGrid(walls);

        int accessPointCount = 0;

        do {
            accessPointCount++;

            AccessPoint[] accessPoints = randomlyPlaceAccessPoints(vacancyGrid, accessPointCount);
            int step = 0;

            while(step < MAX_STEPS) {
                float[][] signalStrengthHeatMap = getSignalStrengthHeatMap(pathLossHeatMap, accessPoints);

                CellPosition cellPosition = getMostAttractiveGridCell(signalStrengthHeatMap);
                AccessPoint accessPoint = getBestAccessPointToMove(signalStrengthHeatMap, cellPosition, accessPoints);

                moveAccessPoint(accessPoint, cellPosition);

                if(getAreaCoverage(signalStrengthHeatMap) >= AVERAGE_DECIBEL_THRESHOLD) {
                    return new Recommendation(accessPoints, signalStrengthHeatMap);
                }

                step++;
            }
        } while (accessPointCount < MAX_ACCESS_POINTS);

        return new EmptyRecommendation();
    }

}
