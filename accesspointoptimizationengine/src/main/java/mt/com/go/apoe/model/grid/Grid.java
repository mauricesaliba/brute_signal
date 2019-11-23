package mt.com.go.apoe.model.grid;

public class Grid {

    private GridCell[][] gridCells;

    public Grid(GridCell[][] gridCells) {
        this.gridCells = gridCells;
    }

    public GridCell[][] getGridCells() {
        return gridCells;
    }

    public int getRows() {
        return gridCells.length;
    }

    public int getColumns() {
        return getRows() == 0 ? 0 : gridCells[0].length;
    }

}
