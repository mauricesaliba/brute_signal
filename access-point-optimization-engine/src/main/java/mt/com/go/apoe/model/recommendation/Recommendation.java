package mt.com.go.apoe.model.recommendation;

import lombok.Getter;
import mt.com.go.apoe.model.AccessPoint;

@Getter
public class Recommendation {

    private AccessPoint[] accessPoints;
    private double[][] signalStrengthHeatMap;

    public Recommendation(AccessPoint[] accessPoints, double[][] signalStrengthHeatMap) {
        this.accessPoints = accessPoints;
        this.signalStrengthHeatMap = signalStrengthHeatMap;
    }

}
