package mt.com.go.apoe.model.grid;

import mt.com.go.apoe.model.plan.GridWall;
import mt.com.go.apoe.model.plan.Wall;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Gridster {

    public Grid generateUsabilityGrid(GridWall[] walls) {
        Grid grid = convertWallsToGridCells(walls);

        Stack<GridCell> stack = new Stack<>();
        stack.add(grid.getGridCells()[0][0]);

        while(!stack.isEmpty()) {
            GridCell gridCell = stack.pop();

            if(gridCell.getType() != GridCell.Type.UNKNOWN){
                continue;
            }

            gridCell.setType(GridCell.Type.OUTSIDE);

            GridCell[] neighbouringCells = grid.getNeighbouringCells(
                    gridCell.getGridPosition().getRow(),
                    gridCell.getGridPosition().getColumn());

            Arrays.stream(neighbouringCells).parallel()
                    .filter(neighbouringCell -> !neighbouringCell.isWall())
                    .forEach(neighbouringCell -> stack.push(neighbouringCell));
        }

        Arrays.stream(grid.getGridCells()).parallel().flatMap(row -> Arrays.stream(row))
                .filter(gridCell -> gridCell.getType() == GridCell.Type.UNKNOWN)
                .forEach(gridCell -> gridCell.setType(GridCell.Type.INSIDE));

        return grid;
    }


    private Grid convertWallsToGridCells(GridWall[] walls) {
        GridPoint dimensions = Wall.getPlanDimensions(walls);
        Grid grid = Grid.create(dimensions.getRow(), dimensions.getColumn());

        for (GridWall wall : walls) {
            List<GridPoint> gridPoints = Grid.findLine(
                    wall.getGridPointStart().getColumn(),
                    wall.getGridPointStart().getRow(),
                    wall.getGridPointEnd().getColumn(),
                    wall.getGridPointEnd().getRow());
            gridPoints.parallelStream()
                    .forEach(gridPoint -> grid.getGridCells()[gridPoint.getRow()][gridPoint.getColumn()].setType(GridCell.Type.WALL));
        }

        return grid;
    }

}
