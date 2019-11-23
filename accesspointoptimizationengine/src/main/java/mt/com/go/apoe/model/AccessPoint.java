package mt.com.go.apoe.model;

import mt.com.go.apoe.model.grid.Movement;
import mt.com.go.apoe.model.grid.GridPoint;
import mt.com.go.apoe.model.plan.Point;

public class AccessPoint implements Movement {

    private static final float TOP_LEFT_RADIANS = (float) (5 * Math.PI / 4);
    private static final float TOP_RIGHT_RADIANS = (float) (7 * Math.PI / 4);
    private static final float BOTTOM_RIGHT_RADIANS = (float) (Math.PI / 4);
    private static final float BOTTOM_LEFT_RADIANS = (float) (3 * Math.PI / 4);

    private GridPoint gridPoint;
    private float antennaGain = 3;
    private float transmitPower = 0.4f;

    public AccessPoint(GridPoint gridPoint) {
        this.gridPoint = gridPoint;
    }

    public float getAntennaGain() {
        return antennaGain;
    }

    public float getTransmitPower() {
        return transmitPower;
    }

    public GridPoint getGridPoint() {
        return gridPoint;
    }

    public void moveTowards(int rowCount, int columnCount, GridPoint gridPoint) {
        float deltaX = gridPoint.getRow() - this.gridPoint.getRow();
        float deltaY = gridPoint.getColumn() - this.gridPoint.getColumn();

        float radians = (float) Math.atan2(deltaY, deltaX);

        if(radians >= TOP_LEFT_RADIANS && radians <= TOP_RIGHT_RADIANS) {
            moveUp(this.gridPoint);
        } else if(radians >= TOP_RIGHT_RADIANS && radians <= BOTTOM_RIGHT_RADIANS) {
            moveRight(columnCount, this.gridPoint);
        } else if(radians >= BOTTOM_RIGHT_RADIANS && radians <= BOTTOM_LEFT_RADIANS) {
            moveDown(rowCount, this.gridPoint);
        } else {
            moveLeft(this.gridPoint);
        }
    }

}
