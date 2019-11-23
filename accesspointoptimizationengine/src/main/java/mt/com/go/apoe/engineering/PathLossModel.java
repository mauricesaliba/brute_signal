package mt.com.go.apoe.engineering;

import mt.com.go.apoe.model.plan.Material;

public class PathLossModel {

    public void findLine(PathLossModelCache cache, int x1, int y1, int x2, int y2, float loss) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
 
        int sx = x1 < x2 ? 1 : -1; 
        int sy = y1 < y2 ? 1 : -1; 
 
        int err = dx-dy;
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

    public class PathLossModelCache {
        public int dim_x;
        public int dim_y;
        public float cache[][];

        public PathLossModelCache(int Dim_x, int Dim_y){
            dim_x = Dim_x;
            dim_y = Dim_y;

            cache = new float[dim_x][dim_y];
        }
    }

    class WallObject{
        public float x1;
        public float x2;
        public float y1;
        public float y2;
        public Material material;
        public float thickness;

        public WallObject(float X1, float X2, float Y1, float Y2, Material Material, float Thickness){
            x1 = X1;
            x2 = X2;
            y1 = Y1;
            y2 = Y2;
            material = Material;
            thickness = Thickness;
        }
    }

    int _resolution = 20;

    public PathLossModel(int resolution){
        _resolution = resolution;
    }

    public static PathLossModelCache generateCache(WallObject walls[]){

        float min_x = Float.MAX_VALUE;
        float min_y = Float.MAX_VALUE;
        float max_x = Float.MIN_VALUE;
        float max_y = Float.MIN_VALUE;

        for (WallObject wallObject : walls) {
            if (wallObject.x1 < min_x){ min_x = wallObject.x1; }
            if (wallObject.y1 < min_y){ min_y = wallObject.y1; }
            if (wallObject.x2 < min_x){ min_x = wallObject.x2; }
            if (wallObject.y2 < min_y){ min_y = wallObject.y2; }

            if (wallObject.x1 > max_x){ max_x = wallObject.x1; }
            if (wallObject.y1 > max_y){ max_y = wallObject.y1; }
            if (wallObject.x2 > max_x){ max_x = wallObject.x2; }
            if (wallObject.y2 > max_y){ max_y = wallObject.y2; }
        }

        int dim_x = Math.ceil((max_x+min_x)/_resolution);
        int dim_y = Math.ceil((max_y+min_y)/_resolution);

        PathLossModelCache cache = new PathLossModelCache(dim_x, dim_y);

        //loss is currently set as a fixed value (10)
        for (WallObject wallObject : walls) {
            findLine(cache, Int(wallObject.x1/_resolution), Int(wallObject.y1/_resolution), Int(wallObject.x2/_resolution), Int(wallObject.y2/_resolution), 10.0);
        }

        return cache;
    }

}