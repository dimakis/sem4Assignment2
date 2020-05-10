package org.example;

import java.io.*;
import java.util.*;


import graphTraversal.CostedPath;
import graphTraversal.Dijkstras;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import utils.GraphLinkAL;
import utils.GraphNodeAL;
import utils.Landmark;

import static org.example.DataManager.landmarks;

public class PrimaryController {

    public ImageView imageView;
    public WritableImage blackAndWhite;
    public double xCoord, yCoord;
    public Pane imagePane;
    public Button btn1;
    public GraphNodeAL start, dest;
    public boolean startNode = true;
    public int[] graphArray;
    public GraphNodeAL gn = new GraphNodeAL("", 0, 0);
    public TextField landmarkName;
    public ComboBox selectStart, selectEnd;
    public Pane landmarkPane;
    public Button djikstrasBtn;

    public void initialize() {
        DataManager.createLandmarkList();
        File file = new File("src/main/resources/org/example/romeMap.jpg");
        try {
            DataManager.loadFromCSV("src/main/resources/org/example/landmarks.csv", landmarks);
        } catch (Exception e) {
            System.out.println("Load Failed");
        }
        Image im = new Image(String.valueOf(file.toURI()), 900, 742, false, false);
        blackAndWhite = setBlackWhite(im);
        populateComboBox();
//        imageView.setImage(im);
        imageView.setImage(blackAndWhite);
        selectWaypoint();
        graphArray = createGraphArray(blackAndWhite);
        updateLandmarks();
        runDijkstras();
//        findRouteDijkstras();
//        example();
//        bfs();
    }

    public void example() {
        GraphNodeAL<Landmark> col = new GraphNodeAL(new Landmark(653,602,"Collesuem"));
        GraphNodeAL<Landmark> gio = new GraphNodeAL(new Landmark("Giovanni"));
        GraphNodeAL<Landmark> piaV = new GraphNodeAL(new Landmark("Piazza Venezia"));
        GraphNodeAL<Landmark> mar = new GraphNodeAL(new Landmark("Maria"));
        GraphNodeAL<Landmark> tre = new GraphNodeAL(new Landmark(550, 414, "Trevi"));
        col.connectToNodeUndirected(col,gio, 500);
        col.connectToNodeUndirected(col,piaV, 150);
        col.connectToNodeUndirected(col,mar, 200);
        gio.connectToNodeUndirected(gio,mar, 600);
        piaV.connectToNodeUndirected(piaV,tre, 250);
        mar.connectToNodeUndirected(mar,tre, 300);
        System.out.println("The cheapest path");
        System.out.println("using Dijkstra's algorithm:");
        System.out.println("-------------------------------------");
        CostedPath cpa = Dijkstras.findCheapestPathDijkstra(col, tre);
        for (GraphNodeAL<?> n : cpa.pathList)
            System.out.println(n.data);
        System.out.println("\nThe total path cost is: " + cpa.pathCost);
    }

    public void populateComboBox() {
        selectStart.getItems().addAll(landmarks);
        selectEnd.getItems().addAll(landmarks);
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

//    public void setUpExample() {
//        Landmark lk = new Landmark(startNode)
//        GraphNodeAL<Landmark> col = new GraphNodeAL<>()
//    }

    public void findRouteDijkstras(GraphNodeAL node) {
        if (startNode) {
//            start = selectWaypoint();
            start = node;
            System.out.println("Start");
            startNode = false;
        } else {
//            dest = selectWaypoint();
            System.out.println("Dest");
            dest = node;
            node.connectToNodeDirected(start,dest, 800);
            dest.data = "Colleseum";
            startNode = true;
            CostedPath costedPath = Dijkstras.findCheapestPathDijkstra(start, dest.data);
            System.out.println("Hello," + costedPath);
        }
//        CostedPath costedPath = Dijkstras.findCheapestPathDijkstra(start, dest);
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
                if (color.equals(Color.WHITE)) graphArr[currentPix] = 0;
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
        landmarkPane.setOnMouseClicked(e -> {
//            imageView.toFront();
            xCoord = e.getX() - 7;
            yCoord = e.getY() - 7;
//            imagePane.toFront();
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
//            gn = (new GraphNodeAL("", xCoord, yCoord));
//            findRouteDijkstras(gn);
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


    public void setStart(ActionEvent actionEvent) {

        //manually selecting start point
    }

    public void setEnd(ActionEvent actionEvent) {
        //manually selecting destination
    }

    public void setLandmark(ActionEvent actionEvent) {

//        Icon ico = new Image("src/main/resources/org/example/landmarkIcon.png");
        Landmark landmark = new Landmark(xCoord, yCoord, landmarkName.getText());
//        GraphNodeAL<Landmark> lmark = new GraphNodeAL<Landmark>(landmark);
        landmarks.add(landmark);
        Circle landmarkCircle = new Circle();
        landmarkCircle.setFill(Color.PURPLE);
        landmarkCircle.setCenterX(xCoord);
        landmarkCircle.setCenterY(yCoord);
        imagePane.getChildren().add(landmarkCircle);
        try {
//            updateLandmarks();
            DataManager.saveArraylistToCSV("src/main/resources/org/example/landmarks.csv", landmarks);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Save Failure");
        }
        //manually selecting landmark location
    }
    public void runDijkstras()  {
        djikstrasBtn.setOnAction(e -> {
            GraphNodeAL stNode = new GraphNodeAL((Landmark) selectStart.getSelectionModel().getSelectedItem());
//            stNode.setData(selectStart.getSelectionModel().getSelectedItem());
            GraphNodeAL edNode = new GraphNodeAL((Landmark) selectEnd.getSelectionModel().getSelectedItem());
            stNode.connectToNodeUndirected(stNode,edNode, 500);
            CostedPath cp = Dijkstras.findCheapestPathDijkstra(stNode,edNode);
            System.out.println(cp);
//            Dijkstras.findCheapestPathDijkstra((GraphNodeAL<?>) selectStart.getSelectionModel().getSelectedItem(),selectEnd.getSelectionModel().getSelectedItem());
        });
    }

    public void updateLandmarks() {
        landmarkPane.getChildren().clear();
        if (landmarks.size() > 0) {
            for (Object landmark : landmarks) {
                Circle circle = new Circle();
                System.out.println(landmark);
                circle.setCenterX(((Landmark) landmark).x + 6);
                circle.setCenterY(((Landmark) landmark).y + 6);
                circle.setRadius(6);
                circle.setFill(Color.ORANGE);
                landmarkPane.getChildren().add(circle);
            }
        }
    }
}
