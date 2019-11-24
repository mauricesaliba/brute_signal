package mt.com.go.apoe.model;

import mt.com.go.apoe.model.grid.Movement;
import mt.com.go.apoe.model.grid.GridPoint;

public class AccessPoint implements Movement {

    private static final float TOP_LEFT_RADIANS = (float) ((5 * Math.PI) / 4);
    private static final float TOP_RIGHT_RADIANS = (float) ((7 * Math.PI) / 4);
    private static final float BOTTOM_RIGHT_RADIANS = (float) (Math.PI / 4);
    private static final float BOTTOM_LEFT_RADIANS = (float) ((3 * Math.PI) / 4);

    private GridPoint currentGridPoint;
    private float antennaGain = 3;
    private float transmitPower = 0.4f;

    public AccessPoint(GridPoint currentGridPoint) {
        this.currentGridPoint = currentGridPoint;
    }

    public float getAntennaGain() {
        return antennaGain;
    }

    public float getTransmitPower() {
        return transmitPower;
    }

    public GridPoint getCurrentGridPoint() {
        return currentGridPoint;
    }

    public void moveTowards(int rowCount, int columnCount, GridPoint attractiveGridPoint) {
        float deltaX = attractiveGridPoint.getRow() - this.currentGridPoint.getRow();
        float deltaY = this.currentGridPoint.getColumn() - attractiveGridPoint.getColumn();

        float radians = (float) Math.atan2(deltaY, deltaX);

        if(radians >= TOP_LEFT_RADIANS && radians <= TOP_RIGHT_RADIANS) {
            moveUp(this.currentGridPoint);
        } else if(radians >= TOP_RIGHT_RADIANS && radians <= BOTTOM_RIGHT_RADIANS) {
            moveRight(columnCount, this.currentGridPoint);
        } else if(radians >= BOTTOM_RIGHT_RADIANS && radians <= BOTTOM_LEFT_RADIANS) {
            moveDown(rowCount, this.currentGridPoint);
        } else {
            moveLeft(this.currentGridPoint);
        }
    }

}
