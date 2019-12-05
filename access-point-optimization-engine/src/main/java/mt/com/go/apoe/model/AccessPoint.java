package mt.com.go.apoe.model;

import mt.com.go.apoe.model.grid.Grid;
import mt.com.go.apoe.model.grid.GridCell;
import mt.com.go.apoe.model.grid.GridPoint;
import mt.com.go.apoe.model.grid.Movement;

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

    public void jump(Grid planLayoutGrid, float maxForce, GridCell gridCell) {
        float diffRow = gridCell.getGridPosition().getRow() - currentGridPoint.getRow() ;
        float diffCol = gridCell.getGridPosition().getColumn() - currentGridPoint.getColumn();

        int directionRow = (int) (diffRow / Math.abs(diffRow));
        int directionCol = (int) (diffCol / Math.abs(diffCol));

        float randomForce = (float) (Math.random() * maxForce);

        int row = Math.round(currentGridPoint.getRow() + directionRow * randomForce);
        int col = Math.round(currentGridPoint.getColumn() + directionCol * randomForce);

        if (planLayoutGrid.isNotOutOfBounds(row, col) && planLayoutGrid.getGridCells()[row][col].isInside()) {
            currentGridPoint = planLayoutGrid.getGridCells()[row][col].getGridPosition();
        }
    }
}
