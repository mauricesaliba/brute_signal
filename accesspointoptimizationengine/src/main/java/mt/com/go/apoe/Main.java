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

        Wall wall1 = new UiWall(new Point(5, 5), new Point(15, 5), Material.CONCRETE, 50);

        Wall wall2 = new UiWall(new Point(15, 5), new Point(15, 15), Material.CONCRETE, 50);

        Wall wall3 = new UiWall(new Point(15, 15), new Point(5, 15), Material.CONCRETE, 50);

        Wall wall4 = new UiWall(new Point(5, 15), new Point(5, 5), Material.CONCRETE, 50);

        Wall walls[] = new UiWall[4];
        walls[0] = wall1;
        walls[1] = wall2;
        walls[2] = wall3;
        walls[3] = wall4;

        return walls;
    }
}
