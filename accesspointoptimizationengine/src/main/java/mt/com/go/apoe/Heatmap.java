package mt.com.go.apoe;

import mt.com.go.apoe.model.grid.Grid;
import mt.com.go.apoe.model.grid.GridPoint;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Heatmap {

    private static final float MAX_HEAT                  = 0.4f;
    private static final float MIN_HEAT                  = -80f;
    private static final String path  = "results/test_data";

    public static void generateHeatMapImage(double[][] signalStrengthHeatMap, int step, int accessPointCount, GridPoint attractionPoint, Grid usabilityGrid) {
        try {
            BufferedImage debugImage = new BufferedImage(
                    signalStrengthHeatMap[0].length,
                    signalStrengthHeatMap.length,
                    BufferedImage.TYPE_INT_RGB
            );
            for (int row = 0; row < signalStrengthHeatMap.length; row++) {
                for (int col = 0; col < signalStrengthHeatMap[0].length; col++) {
                    Color color = generateColor(signalStrengthHeatMap[row][col]);
                    debugImage.setRGB(col, row, color.getRGB());
                    if(!usabilityGrid.getGridCells()[row][col].isNotAWall()){
                        Color wallColor = Color.WHITE;
                        debugImage.setRGB(col,row, wallColor.getRGB());
                    }
                }
            }
            debugImage.setRGB(attractionPoint.getColumn(), attractionPoint.getRow()-1, Color.BLACK.getRGB());
            debugImage.setRGB(attractionPoint.getColumn(), attractionPoint.getRow()+1, Color.BLACK.getRGB());
            debugImage.setRGB(attractionPoint.getColumn()+1, attractionPoint.getRow(), Color.BLACK.getRGB());
            debugImage.setRGB(attractionPoint.getColumn()-1, attractionPoint.getRow(), Color.BLACK.getRGB());
            debugImage.setRGB(attractionPoint.getColumn(), attractionPoint.getRow(), Color.BLACK.getRGB());
            Path dir = Paths.get("results");
            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
            }
            dir = Paths.get( path + "_" + accessPointCount);
            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
            }
            File outputfile = new File(dir.toString() + "/HeatMapResult" + step + ".png");
            ImageIO.write(debugImage, "png", outputfile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static Color generateColor(double v) {
        float threshold = MAX_HEAT - MIN_HEAT;
        float heatPercentage = (float) -v / threshold;
        if (heatPercentage > 1f) {
            heatPercentage = 1f;
        }
        if (heatPercentage < 0f) {
            heatPercentage = 0f;
        }

        float sat = 255;
        float lum = 255;
        float hue = 2*150 * (1f - heatPercentage)/2;

        return Color.getHSBColor(hue/255f,sat/255f,lum/255f);
    }

}
