package mt.com.go.apoe.engineering;

import mt.com.go.apoe.model.AccessPoint;
import mt.com.go.apoe.model.grid.Grid;

import java.util.Arrays;

public class PathLossModel {

    private static final float WALL_LOSS = 10.0f;

    private PathLossModelCache cache;

    public PathLossModel(Grid grid) {
        cache = generateCache(grid);
    }

    private double findLoss(PathLossModelCache cache, int x1, int y1, final int x2, final int y2) {
        final int dx = Math.abs(x2 - x1);
        final int dy = Math.abs(y2 - y1);

        final int sx = x1 < x2 ? 1 : -1;
        final int sy = y1 < y2 ? 1 : -1;

        int err = dx - dy;
        int e2;
        double totalLoss = 0;

        while (true) {
            totalLoss += cache.cache[x1][y1];

            if (x1 == x2 && y1 == y2)
                break;

            e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x1 = x1 + sx;
            }

            if (e2 < dx) {
                err = err + dx;
                y1 = y1 + sy;
            }
        }
        return totalLoss;
    }

    private double CalculateRxPower(double distance, double wallLoss, double transmitPower, boolean is24GHz, double antennaGain) {
        double antennaGainDevice = 0;
        double frequency = (is24GHz) ? 2400 : 5000;
        double gains = transmitPower + antennaGainDevice + antennaGain;
        double powerLossCoefficient = 28;

        double PathLoss = 20 * Math.log10(frequency) + powerLossCoefficient * Math.log10(distance) + wallLoss - powerLossCoefficient;

        return gains - PathLoss;

    }

    private PathLossModelCache generateCache(Grid grid) {
        PathLossModelCache cache = new PathLossModelCache(grid.getRows(), grid.getColumns());

        Arrays.stream(grid.getGridCells()).parallel().flatMap(x -> Arrays.stream(x))
                .filter(gridCell -> gridCell.isWall())
                .forEach(gridCell -> {
                    cache.cache[gridCell.getGridPosition().getRow()][gridCell.getGridPosition().getColumn()] = WALL_LOSS;
                });

        return cache;
    }

    public double[][] generateHeatMap(AccessPoint[] accessPoints, boolean accumulativeHeatMap) {
        double[][] heatMap = new double[cache.dimX][cache.dimY];
        if (!accumulativeHeatMap) {
            Arrays.stream(heatMap).forEach(a -> Arrays.fill(a, Double.NEGATIVE_INFINITY));
        }

        for (AccessPoint accessPoint : accessPoints) {
            for (int i = 0; i < cache.dimX; i++) {
                for (int j = 0; j < cache.dimY; j++) {
                    if (i == accessPoint.getCurrentGridPoint().getColumn() && j == accessPoint.getCurrentGridPoint().getRow()) {
                        if (accumulativeHeatMap) {
                            heatMap[i][j] += accessPoint.getTransmitPower();
                        } else {
                            heatMap[i][j] = Math.max(accessPoint.getTransmitPower(), heatMap[i][j]);
                        }
                        continue;
                    }
                    double distance = Math.sqrt((Math.pow((accessPoint.getCurrentGridPoint().getColumn() - i), 2)) + (Math.pow(accessPoint.getCurrentGridPoint().getRow() - j, 2)));
                    double totalLoss = findLoss(cache, accessPoint.getCurrentGridPoint().getColumn(), accessPoint.getCurrentGridPoint().getRow(), (i), (j));
                    double receivedPower = Math.round(CalculateRxPower(distance, totalLoss, accessPoint.getTransmitPower(), true, accessPoint.getAntennaGain()));
                    if (accumulativeHeatMap) {
                        heatMap[i][j] += receivedPower;
                    } else {
                        heatMap[i][j] = Math.max(receivedPower, heatMap[i][j]);
                    }
                }
            }
        }
        return heatMap;
    }

    private class PathLossModelCache {

        public int dimX;
        public int dimY;
        public double cache[][];

        public PathLossModelCache(int dimX, int dimY) {
            this.dimX = dimX;
            this.dimY = dimY;

            cache = new double[this.dimX][this.dimY];
        }
    }

}