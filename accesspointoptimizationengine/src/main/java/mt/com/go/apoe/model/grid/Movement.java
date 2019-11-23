package mt.com.go.apoe.model.grid;

public interface Movement {

    default void moveUp(GridPoint gridPoint) {
        int row = gridPoint.getRow() <= 0 ? 0 : gridPoint.getRow() - 1;
        gridPoint.setRow(row);
    }

    default void moveDown(int rowCount, GridPoint gridPoint) {
        int row = gridPoint.getRow() > rowCount ? gridPoint.getRow() : gridPoint.getRow() + 1;
        gridPoint.setRow(row);
    }

    default void moveRight(int columnCount, GridPoint gridPoint) {
        int row = gridPoint.getColumn() > columnCount ? gridPoint.getColumn() : gridPoint.getColumn() + 1;
        gridPoint.setRow(row);
    }

    default void moveLeft(GridPoint gridPoint) {
        int row = gridPoint.getColumn() <= 0 ? 0 : gridPoint.getColumn() - 1;
        gridPoint.setRow(row);
    }

}
