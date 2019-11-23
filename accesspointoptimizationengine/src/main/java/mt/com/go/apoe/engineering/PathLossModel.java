package mt.com.go.apoe.engineering;

import mt.com.go.apoe.model.grid.GridPoint;
import mt.com.go.apoe.model.grid.Gridster;
import mt.com.go.apoe.model.plan.GridWall;
import mt.com.go.apoe.model.plan.Wall;

public class PathLossModel {

    private final int gridCellSize;

    public PathLossModel(int gridCellSize) {
        this.gridCellSize = gridCellSize;
    }

    public void findLine(PathLossModel.PathLossModelCache cache, int x1, int y1, int x2, int y2, float loss) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;

        int err = dx - dy;
        int e2;

        while (true) {
            cache.cache[x1][y1] = loss;

            if (x1 == x2 && y1 == y2)
                break;

            e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x1 = x1 + sx;
            }

            if (e2 < dx) {
                err = err + dx;
                y1 = y1 + sy;
            }
        }
    }

    public PathLossModel.PathLossModelCache generateCache(Wall walls[]) {
        GridPoint gridPoint = new Gridster().getGridDimensions(walls);

        PathLossModel.PathLossModelCache cache = new PathLossModel.PathLossModelCache(gridPoint.getColumn(), gridPoint.getRow());

        //loss is currently set as a fixed value (10)
        for (Wall wall : walls) {
            GridWall gridWall = (GridWall) wall;
            findLine(
                    cache,
                    gridWall.getGridPointStart().getColumn() / gridCellSize,
                    gridWall.getGridPointStart().getRow() / gridCellSize,
                    gridWall.getGridPointEnd().getColumn() / gridCellSize,
                    gridWall.getGridPointEnd().getRow() / gridCellSize,
                    10.0f);
        }

        return cache;
    }

    public class PathLossModelCache {

        public int dim_x;
        public int dim_y;
        public float cache[][];

        public PathLossModelCache(int Dim_x, int Dim_y) {
            dim_x = Dim_x;
            dim_y = Dim_y;

            cache = new float[dim_x][dim_y];
        }
    }

}