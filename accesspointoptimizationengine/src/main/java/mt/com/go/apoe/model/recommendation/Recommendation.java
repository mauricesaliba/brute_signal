package mt.com.go.apoe.model.recommendation;

import mt.com.go.apoe.model.AccessPoint;

import java.util.List;

public class Recommendation {

    private AccessPoint[] accessPoints;
    private double[][] signalStrengthHeatMap;

    public Recommendation(AccessPoint[] accessPoints, double[][] signalStrengthHeatMap) {
        this.accessPoints = accessPoints;
        this.signalStrengthHeatMap = signalStrengthHeatMap;
    }

    public AccessPoint[] getAccessPoints() {
        return accessPoints;
    }

    public double[][] getSignalStrengthHeatMap() {
        return signalStrengthHeatMap;
    }

}
