package mt.com.go.apoe;

import mt.com.go.apoe.model.plan.Material;
import mt.com.go.apoe.model.plan.Point;
import mt.com.go.apoe.model.plan.UiWall;
import mt.com.go.apoe.model.plan.Wall;

import javax.print.DocFlavor;

public class Main {

    public static void main(String[] params) {
        System.out.println("hello");

        new OptimizationEngine().getOptimalSolution(getTestingUIWalls());
    }

    private static Wall[] getTestingUIWalls() {

        Wall wall1 = new UiWall(new Point(50, 50), new Point(150, 50), Material.CONCRETE, 50);

        Wall wall2 = new UiWall(new Point(150, 50), new Point(150, 150), Material.CONCRETE, 50);

        Wall wall3 = new UiWall(new Point(150, 150), new Point(50, 150), Material.CONCRETE, 50);

        Wall wall4 = new UiWall(new Point(50, 150), new Point(50, 50), Material.CONCRETE, 50);

        Wall walls[] = new UiWall[4];
        walls[0] = wall1;
        walls[1] = wall2;
        walls[2] = wall3;
        walls[3] = wall4;

        return walls;
    }
}
