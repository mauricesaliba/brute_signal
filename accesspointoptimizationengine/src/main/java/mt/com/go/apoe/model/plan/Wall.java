package mt.com.go.apoe.model.plan;

import mt.com.go.apoe.model.grid.GridPoint;

public abstract class Wall {

    private Material material;

    //in CM
    private int thickness;

    protected Wall(Material material, int thickness) {
        this.material = material;
        this.thickness = thickness;
    }

    public Material getMaterial() {
        return material;
    }

    public int getThickness() {
        return thickness;
    }

    public static GridPoint getPlanDimensions(GridWall[] walls) {
        float min_x = Float.MAX_VALUE;
        float min_y = Float.MAX_VALUE;
        float max_x = Float.MIN_VALUE;
        float max_y = Float.MIN_VALUE;

        for (GridWall wall : walls) {
            float x1 = wall.getGridPointStart().getColumn();
            float y1 = wall.getGridPointStart().getRow();

            float x2 = wall.getGridPointEnd().getColumn();
            float y2 = wall.getGridPointEnd().getRow();

            if (x1 < min_x){ min_x = x1; }
            if (y1 < min_y){ min_y = y1; }
            if (x2 < min_x){ min_x = x2; }
            if (y2 < min_y){ min_y = y2; }

            if (x1 > max_x){ max_x = x1; }
            if (y1 > max_y){ max_y = y1; }
            if (x2 > max_x){ max_x = x2; }
            if (y2 > max_y){ max_y = y2; }
        }

        int dim_x = (int) Math.ceil((max_x+min_x));
        int dim_y = (int) Math.ceil((max_y+min_y));

        return new GridPoint(dim_y, dim_x);
    }

}
