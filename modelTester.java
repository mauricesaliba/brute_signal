import PathlossModel.AccessPoint;

public class modelTester{

public static class FormattedTablePrint {

   public static void printTable(double twoDm[][]) {
       for(int i=0; i < twoDm[0].length; i++ ) {
        for(int j=0; j < twoDm.length; j++) {
            System.out.print(twoDm[j][i]);
            System.out.print("\t");
        }
        System.out.println();
        }
    } 
}


public static void main(String[] args) {
    PathlossModel model = new PathlossModel(0.20);

    PathlossModel.WallObject[] walls = new PathlossModel.WallObject[4];

    //walls[0] = model.new WallObject(1.0, 3.0, 1.0, 4.0, PathlossModel.MaterialEnum.CONCRETE, 20.0);

    walls[0] = model.new WallObject(1.0, 1.0, 4.0, 4.0, PathlossModel.MaterialEnum.CONCRETE, 20.0);
    walls[1] = model.new WallObject(4.0, 4.0, 10.0, 4.0, PathlossModel.MaterialEnum.CONCRETE, 20.0);
    walls[2] = model.new WallObject(10.0, 4.0, 10.0, 1.0, PathlossModel.MaterialEnum.CONCRETE, 20.0);
    walls[3] = model.new WallObject(10.0, 1.0, 1.0, 1.0, PathlossModel.MaterialEnum.CONCRETE, 20.0);
    //walls[4] = model.new WallObject(2.6, 2.6, 10, 2.6, PathlossModel.MaterialEnum.CONCRETE, 20.0);

    PathlossModel.PathlossModelCache cache = model.GenerateCache(walls);

    PathlossModel.AccessPoint[] myAPs = new PathlossModel.AccessPoint[] {model.new AccessPoint(24, 10, 3.0, 0.4),model.new AccessPoint(24, 16, 3.0, 0.4)};

    double[][] heatMap = model.generateHeatmap(cache, myAPs, false);

    FormattedTablePrint.printTable(cache.cache);
    System.out.println();
    FormattedTablePrint.printTable(heatMap);

}
    

}
