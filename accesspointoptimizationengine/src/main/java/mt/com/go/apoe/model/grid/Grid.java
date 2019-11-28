package mt.com.go.apoe.model.grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grid {

    private GridCell[][] gridCells;

    private Grid() {}

    public static Grid create(int rows, int columns) {
        GridCell[][] gridCells = new GridCell[rows][columns];

        for (int i = 0; i < gridCells.length; i++) {
            for (int j = 0; j < gridCells[0].length; j++) {
                gridCells[i][j] = new GridCell(new GridPoint(i, j));
            }
        }

        return new Grid().setGridCells(gridCells);
    }

    public GridCell[][] getGridCells() {
        return gridCells;
    }

    private Grid setGridCells(GridCell[][] gridCells) {
        this.gridCells = gridCells;

        return this;
    }

    public int getRows() {
        return gridCells.length;
    }

    public int getColumns() {
        return getRows() == 0 ? 0 : gridCells[0].length;
    }

    public boolean isNotOutOfBounds(int row, int column) {
        return row >= 0 && column >= 0 && row < getRows() && column < getColumns();
    }

    public GridCell[] getNeighbouringCells(int row, int column) {
        GridPoint[] neighbouringCellPositions = new GridPoint[] {
                new GridPoint(row, column + 1),
                new GridPoint(row + 1, column + 1),
                new GridPoint(row + 1, column),
                new GridPoint(row + 1, column - 1),
                new GridPoint(row, column - 1),
                new GridPoint(row - 1, column - 1),
                new GridPoint(row - 1, column),
                new GridPoint(row - 1, column + 1),
        };

        return Arrays.stream(neighbouringCellPositions).parallel()
                .filter(gridPoint -> isNotOutOfBounds(gridPoint.getRow(), gridPoint.getColumn()))
                .map(gridPoint -> this.gridCells[gridPoint.getRow()][gridPoint.getColumn()])
                .toArray(GridCell[]::new);
    }

    public static List<GridPoint> findLine(int x0, int y0, int x1, int y1) {
        List<GridPoint> line = new ArrayList<>();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        int err = dx - dy;
        int e2;

        while (true) {
            line.add(new GridPoint(y0, x0));

            if (x0 == x1 && y0 == y1)
                break;

            e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x0 = x0 + sx;
            }

            if (e2 < dx) {
                err = err + dx;
                y0 = y0 + sy;
            }
        }
        return line;
    }

}
