package mt.com.go.apoe.model.grid;

public class GridCell {

    private GridPoint gridPoint;
    private boolean usable = true;
    private boolean wall;
    private boolean visited;

    public GridCell(GridPoint gridPoint) {
        this.gridPoint = gridPoint;
    }

    public GridPoint getGridPoint() {
        return gridPoint;
    }

    public boolean isUsable() {
        return usable;
    }

    public GridCell setUsable(boolean usable) {
        this.usable = usable;
        return this;
    }

    public boolean isVisited() {
        return visited;
    }

    public GridCell setVisited(boolean visited) {
        this.visited = visited;
        return this;
    }

    public boolean isNotAWall() {
        return !wall;
    }

    public GridCell setWall(boolean wall) {
        this.wall = wall;
        return this;
    }

    @Override
    public String toString() {
        return "GridCell{" +
                "gridPoint=" + gridPoint +
                '}';
    }
}
