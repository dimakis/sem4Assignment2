package org.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javafx.fxml.FXML;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PrimaryController {

    public ImageView imageView;
    public Image blackAndWhite;
    public double xCoord, yCoord;
    public Pane imagePane;

    public void initialize() {
        File file = new File("src/main/resources/org/example/romeMap.jpg");
        Image im = new Image(String.valueOf(file.toURI()), 920, 760, false, false);
        blackAndWhite = setBlackWhite(im);
        imageView.setImage(blackAndWhite);
        selectWaypoint();

    }

    // takes in an image, returns a black and white writable image using RGB values in image, optimised for maps.
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
                if (r > 205 && b > 165 && g > 225) {
                    pixelWriter.setColor(x, y, Color.WHITE);
                } else {
                    pixelWriter.setColor(x, y, Color.BLACK);
                }
            }
        }
        return writableImage;
    }

    public void selectWaypoint()    {
        imagePane.setOnMouseClicked(e -> {
            imageView.toFront();
            xCoord = e.getX() - 7;
            yCoord = e.getY() -7;
            imagePane.toFront();
            Circle circle = new Circle();
            circle.setCenterX(xCoord);
            circle.setCenterY(yCoord);
            circle.setRadius(6);
            circle.setFill(Color.RED);
           imagePane.getChildren().add(circle);
           circle.relocate(xCoord,yCoord);
           System.out.println("xCoord: " + xCoord + ", yCoord: " + yCoord);
        });
    }

}
