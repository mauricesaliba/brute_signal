package mt.com.go.apoe.model;

import mt.com.go.apoe.model.grid.GridCell;
import mt.com.go.apoe.model.grid.Movement;
import mt.com.go.apoe.model.grid.GridPoint;

public class AccessPoint implements Movement {

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

    public void setCurrentGridPoint(GridPoint gridPoint) {
        this.currentGridPoint = gridPoint;
    }

    public void moveTowards(int rowCount, int columnCount, GridPoint attractiveGridPoint) {
        float deltaRow = this.currentGridPoint.getRow() - attractiveGridPoint.getColumn();
        float deltaColumn = this.currentGridPoint.getColumn() - attractiveGridPoint.getRow();

        float absDeltaRow = Math.abs(deltaRow);
        float absDeltaColumn = Math.abs(deltaColumn);

        if(absDeltaColumn >= absDeltaRow && deltaColumn >= 0 ) {
            moveLeft(this.currentGridPoint);
        } else if(absDeltaColumn >= absDeltaRow && deltaColumn <= 0) {
            moveRight(columnCount, this.currentGridPoint);
        } else if(absDeltaRow >= absDeltaColumn && deltaRow >= 0) {
            moveUp(this.currentGridPoint);
        } else if(absDeltaRow >= absDeltaColumn && deltaRow <= 0) {
            moveDown(rowCount, this.currentGridPoint);
        }
    }

    public void jump(GridCell gridCell, float maxForce) {
        currentGridPoint = gridCell.getGridPosition();
    }
}
