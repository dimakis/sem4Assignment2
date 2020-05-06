package org.example;

import java.io.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import utils.GraphNodeAL;

public class PrimaryController {

    public ImageView imageView;
    public WritableImage blackAndWhite;
    public double xCoord, yCoord;
    public Pane imagePane;
    public Button btn1;
    public GraphNodeAL start, dest;
    public boolean startTrue = false;
    public int[] graphArray;
//    public XSream xSream = new XStream();

    public void initialize() {
        File file = new File("src/main/resources/org/example/romeMap.jpg");
        Image im = new Image(String.valueOf(file.toURI()), 920, 760, false, false);
        blackAndWhite = setBlackWhite(im);
//        imageView.setImage(im);
        imageView.setImage(blackAndWhite);
        selectWaypoint();
        graphArray = createGraphArray(blackAndWhite);
//        bfs();
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

    public int[] createGraphArray(WritableImage im) {
        double width = im.getWidth(), height = im.getHeight();
        PixelReader pixelReader = im.getPixelReader();
        PixelWriter pixelWriter = im.getPixelWriter();
        int[] graphArr = new int[(int) width * (int) height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int currentPix = (y * (int) width + x);
                Color color = pixelReader.getColor(x, y);
                if (color.equals(Color.WHITE)) graphArr[currentPix] = currentPix;
                else graphArr[currentPix] = -1;
            }
        }
//        for (int i = 0; i < graphArr.length - 1; i++) {
//            if (i % width == 0) {
//                System.out.println("\n");
//            }
//            System.out.print(graphArr[i] + "");
//        }
        return graphArr;
    }

    public void selectWaypoint() {
        imagePane.setOnMouseClicked(e -> {
            imageView.toFront();
            xCoord = e.getX() - 7;
            yCoord = e.getY() - 7;
            imagePane.toFront();
//            GraphNodeAL node = new GraphNodeAL(null, xCoord, yCoord);
//            startTrue = true;
//            assignStartDestNode(node);
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
//
//    public void assignStartDestNode(GraphNodeAL node) {
//        if (!startTrue) start = node;
//        else {
//            dest = node;
//            start.data = node;
//        }
//    }
//
//    public void bfs() {
//        btn1.setOnAction(e -> {
//            BreadthFirstSearch.findPathBreadthFirst(start, dest);
//
//        });
//    }

    public void removeChildrenFromImagePane() {
        imagePane.getChildren().clear();
    }

    public void load() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectInputStream is = xstream.createObjectInputStream(new FileReader("nodes.xml"));
//        products = (ArrayList<Product>) is.readObject();
        is.close();
    }

    public void save() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter("nodes.xml"));
//        out.writeObject(products);
        out.close();
    }
}
