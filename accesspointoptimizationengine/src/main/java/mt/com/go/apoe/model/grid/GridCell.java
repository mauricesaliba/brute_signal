package mt.com.go.apoe.model.grid;

public class GridCell {

    private GridPoint gridPosition;
    private Type type;

    public enum Type {
        WALL,
        INSIDE,
        OUTSIDE,
        UNKNOWN
    }

    public GridCell(GridPoint gridPosition) {
        this.gridPosition = gridPosition;
        this.type = Type.UNKNOWN;
    }

    public GridPoint getGridPosition() {
        return gridPosition;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isOutside() {
        return type == Type.OUTSIDE;
    }

    public boolean isInside() {
        return type == Type.INSIDE;
    }

    public boolean isWall() {
        return type == Type.WALL;
    }

}
