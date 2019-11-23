package mt.com.go.apoe.engineering;

import mt.com.go.apoe.model.grid.CellPosition;

public interface Movement {

    default void moveUp(CellPosition cellPosition) {
        int row = cellPosition.getRow() <= 0 ? 0 : cellPosition.getRow() - 1;
        cellPosition.setRow(row);
    }

    default void moveDown(CellPosition cellPosition) {
        int row = cellPosition.getRow() >  <= 0 ? 0 : cellPosition.getRow() - 1;
        cellPosition.setRow(row);
    }

}
