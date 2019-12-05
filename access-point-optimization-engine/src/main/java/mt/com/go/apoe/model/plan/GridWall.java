package mt.com.go.apoe.model.plan;

import lombok.Getter;
import mt.com.go.apoe.model.grid.GridPoint;

public class GridWall extends Wall {

    @Getter
    private GridPoint gridPointStart;
    @Getter
    private GridPoint gridPointEnd;

    public GridWall(GridPoint gridPointStart, GridPoint gridPointEnd, Material material, int thickness) {
//        super.setMaterial(material);
//        super.setThickness(thickness);

        super(material, thickness);

        this.gridPointStart = gridPointStart;
        this.gridPointEnd = gridPointEnd;
    }

}
