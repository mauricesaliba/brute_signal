package mt.com.go.apoe.data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MapWriter {

    private final String path = "results";

    protected void write(BufferedImage image, String childPath, String fileName) {
        Path dir = Paths.get(path + "/" + childPath);
        if (!Files.exists(dir)) {
            try {
                Files.createDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File outputFile = new File(dir.toString() + "/" + fileName + ".png");
        try {
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
