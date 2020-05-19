package org.example;

import java.io.*;
import java.util.*;


import graphTraversal.BreadthFirstSearch;
import graphTraversal.CostedPath;
import graphTraversal.DepthFirstSearch;
import graphTraversal.EuclidianDistance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import utils.GraphLinkAL;
import utils.GraphNodeAL;
import utils.Landmark;

import static graphTraversal.Dijkstras.findCheapestPathDijkstra;
import static org.example.DataManager.*;

public class PrimaryController {

    public ImageView imageView;
    public WritableImage blackAndWhite;
    public double xCoord, yCoord, xCoordStart, yCoordStart, xCoordDstn, yCoordDstn;
    public Pane imagePane, labelPane, landmarkPane;
    public boolean startPoint;
    public GraphNodeAL start, dest, manStart, manDest;
    public int[] graphArray;
    public ComboBox selectStart, selectEnd, includeComboBox, avoidComboBox, selectDestCost, selectStartAddCost, deleteLandmarkCombo;
    public Button dijkstrasBtn, addLandmarkToDB_btn, bfs_btn, addLandmarkCOST_ToDB_btn1, addWaypoint_btn, addAvoidNode_btn, findHistoric, deleteLandmarkToDB_btn1, clearSelection, dfsBtn;
    public RadioButton selectArea_radioBtn, historic, toggleLabels_btn, pointerForDest, pointerForStart, selectEndPoint_rBtn, selectStartPoint_rBtn;
    public TextField textField_x, textField_y, pathCostTextField, landmarkName;
    public TextArea enrouteListTextArea, avoidListTextArea;
    public ArrayList<Integer> bfsList, dfsList, choiceList;
    public Button euclideanSearchBtn;
    public ComboBox searchModifier;
    public TextField currentCost;
    public Button showCostBtn;

    public void initialize() {
        DataManager.createLandmarkList();
        File file = new File("src/main/resources/org/example/romeMap.jpg");

        Image im = new Image(String.valueOf(file.toURI()), 900, 742, false, false);
        blackAndWhite = setBlackWhite(im);
        imageView.setImage(im);
        selectWaypoint();
        addLandmarkToDatabase();
        DataManager.createGraphList();
        try {
            DataManager.load();
        } catch (Exception e) {
            System.out.println("Load Failed");
        }
        populateComboBox();
        updateLandmarks();
        addCostToLandmarkLinks();
        findRouteDijkstras();
        deleteLandmark();
        addWayPoint();
        setGraphArray();
        breadthFirstSearch();
        addAvoid();
        displayLabels();
        startPoint = true;
        setClearSelection();
        depthFirstSearch();
        addOptions();
        optionsSearch();
        displayCost();
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
        avoidListTextArea.setText(avoids.toString());

    }

    public Paint randomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }

    public void colorPath(ArrayList<Integer> arrayList, int index) {
        Paint paint;
        if (index == 0)
            paint = Color.RED;
        else if (index == 1)
            paint = Color.BLUE;
        else
            paint = randomColor();
        for (int path : arrayList) {
            double x = path % imageView.getImage().getWidth();
            double y = path / imageView.getImage().getWidth() + 1;
            Circle circle = new Circle();
            circle.setLayoutX(x);
            circle.setLayoutY(y);
            circle.setRadius(1);
            circle.setFill(paint);
            imagePane.getChildren().add(circle);
            arrayList = new ArrayList<>();
        }
    }

    public void addCostToLandmarkLinks() {
        addLandmarkCOST_ToDB_btn1.setOnAction(e -> {
            GraphNodeAL<Landmark> strt = (GraphNodeAL) selectStartAddCost.getSelectionModel().getSelectedItem();
            GraphNodeAL<Landmark> end = (GraphNodeAL) selectDestCost.getSelectionModel().getSelectedItem();
            strt.connectToNodeUndirected(strt, end, Integer.parseInt(pathCostTextField.getText()));
            getCost();
            save();
        });
    }

    public void displayCost() {
        showCostBtn.setOnAction(e -> {
            getCost();
        });
    }

    public void getCost() {
        try {
            GraphNodeAL<Landmark> strt = (GraphNodeAL) selectStartAddCost.getSelectionModel().getSelectedItem();
            GraphNodeAL<Landmark> end = (GraphNodeAL) selectDestCost.getSelectionModel().getSelectedItem();
            for (GraphLinkAL link : strt.adjList) {
                if (link.startNode.equals(strt) && link.destNode.equals(end))
                    currentCost.setText(String.valueOf(link.cost));
                else
                    currentCost.setText("No costs associated yet");
            }
        } catch (Exception exc) {
            currentCost.setText("No costs associated yet");
        }
    }


    public void displayLabels() {
        toggleLabels_btn.setOnAction(e -> {
            if (toggleLabels_btn.isSelected()) {
                for (Object node : graphlist) {
                    Label landmarkName = new Label();
                    landmarkName.setText(((Landmark) (((GraphNodeAL) node).data)).landmarkName);
                    landmarkName.setLayoutX(((Landmark) (((GraphNodeAL) node).data)).getX());
                    landmarkName.setLayoutY(((Landmark) (((GraphNodeAL) node).data)).getY());
                    labelPane.getChildren().add(landmarkName);
                }
            } else
                labelPane.getChildren().clear();
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
                if (x == 0 || x == width - 1)
                    pixelWriter.setColor(x, y, Color.BLACK);
                if (y == 0 || y == height - 1)
                    pixelWriter.setColor(x, y, Color.BLACK);
            }
        }
        return writableImage;
    }

    // uses dijkstras to find route between two nodes that are connected with weights and then uses BFS to draw lines between them
    public void findRouteDijkstras() {
        dijkstrasBtn.setOnAction(e -> {
            try {
                CostedPath cp = new CostedPath();
                ArrayList<Integer> dijkstraList = new ArrayList<>();
                GraphNodeAL strt = (GraphNodeAL) selectStart.getSelectionModel().getSelectedItem();
                GraphNodeAL end = (GraphNodeAL) selectEnd.getSelectionModel().getSelectedItem();
                waypoints.add(0, strt);
                CostedPath tempCp = null;
                waypoints.add(end);
                System.out.println("waypoints: " + waypoints.toString());
                for (int i = 0; i < waypoints.size() - 1; i++) {
                    tempCp = findCheapestPathDijkstra(waypoints.get(i), waypoints.get(i + 1).data, avoids);
                    cp.pathCost += tempCp.getPathCost();
                    for (int j = 0; j < tempCp.pathList.size(); j++) {
                        cp.pathList.add(tempCp.getPathList().get(j));
                    }
                }
                System.out.println("Pathcost: " + cp.pathCost + "\nPathList: " + cp.pathList.toString());
                for (int i = 0; i < cp.pathList.size() - 1; i++) {
                    int[] arr = createGraphArray(blackAndWhite);
                    if (!(cp.pathList.get(i).equals(cp.pathList.get(i + 1))))
                        dijkstraList.addAll(BreadthFirstSearch.bfs(cp.pathList.get(i), cp.pathList.get(i + 1), (int) imageView.getImage().getWidth(), arr));
                }
                System.out.println("Pathcost: " + cp.pathCost + "\nPathList: " + cp.pathList.toString());
                colorPath(dijkstraList, 2);
                waypoints.clear();
                avoids.clear();
            } catch (Exception excep) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No valid Start and Destination locations selected\nMay not have correct costs in database");
                alert.showAndWait();
            }
        });
    }


    public void setGraphArray() {
        graphArray = createGraphArray(blackAndWhite);
    }

    // this method selects the params for which to use with bfs and calls bfs
    public void breadthFirstSearch() {
        bfs_btn.setOnAction(e -> {
            int width = (int) imageView.getFitWidth();
            try {
                if (pointerForStart.isSelected()) {
                    start = new GraphNodeAL(xCoordStart, yCoordStart);
                    start.x = xCoordStart;
                    start.y = yCoordStart;
                } else
                    start = (GraphNodeAL) selectStart.getSelectionModel().getSelectedItem();
                if (pointerForDest.isSelected()) {
                    dest = new GraphNodeAL(xCoordDstn, yCoordDstn);
                    dest.x = xCoordDstn;
                    dest.y = yCoordDstn;
                } else
                    dest = (GraphNodeAL) selectEnd.getSelectionModel().getSelectedItem();
                int[] graphArr = createGraphArray(blackAndWhite);
                bfsList = BreadthFirstSearch.bfs(start, dest, width, graphArr);
                colorPath(bfsList, 0);
            } catch (Exception excep) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No valid Start and Destination locations selected");
                alert.showAndWait();
            }
        });
    }

    // this method selects the params for which to use with dfs and calls dfs
    public void depthFirstSearch() {
        dfsBtn.setOnAction(e -> {
            int width = (int) imageView.getFitWidth();
            try {
                if (pointerForStart.isSelected()) {
                    start = new GraphNodeAL(xCoordStart, yCoordStart);
                    start.x = xCoordStart;
                    start.y = yCoordStart;
                } else
                    start = (GraphNodeAL) selectStart.getSelectionModel().getSelectedItem();
                if (pointerForDest.isSelected()) {
                    dest = new GraphNodeAL(xCoordDstn, yCoordDstn);
                    dest.x = xCoordDstn;
                    dest.y = yCoordDstn;
                } else
                    dest = (GraphNodeAL) selectEnd.getSelectionModel().getSelectedItem();
                int[] graphArr = createGraphArray(blackAndWhite);
                dfsList = DepthFirstSearch.dfs(start, dest, width, graphArr);
                colorPath(dfsList, 1);
            } catch (Exception excep) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No valid Start and Destination locations selected");
                alert.showAndWait();
            }
        });
    }

    public void addOptions() {
        ObservableList options = FXCollections.observableArrayList();
        options.add("Euclidean");
        options.add("FeelingLucky");
        options.add("Dodgy Taxi");
        searchModifier.setItems(options);

    }

    // this method selects the params for which to use with euclidean distance
    public void optionsSearch() {
        euclideanSearchBtn.setOnAction(e -> {
            int width = (int) imageView.getFitWidth();
            try {
                if (pointerForStart.isSelected()) {
                    start = new GraphNodeAL(xCoordStart, yCoordStart);
                    start.x = xCoordStart;
                    start.y = yCoordStart;
                } else
                    start = (GraphNodeAL) selectStart.getSelectionModel().getSelectedItem();
                if (pointerForDest.isSelected()) {
                    dest = new GraphNodeAL(xCoordDstn, yCoordDstn);
                    dest.x = xCoordDstn;
                    dest.y = yCoordDstn;
                } else
                    dest = (GraphNodeAL) selectEnd.getSelectionModel().getSelectedItem();
                int[] graphArr = createGraphArray(blackAndWhite);
                String option = (String) searchModifier.getSelectionModel().getSelectedItem();
                choiceList = EuclidianDistance.euclideanSearch(start, dest, width, graphArr, option);
                colorPath(choiceList, 2);
            } catch (Exception excep) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No valid Start and Destination locations selected");
                alert.showAndWait();
            }
        });
    }

    // creates an array for use with bfs based on the input image
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

    public void checkPointSuitability(double x, double y, int selection) {
        if (graphArray[((int) (y * imageView.getImage().getWidth() + x))] == 0) {
            Circle circle = new Circle();
            circle.setLayoutX(x);
            circle.setLayoutY(y);
            circle.setRadius(6);
            if (selection == 3) {
                try {

                    imagePane.getChildren().removeIf(node -> node.getId().equals("Landmark"));
                } catch (Exception exc) {
                    System.out.println("No Landmarks to remove");
                }
                circle.setId("Landmark");
                circle.setFill(Color.PURPLE);
            } else if (selection == 1) {
                try {
                    imagePane.getChildren().removeIf(node -> node.getId().equals("Start"));
                } catch (Exception ex) {
                    System.out.println("No Start points to remove");
                }
                circle.setId("Start");
                circle.setFill(Color.GREEN);
            } else {
                try {
                    imagePane.getChildren().removeIf(node -> node.getId().equals("Destination"));
                } catch (Exception exc) {
                    System.out.println("No Destination points to remove");
                }
                circle.setId("Destination");
                circle.setFill(Color.RED);
            }

            imagePane.getChildren().add(circle);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Landmark Error");
            alert.setContentText("Please select a road next to location");
            alert.showAndWait();
        }
    }

    public void selectWaypoint() {
        int selectStart = 1;
        int selectDest = 2;
        int selectLandmk = 3;
        landmarkPane.setOnMouseClicked(e -> {
            if (selectStartPoint_rBtn.isSelected()) {
                xCoordStart = e.getX();
                yCoordStart = e.getY();
                checkPointSuitability(xCoordStart, yCoordStart, selectStart);
                selectStartPoint_rBtn.setSelected(false);
            } else if (selectEndPoint_rBtn.isSelected()) {
                xCoordDstn = e.getX();
                yCoordDstn = e.getY();
                checkPointSuitability(xCoordDstn, yCoordDstn, selectDest);
                selectEndPoint_rBtn.setSelected(false);
            } else {
                xCoord = e.getX();
                yCoord = e.getY();
                checkPointSuitability(xCoord, yCoord, selectLandmk);
            }
        });
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
            GraphNodeAL gn = new GraphNodeAL(new Landmark(x, y, landmarkName.getText(), historic.isSelected()));
            graphlist.add(gn);
            save();
            updateLandmarks();
            populateComboBox();
            landmarkName.setPromptText("Landmark Name");
            selectArea_radioBtn.setSelected(false);
            historic.setSelected(false);
        });
    }

    public void addAvoid() {
        addAvoidNode_btn.setOnAction(e -> {
            GraphNodeAL avoid = (GraphNodeAL) avoidComboBox.getSelectionModel().getSelectedItem();
            avoids.add(avoid);
            avoidListTextArea.setText(avoids.toString());
        });
    }


    public void updateLandmarks() {
        imagePane.getChildren().clear();
        landmarkPane.getChildren().clear();
        selectStart.getItems().clear();
        selectEnd.getItems().clear();
        deleteLandmarkCombo.getItems().clear();
        selectStartAddCost.getItems().clear();
        selectDestCost.getItems().clear();
        includeComboBox.getItems().clear();
        avoidComboBox.getItems().clear();

        enrouteListTextArea.setText(waypoints.toString());
        avoidListTextArea.setText(avoids.toString());

        for (Object node : graphlist) {
            Circle circle = new Circle();
            circle.setLayoutX(((Landmark) (((GraphNodeAL) node).data)).x);
            circle.setLayoutY(((Landmark) (((GraphNodeAL) node).data)).y);
            circle.setRadius(6);
            circle.setFill(Color.ORANGE);

            Tooltip tooltip = new Tooltip(((Landmark) (((GraphNodeAL) node).data)).landmarkName);
            Tooltip.install(circle, tooltip);

            landmarkPane.getChildren().add(circle);

            selectStart.getItems().add(node);
            selectEnd.getItems().add(node);
            deleteLandmarkCombo.getItems().add(node);
            selectStartAddCost.getItems().add(node);
            selectDestCost.getItems().add(node);
            includeComboBox.getItems().add(node);
            avoidComboBox.getItems().add(node);
        }
    }

    public void setClearSelection() {
        clearSelection.setOnAction(e -> {
            imagePane.getChildren().clear();
            waypoints.clear();
            avoids.clear();
            updateLandmarks();
            populateComboBox();
        });
    }
}
