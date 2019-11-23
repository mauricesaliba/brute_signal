import java.lang.reflect.Array;
import java.util.stream.Stream;
import jdk.nashorn.internal.runtime.StoredScript;
import java.util.Arrays;

/* PathlossModel.java
 */

public class PathlossModel
{
    public void findLine(final PathlossModel.PathlossModelCache cache, final int _x1, final int _y1, final int x2, final int y2, final double loss) 
    { 
        int x1 = _x1;
        int y1 = _y1;

        final int dx = Math.abs(x2 - x1);
        final int dy = Math.abs(y2 - y1);
 

        final int sx = x1 < x2 ? 1 : -1; 
        final int sy = y1 < y2 ? 1 : -1; 
 
        int err = dx-dy;
        int e2;
 
        while (true) 
        {
            cache.cache[x1][y1] = loss;
 
            if (x1 == x2 && y1 == y2) 
                break;
 
            e2 = 2 * err;
            if (e2 > -dy) 
            {
                err = err - dy;
                x1 = x1 + sx;
            }
 
            if (e2 < dx) 
            {
                err = err + dx;
                y1 = y1 + sy;
            }
        }                                
    }

    public double findLoss(PathlossModel.PathlossModelCache cache, int x1, int y1, final int x2, final int y2) 
    { 
        final int dx = Math.abs(x2 - x1);
        final int dy = Math.abs(y2 - y1);
 
        final int sx = x1 < x2 ? 1 : -1; 
        final int sy = y1 < y2 ? 1 : -1; 
 
        int err = dx-dy;
        int e2;
        double totalLoss = 0;
 
        while (true) 
        {
            totalLoss += cache.cache[x1][y1];
 
            if (x1 == x2 && y1 == y2) 
                break;
 
            e2 = 2 * err;
            if (e2 > -dy) 
            {
                err = err - dy;
                x1 = x1 + sx;
            }
 
            if (e2 < dx) 
            {
                err = err + dx;
                y1 = y1 + sy;
            }
        }                                
        return totalLoss;
    }


    public class PathlossModelCache{
        public int dim_x;
        public int dim_y;
        public double cache[][];

        public PathlossModelCache(final int Dim_x, final int Dim_y){
            dim_x = Dim_x;
            dim_y = Dim_y;

            cache = new double[dim_x][dim_y];
        }
    }

    public enum MaterialEnum{
        CONCRETE, LIMESTONE, WOOD, GLASS
    }

    public class WallObject{
        public double x1;
        public double x2;
        public double y1;
        public double y2;
        public MaterialEnum material;
        public double thickness;

        public WallObject(final double X1, final double Y1, final double X2, final double Y2, final MaterialEnum Material, final double Thickness){
            x1 = X1;
            x2 = X2;
            y1 = Y1;
            y2 = Y2;
            material = Material;
            thickness = Thickness;
        }
    }

    public class AccessPoint{
        public int x1;
        public int y1;
        public double antennaGain;
        public double txPower;

        public AccessPoint(final int X1, int Y1, double AntennaGain, double TxPower){
            x1 = X1;
            y1 = Y1;
            antennaGain = AntennaGain;
            txPower = TxPower;
        }
    }

    double _resolution = 0.20;

    public PathlossModel(final double resolution){
        _resolution = resolution;
    }

    public PathlossModel(){

    }


    double CalculateRxPower (double Distance, double WallLoss, double TransmitPower, boolean Is24GHz, double AntennaGain){

        double distance = Distance;
        double antennaGainAP = AntennaGain;
        double txPower = TransmitPower;
        double antennaGainDevice = 0;
        double wallLoss = WallLoss;
        double frequency = (Is24GHz)? 2400 : 5000;
        double gains = txPower + antennaGainDevice + antennaGainAP;
        double powerLossCoefficient = 28;
        
        double pathLoss = 20 * Math.log10(frequency) + powerLossCoefficient * Math.log10(distance) + wallLoss - powerLossCoefficient;
        
        return gains - pathLoss;
        
    }

    public PathlossModel.PathlossModelCache GenerateCache(final WallObject walls[]){

        double min_x = Double.POSITIVE_INFINITY; 
        double min_y = Double.POSITIVE_INFINITY;
        double max_x = Double.NEGATIVE_INFINITY;
        double max_y = Double.NEGATIVE_INFINITY;

        for (final WallObject wallObject : walls) {
            if (wallObject.x1 < min_x){ min_x = wallObject.x1; }
            if (wallObject.y1 < min_y){ min_y = wallObject.y1; }
            if (wallObject.x2 < min_x){ min_x = wallObject.x2; }
            if (wallObject.y2 < min_y){ min_y = wallObject.y2; }

            if (wallObject.x1 > max_x){ max_x = wallObject.x1; }
            if (wallObject.y1 > max_y){ max_y = wallObject.y1; }
            if (wallObject.x2 > max_x){ max_x = wallObject.x2; }
            if (wallObject.y2 > max_y){ max_y = wallObject.y2; }
        }

        int dim_x = (int) Math.ceil((max_x+min_x)/_resolution);
        int dim_y = (int) Math.ceil((max_y+min_y)/_resolution);

        dim_x = dim_x + 1;
        dim_y = dim_y + 1;

        final PathlossModel.PathlossModelCache cache = new PathlossModel.PathlossModelCache(dim_x, dim_y);

        //loss is currently set as a fixed value (10)
        for (final WallObject wallObject : walls) {
            findLine(cache, (int) (wallObject.x1/_resolution), (int) (wallObject.y1/_resolution), (int) (wallObject.x2/_resolution), (int) (wallObject.y2/_resolution), 5.0);
        }

        return cache;
    }

    public double[][] generateHeatmap(PathlossModel.PathlossModelCache cache, AccessPoint APs[], boolean AccumulativeHeatMap){
        double[][] heatMap = new double[cache.dim_x][cache.dim_y]; 
        Arrays.stream(heatMap).forEach(a -> Arrays.fill(a, Double.NEGATIVE_INFINITY));
        //Array.fill(heatMap, Double.NEGATIVE_INFINITY);

        for (AccessPoint AP : APs) {
            for (int i = 0; i < cache.dim_x; i++) {
                for (int j = 0; j < cache.dim_y; j++) {
                    if (i == AP.x1 && j == AP.y1){
                        if (AccumulativeHeatMap) { 
                            heatMap[i][j] += AP.txPower;
                        }
                        else {
                            heatMap[i][j] = Math.max(AP.txPower, heatMap[i][j]);
                        }
                        continue;
                    }
                    double distance = Math.sqrt((Math.pow((AP.x1-i), 2))+(Math.pow(AP.y1-j, 2)))*_resolution;
                    double totalLoss = findLoss(cache, (int) (AP.x1), (int) (AP.y1), (i), (j));
                    double recievedPower = Math.round(CalculateRxPower(distance, totalLoss, AP.txPower, true, AP.antennaGain));
                    if (AccumulativeHeatMap) { 
                        heatMap[i][j] += recievedPower;
                    }
                    else {
                        heatMap[i][j] = Math.max(recievedPower, heatMap[i][j]);
                    }
                }
            }
        }
    return heatMap;
    }
}