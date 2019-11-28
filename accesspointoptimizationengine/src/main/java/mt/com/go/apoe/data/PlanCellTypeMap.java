package mt.com.go.apoe.data;

import mt.com.go.apoe.model.grid.Grid;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class PlanCellTypeMap extends MapWriter {

    public void generateHeatMapImage(Grid planLayoutGrid) {
        BufferedImage debugImage = new BufferedImage(
                planLayoutGrid.getColumns(),
                planLayoutGrid.getRows(),
                BufferedImage.TYPE_INT_RGB
        );

        Arrays.stream(planLayoutGrid.getGridCells()).parallel().flatMap(x -> Arrays.stream(x))
                .forEach(gridCell -> {
                    Color color;
                    if (gridCell.isInside()) {
                        color = Color.GREEN;
                    } else if (gridCell.isOutside()) {
                        color = Color.RED;
                    } else if (gridCell.isWall()) {
                        color = Color.BLACK;
                    } else {
                        color = Color.WHITE;
                    }
                    debugImage.setRGB(gridCell.getGridPosition().getColumn(), gridCell.getGridPosition().getRow(), color.getRGB());
                });

        super.write(debugImage, "", "planLayout");
    }
}
