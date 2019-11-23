package mt.com.go.apoe.model.grid;

public class Grid {

    private Cell[][] cells;

    public Grid(Cell[][] cells) {
        this.cells = cells;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public int getRows() {
        return cells.length;
    }

    public int getColumns() {
        return getRows() == 0 ? 0 : cells[0].length;
    }

}
