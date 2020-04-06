package org.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

public class PrimaryController {

    public ImageView imageView;
    public Image blackAndWhite;

    public void initialize() {
        File file = new File("src/main/resources/org/example/romeMap.jpg");
        Image im = new Image(String.valueOf(file.toURI()), 920, 760, false, false);
        blackAndWhite = setBlackWhite(im);
        imageView.setImage(blackAndWhite);

    }

    public WritableImage setBlackWhite(Image initialImage) {
        int width = (int) initialImage.getWidth();
        int height = (int) initialImage.getHeight();
        PixelReader pixelReader = initialImage.getPixelReader();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = pixelReader.getColor(x, y);
                double r = color.getRed() * 255;
                double g = color.getGreen() * 255;
                double b = color.getBlue() * 255;
                if (r > 205 && b > 165 && g > 225){
                    pixelWriter.setColor(x, y, Color.WHITE);
                } else {
                    pixelWriter.setColor(x, y, Color.BLACK);
                }
            }
        }
        return writableImage;
    }

}
