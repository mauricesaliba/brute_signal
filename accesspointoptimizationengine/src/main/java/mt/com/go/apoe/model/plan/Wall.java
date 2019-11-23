package mt.com.go.apoe.model.plan;

public class Wall {

    private Point start;
    private Point end;
    private Material material;
    private int thickness;

    public Wall(Point start, Point end, Material material, int thickness) {
        this.start = start;
        this.end = end;
        this.material = material;
        this.thickness = thickness;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public Material getMaterial() {
        return material;
    }

    public int getThickness() {
        return thickness;
    }
}
