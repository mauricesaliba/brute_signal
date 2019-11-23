package mt.com.go.apoe.model;


import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

/** Class Bresenham **/
// https://www.sanfoundry.com/java-program-bresenham-line-algorithm/
private class Bresenham {
    /**
     * function findLine() - to find that belong to line connecting the two points
     **/
    public List<Point> findLine(Point[][] grid, int x0, int y0, int x1, int y1) {
        List<Point> line = new ArrayList<Point>();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        int err = dx - dy;
        int e2;

        while (true) {
            line.add(grid[x0][y0]);

            if (x0 == x1 && y0 == y1)
                break;

            e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x0 = x0 + sx;
            }

            if (e2 < dx) {
                err = err + dx;
                y0 = y0 + sy;
            }
        }
        return line;
    }
}

//	accessPoint <- getBestAccessPointToMove(signalStrengthMap, cell, accessPoints)


// input : signalStrengthMap,cell, accessPoints
//
//
//
//
//
//  grid_size = 40
//  AP_strength_list= []
//  best_AP_index= 0
//
//  # Check sum strength for every AP
//  for i in range(len(accessPoints):
//      sum_signal_strength = 0
//      # get the list of the affected cells
//      List<Point> line = b.findLine(grid_size, apl_x, apl_y, cell_x, cell_y);
//
//      for i in range(len(line)):
//          sum_signal_strength += signalStrengthMap[line[i][x]][line[i][y]
//
//      AP_strength_list.append(sum_signal_strength)
//
//
//  for i in range(len(AP_strength_list):
//      if AP_strength_list[i] == min(AP_strength_list)
//      best_AP_index = i
//
//
//
//
//
//
//
//
//
// select the AP to move based on the lowest amount of of sum
// or
// select the AP which results in the smallest delta from the optimal overall state
//
// make the move


