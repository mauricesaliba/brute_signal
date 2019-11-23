package mt.com.go.apoe.model.plan;

import mt.com.go.apoe.model.grid.GridPoint;

public class GridWall extends Wall {

    private GridPoint gridPoint;

    public GridWall(GridPoint gridPoint, Material material, int thickness) {
        super(material, thickness);

        this.gridPoint = gridPoint;
    }

    public GridPoint getGridPoint() {
        return gridPoint;
    }

}
