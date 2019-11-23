package mt.com.go.apoe.model;

import mt.com.go.apoe.model.grid.CellPosition;
import mt.com.go.apoe.model.plan.Point;

public class AccessPoint {

    private static final float TOP_LEFT_RADIANS = (float) (5 * Math.PI / 4);
    private static final float TOP_RIGHT_RADIANS = (float) (7 * Math.PI / 4);
    private static final float BOTTOM_RIGHT_RADIANS = (float) (Math.PI / 4);
    private static final float BOTTOM_LEFT_RADIANS = (float) (3 * Math.PI / 4);

    private Point position;
    private CellPosition cellPosition;
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

    public CellPosition getCellPosition() {
        return cellPosition;
    }

    public void moveTowards(int rowCount, int columnCount, CellPosition cellPosition) {
        float deltaX = cellPosition.getRow() - this.cellPosition.getRow();
        float deltaY = cellPosition.getColumn() - this.cellPosition.getColumn();

        float radians = (float) Math.atan2(deltaY, deltaX);

        if(radians >= TOP_LEFT_RADIANS && radians <= TOP_RIGHT_RADIANS) {
            moveUp();
        } else if(radians >= TOP_RIGHT_RADIANS && radians <= BOTTOM_RIGHT_RADIANS) {
            moveRight();
        } else if(radians >= BOTTOM_RIGHT_RADIANS && radians <= BOTTOM_LEFT_RADIANS) {
            moveDown();
        } else {
            moveLeft();
        }
    }

}
