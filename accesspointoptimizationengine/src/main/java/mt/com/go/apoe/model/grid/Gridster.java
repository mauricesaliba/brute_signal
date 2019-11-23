package mt.com.go.apoe.model.grid;

import mt.com.go.apoe.model.grid.GridCell;
import mt.com.go.apoe.model.grid.GridPoint;
import mt.com.go.apoe.model.plan.GridWall;
import mt.com.go.apoe.model.plan.Point;
import mt.com.go.apoe.model.plan.Wall;

public class Gridster {

    public Grid generateUsabilityGrid(Wall[] walls) {
        GridPoint dimensions = getGridDimensions(walls);
        GridCell[][] gridCells = new GridCell[dimensions.getRow()][dimensions.getColumn()];

        int rows = gridCells.length;
        int columns = gridCells[0].length;

        OutsideInside outsideInside = new OutsideInside();

        for(int i = 0; i < rows; i++) {
            for(int k = 0; k < columns; k++){
                GridCell gridCell = new GridCell(new GridPoint(i, k), false);
                GridPoint gridPoint = gridCell.getGridPoint();
                Point point = new Point(gridPoint.getRow(),gridPoint.getColumn());

                if(outsideInside.isInside(walls, point)) {
                    gridCell.setToUsable();
                }
                gridCells[i][k] = gridCell;
            }
        }

        return new Grid(gridCells);
    }

    public void printCellGrid(GridCell[][] gridCells) {
        int rows = gridCells.length;
        int columns = gridCells[0].length;

        for(int i=0; i < rows; i++ ) {
            for(int k=0; k < columns; k++ ) {
                if(gridCells[i][k].isUsable()) {
                    System.out.print('1');
                } else {
                    System.out.print('0');
                }
            }
            System.out.println();
        }
    }


    public GridPoint getGridDimensions(Wall walls[]){
        int _resolution = 1;

        float min_x = Float.MAX_VALUE;
        float min_y = Float.MAX_VALUE;
        float max_x = Float.MIN_VALUE;
        float max_y = Float.MIN_VALUE;

        for (Wall wall : walls) {

            GridWall gridWall = (GridWall) wall;

            float x1 = gridWall.getGridPointStart().getRow();
            float y1 = gridWall.getGridPointStart().getColumn();

            float x2 = gridWall.getGridPointEnd().getRow();
            float y2 = gridWall.getGridPointEnd().getColumn();


            if (x1 < min_x){ min_x = x1; }
            if (y1 < min_y){ min_y = y1; }
            if (x2 < min_x){ min_x = x2; }
            if (y2 < min_y){ min_y = y2; }

            if (x1 > max_x){ max_x = x1; }
            if (y1 > max_y){ max_y = y1; }
            if (x2 > max_x){ max_x = x2; }
            if (y2 > max_y){ max_y = y2; }
        }

        int dim_x = (int) Math.ceil((max_x+min_x)/_resolution);
        int dim_y = (int) Math.ceil((max_y+min_y)/_resolution);

        GridPoint gridPoint = new GridPoint(dim_x, dim_y);

        return gridPoint;
    }

}
