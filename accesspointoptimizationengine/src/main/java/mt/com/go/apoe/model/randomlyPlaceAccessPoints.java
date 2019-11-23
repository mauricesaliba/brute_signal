package mt.com.go.apoe.model;


import java.util.ArrayList;
import java.util.List;

public class randomlyPlaceAccessPoints {
    static void myMethod(GridCell[][] vacanyGrid,int accessPointCount) {
        List<Integer[]> accesPointPlace = new ArrayList<Integer[]>();

        for (int i = 0; i < accessPointCount; i ++){
            int x,y = 0;
            boolean got_it = false;
            while(!got_it){
                x = (int) Math.random();
                y = (int) Math.random();

                if (vacanyGrid[x][y].isUsable()){
                    accesPointPlace.add(new Integer[]{x,y});
                    vacanyGrid[x][y] = new GridCell(false);
                    got_it = true;
                }
            }

        }
        // accesPointPlace : [[x1,y1], [x2,y2], [x3,y3]] empty at beginning
        // for i in range(len(accespointcount)):
        //      x value : empty
        //      y value : empty
        //
        //      got_it = False
        //      while got_it not True:
        //          choose random x
        //          choose random y
        //          if vacancyGrid[x,y] usable
        //              accesPointPlace[i] = vacancyGrid[x,y]
        //              vacancyGrid[x,y] = not usable
        //              got_it = true
        //
        //
    }
}

