package mt.com.go.apoe.model;

public class AccessPoint {

    private Point position;
    private float antennaGain;
    private float transmitPower;

    public AccessPoint(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public float getAntennaGain() {
        return antennaGain;
    }

    public float getTransmitPower() {
        return transmitPower;
    }
}
