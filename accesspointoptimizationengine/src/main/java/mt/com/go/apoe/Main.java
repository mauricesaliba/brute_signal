package mt.com.go.apoe;

import mt.com.go.apoe.model.plan.Material;
import mt.com.go.apoe.model.plan.Point;
import mt.com.go.apoe.model.plan.UiWall;
import mt.com.go.apoe.model.plan.Wall;

public class Main {

    public static void main(String[] params) {
        System.out.println("hello");

        new OptimizationEngine(getTestingUIWalls()).optimize();
    }

    private static UiWall[] getTestingUIWalls() {

        UiWall wall1 = new UiWall(new Point(5, 5), new Point(15, 5), Material.CONCRETE, 20);
        UiWall wall2 = new UiWall(new Point(15, 5), new Point(15, 15), Material.CONCRETE, 20);
        UiWall wall3 = new UiWall(new Point(15, 15), new Point(5, 15), Material.CONCRETE, 20);
        UiWall wall4 = new UiWall(new Point(5, 15), new Point(5, 5), Material.CONCRETE, 20);
        UiWall wall5 = new UiWall(new Point(8, 10), new Point(8, 5), Material.CONCRETE, 20);
        UiWall wall6 = new UiWall(new Point(5, 10), new Point(8, 10), Material.CONCRETE, 20);

        UiWall walls[] = new UiWall[6];
        walls[0] = wall1;
        walls[1] = wall2;
        walls[2] = wall3;
        walls[3] = wall4;
        walls[4] = wall5;
        walls[5] = wall6;

        return walls;
    }
}
