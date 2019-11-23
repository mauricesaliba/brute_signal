package mt.com.go.apoe.model.plan;

public class UiWall extends Wall {

    private Point start;
    private Point end;

    public UiWall(Point start, Point end, Material material, int thickness) {
        super(material, thickness);

        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

}
