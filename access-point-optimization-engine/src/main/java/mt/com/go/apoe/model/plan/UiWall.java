package mt.com.go.apoe.model.plan;

import lombok.Getter;

@Getter
public class UiWall extends Wall {

    private Point start;
    private Point end;

    public UiWall(Material material, int thickness, Point start, Point end) {
        super(material, thickness);

        this.start = start;
        this.end = end;
    }
}
