package org.example;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.LongAccumulator;


import graphTraversal.BreadthFirstSearch;
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
import javafx.scene.shape.Line;
import utils.GraphLinkAL;
import utils.GraphNodeAL;
import utils.Landmark;

import static graphTraversal.Dijkstras.findCheapestPathDijkstra;
import static org.example.DataManager.*;

public class PrimaryController {

    public ImageView imageView;
    public WritableImage blackAndWhite;
    public double xCoord, yCoord;
    public Pane imagePane;
    public Button btn1;
    public GraphNodeAL start, dest;
    public boolean startNode = true;
    public int[] graphArray;
    public TextField landmarkName;
    public ComboBox selectStart, selectEnd, includeComboBox, avoidComboBox;
    public Pane landmarkPane;
    public Button dijkstrasBtn;
    public Button addLandmarkToDB_btn;
    public Button selectArea_btn;
    public RadioButton selectArea_radioBtn;
    public TextField textField_x;
    public TextField textField_y;
    public TextArea enrouteListTextArea;
    public TextArea avoidListTextArea;
    public ComboBox selectStartAddCost;
    public ComboBox selectDestCost;
    public TextField pathCostTextField;
    public Button addLandmarkCOST_ToDB_btn1;
    public Button bfs_btn;
    public Button deleteLandmarkToDB_btn1;
    public ComboBox deleteLandmarkCombo;
    public Button addWaypoint_btn;
    public Button addAvoidNode_btn;
    public RadioButton pointerForDest;
    public RadioButton pointerForStart;
    ArrayList<Integer> bfsList;

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
        imageView.setImage(im);
//        imageView.setImage(blackAndWhite);
        selectWaypoint();
//        graphArray = createGraphArray(blackAndWhite);
        if (landmarks.size() == 0) DataManager.loadFromCSV("src/main/resources/org/example/landmarks.csv", landmarks);
//        runDijkstras();
        addLandmarkToDatabase();
        DataManager.createGraphList();
//        findRouteDijkstras();
//        example();
        DataManager.load();
        populateComboBox();
        updateLandmarks();
        addCostToLandmarkLinks();
        findRouteDijkstras();
        deleteLandmark();
        addWayPoint();
        setGraphArray();
        breadthFirstSearch();
    }


    public void populateComboBox() {
        selectEnd.getItems().clear();
        selectStart.getItems().clear();
        selectDestCost.getItems().clear();
        selectStart.getSelectionModel().clearSelection();
        deleteLandmarkCombo.getItems().clear();
        includeComboBox.getItems().clear();
        avoidComboBox.getItems().clear();
        selectStartAddCost.getItems().clear();

        selectStart.getItems().addAll(graphlist);
        selectEnd.getItems().addAll(graphlist);
        selectDestCost.getItems().addAll(graphlist);
        selectStartAddCost.getItems().addAll(graphlist);
        deleteLandmarkCombo.getItems().addAll(graphlist);
        includeComboBox.getItems().addAll(graphlist);
        avoidComboBox.getItems().addAll(graphlist);
        enrouteListTextArea.setText(waypoints.toString());

    }

    public void addCostToLandmarkLinks() {
        addLandmarkCOST_ToDB_btn1.setOnAction(e -> {
            GraphNodeAL<Landmark> strt = (GraphNodeAL) selectStartAddCost.getSelectionModel().getSelectedItem();
            GraphNodeAL<Landmark> end = (GraphNodeAL) selectDestCost.getSelectionModel().getSelectedItem();
            strt.connectToNodeUndirected(strt, end, Integer.parseInt(pathCostTextField.getText()));
            save();
//            for (int i = 0; i < strt.getAdjList().size(); i++) {
//                System.out.println("Path cost: " + strt.getAdjList().get(i).cost);
//            }
        });
    }

    public void deleteLandmark() {
        deleteLandmarkToDB_btn1.setOnAction(e -> {
            GraphNodeAL toDel = (GraphNodeAL) deleteLandmarkCombo.getSelectionModel().getSelectedItem();

            System.out.println("before Del: " + graphlist.size());
            graphlist.remove(toDel);
            System.out.println("After Del: " + graphlist.size());
            updateLandmarks();
            populateComboBox();
            save();
        });
    }

    public void addWayPoint() {
        addWaypoint_btn.setOnAction(e -> {
            GraphNodeAL waypoint = (GraphNodeAL) includeComboBox.getSelectionModel().getSelectedItem();
            waypoints.add(waypoint);
            enrouteListTextArea.setText(waypoints.toString());
        });

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


    public void findRouteDijkstras() {
        dijkstrasBtn.setOnAction(e -> {
            CostedPath cp = new CostedPath();
            GraphNodeAL strt = (GraphNodeAL) selectStart.getSelectionModel().getSelectedItem();
            GraphNodeAL end = (GraphNodeAL) selectEnd.getSelectionModel().getSelectedItem();
            waypoints.add(0, strt);
            CostedPath tempCp = null;
            waypoints.add(end);
//            if (!avoids.isEmpty())  {
//                for (int i = 0; i < waypoints.size()-1;i++)    {
//                    for (int j = 0; j < avoids.size()-1;j++) {
//                        if(waypoints.get(i).data.equals(avoids.get(j)))
//                            waypoints.remove(i);
//                    }
//                }
//            }
            System.out.println("waypoints: " + waypoints.toString());
            for (int i = 0; i < waypoints.size() - 1; i++) {
                tempCp = findCheapestPathDijkstra(waypoints.get(i), waypoints.get(i + 1).data);
                cp.pathCost += tempCp.getPathCost();
            }
            System.out.println("Hello," + cp.pathCost);
            waypoints.clear();
        });
    }

    public void setGraphArray() {
        graphArray = createGraphArray(blackAndWhite);
    }


    public ArrayList<Integer> breadthFirstSearch() {
        bfsList = new ArrayList<>();
//        int[] graphArr = graphArray;
//        GraphNodeAL strt = start;
//        GraphNodeAL dst = dest;
        bfs_btn.setOnAction(e -> {
            int width = (int) imageView.getFitWidth();
            if (pointerForStart.isSelected()) {
                start.x = xCoord;
                start.y = yCoord;
            } else
                start = (GraphNodeAL) selectStart.getSelectionModel().getSelectedItem();
            if (pointerForDest.isSelected()) {
                dest.x = xCoord;
                dest.y = yCoord;
            } else
                dest = (GraphNodeAL) selectEnd.getSelectionModel().getSelectedItem();
            int[] graphArr =  createGraphArray(blackAndWhite); //graphArray.clone();
            bfsList = BreadthFirstSearch.bfs(start, dest, width, graphArr);
        });
        for (Integer node : bfsList
        ) {
            System.out.println("bfsList: " + node.intValue());
        }
        return bfsList;
    }


//            ArrayList<Integer> agenda = new ArrayList<>();
////            start.setNodeValue(1);
//            int destIndex = (int) (dest.y * (int) imageView.getImage().getWidth() + dest.x);
//            int startIndex = (int) (start.y * (int) imageView.getImage().getWidth() + start.x);
//            agenda.add(startIndex);
//            graphArray[startIndex] = 1;
//            //            agenda.add(start.x * start.y);
//            int current = agenda.remove(0);
//            int v = graphArray[current];    // not sure if it goes here
//
//            ArrayList<Integer> newPath;
//            if (graphArray[current] == destIndex) {
//                    newPath = new ArrayList<>();
//                    int cn = graphArray[destIndex];
//                    int totalDistance = graphArray[destIndex];
//                    v = graphArray[current];    // not sure if it goes here
//                    newPath.add(cn);
//                    if (cn == startIndex)
//                        return newPath;
//                    else{
//
//                }
//
//            } else {
//                do {
//
//                    v = graphArray[current];    // not sure if it goes here
//                    if (graphArray[current + 1] == 0) {
//                        graphArray[current + 1] = v + 1;
//                        agenda.add(current + 1);
//                    }
//                    if (graphArray[current - 1] == 0) {
//                        graphArray[current - 1] = v + 1;
//                        agenda.add(current - 1);
//                    }
//                    if (graphArray[(int) (current - imageView.getImage().getWidth())] == 0) {
//                        graphArray[current - width] = v + 1;
//                        agenda.add(current - width);
//                    }
//                    if (graphArray[(int) (current + imageView.getImage().getWidth())] == 0) {
//                        graphArray[current + width] = v + 1;
//                        agenda.add(current + width);
//                    }
//                } while (graphArray[current] != destIndex);
//
//            }
//            List bfs_list;


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
            imagePane.getChildren().clear();
//            imageView.toFront();
            xCoord = e.getX() + 6;
            yCoord = e.getY() + 12;
//            imagePane.toFront();
//            GraphNodeAL node = new GraphNodeAL(null, xCoord, yCoord);
//            startTrue = true;
//            assignStartDestNode(node);
            Circle circle = new Circle();
            circle.setCenterX(xCoord);
            circle.setCenterY(yCoord);
            circle.setRadius(5);
            circle.setFill(Color.RED);
            imagePane.getChildren().add(circle);
            circle.relocate(xCoord, yCoord);
            System.out.println("xCoord: " + xCoord + ", yCoord: " + yCoord);
//            gn = (new GraphNodeAL("", xCoord, yCoord));
//            findRouteDijkstras(gn);
        });
    }


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

    public void addLandmarkToDatabase() {
        addLandmarkToDB_btn.setOnAction(e -> {
            double x, y;
            if (selectArea_radioBtn.isSelected()) {
                x = xCoord;
                y = yCoord;
            } else {
                x = Double.parseDouble(textField_x.getText());
                y = Double.parseDouble(textField_y.getText());
            }
            GraphNodeAL gn = new GraphNodeAL(new Landmark(x, y, landmarkName.getText()));
            graphlist.add(gn);
            save();
            updateLandmarks();
            populateComboBox();
        });
    }

    public void runDijkstras() {
        dijkstrasBtn.setOnAction(e -> {
            GraphNodeAL stNode = new GraphNodeAL((Landmark) selectStart.getSelectionModel().getSelectedItem());
//            stNode.setData(selectStart.getSelectionModel().getSelectedItem());
            GraphNodeAL edNode = new GraphNodeAL((Landmark) selectEnd.getSelectionModel().getSelectedItem());
//            stNode.connectToNodeUndirected(stNode,edNode, 500);
            GraphNodeAL<Landmark> tre = new GraphNodeAL(new Landmark(550, 414, "Trevi"));
            GraphNodeAL<Landmark> mar = new GraphNodeAL(new Landmark(600, 300, "Maria"));
            GraphNodeAL<Landmark> jak = new GraphNodeAL(new Landmark(230, 450, "Jack"));
            GraphNodeAL<Landmark> pan = new GraphNodeAL(new Landmark(655, 256, "Pantheon"));
            stNode.connectToNodeUndirected(stNode, mar, 500);
            mar.connectToNodeUndirected(mar, tre, 800);
            mar.connectToNodeUndirected(mar, jak, 1200);
            tre.connectToNodeUndirected(tre, pan, 800);
            pan.connectToNodeUndirected(pan, edNode, 500);
            landmarks.add(tre.data);
            landmarks.add(mar.data);
            landmarks.add(pan.data);
            landmarks.add(jak.data);

            saveArraylistToCSV("src/main/resources/org/example/landmarks.csv", landmarks);
            CostedPath cp = findCheapestPathDijkstra(stNode, edNode.data);
            System.out.println("cp cost: " + cp.pathCost + ", cp list: " + cp.pathList);
//            Dijkstras.findCheapestPathDijkstra((GraphNodeAL<?>) selectStart.getSelectionModel().getSelectedItem(),selectdEnd.getSelectionModel().getSelectedItem());
            Line line = new Line();
            line.setStroke(Color.RED);
//            line.setStartX(stNode.x);
//            line.setStartY(stNode.y);
//            line.setEndX(edNode.x);
//            line.setEndY(edNode.y);
            landmarkPane.getChildren().add(line);
        });
    }


    public void updateLandmarks() {
        landmarkPane.getChildren().clear();
        if (graphlist.size() > 0) {
            for (GraphNodeAL<Landmark> gn : graphlist) {
//                (Landmark) lm = gn.
                Circle circle = new Circle();
                System.out.println(gn.toString());
                circle.setCenterX(gn.data.getX());
                circle.setCenterY(gn.data.getY());
                circle.setRadius(6);
                circle.setFill(Color.ORANGE);
                landmarkPane.getChildren().add(circle);
            }
        }
    }

    public void example() {
        GraphNodeAL<Landmark> col = new GraphNodeAL(new Landmark(653, 602, "Collesuem"));
        GraphNodeAL<Landmark> gio = new GraphNodeAL(new Landmark("Giovanni"));
        GraphNodeAL<Landmark> piaV = new GraphNodeAL(new Landmark("Piazza Venezia"));
        GraphNodeAL<Landmark> mar = new GraphNodeAL(new Landmark("Maria"));
        GraphNodeAL<Landmark> tre = new GraphNodeAL(new Landmark(550, 414, "Trevi"));
        col.connectToNodeUndirected(col, gio, 500);
        col.connectToNodeUndirected(col, piaV, 150);
        col.connectToNodeUndirected(col, mar, 200);
        gio.connectToNodeUndirected(gio, mar, 600);
        piaV.connectToNodeUndirected(piaV, tre, 250);
        mar.connectToNodeUndirected(mar, tre, 300);
        graphlist.add(tre);
        graphlist.add(col);
        graphlist.add(piaV);
        graphlist.add(mar);
        graphlist.add(gio);
        System.out.println("The cheapest path");
        System.out.println("using Dijkstra's algorithm:");
        System.out.println("-------------------------------------");
        CostedPath cpa = findCheapestPathDijkstra(col, tre.data);
        for (GraphNodeAL<?> n : cpa.pathList)
            System.out.println(n.data);
        System.out.println("\nThe total path cost is: " + cpa.pathCost);
        DataManager.save();
        try {
            DataManager.load();
        } catch (Exception e) {
            System.out.println("Load Error");
        }
    }

}
