package org.example;

import java.io.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
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
//    public XSream xSream = new XStream();

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

    public void selectWaypoint() {
        imagePane.setOnMouseClicked(e -> {
            imageView.toFront();
            xCoord = e.getX() - 7;
            yCoord = e.getY() - 7;
            imagePane.toFront();
            Circle circle = new Circle();
            circle.setCenterX(xCoord);
            circle.setCenterY(yCoord);
            circle.setRadius(6);
            circle.setFill(Color.RED);
            imagePane.getChildren().add(circle);
            circle.relocate(xCoord, yCoord);
            System.out.println("xCoord: " + xCoord + ", yCoord: " + yCoord);
        });
    }

    public void removeChildrenFromImagePane() {
        imagePane.getChildren().clear();
    }
    public void load() throws Exception
    {
        XStream xstream = new XStream();   //(new DomDriver());
        ObjectInputStream is = xstream.createObjectInputStream(new FileReader("nodes.xml"));
//        products = (ArrayList<Product>) is.readObject();
        is.close();
    }

    public void save() throws Exception
    {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter("nodes.xml"));
//        out.writeObject(products);
        out.close();
    }
}
