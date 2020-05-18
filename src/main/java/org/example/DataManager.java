package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import utils.GraphNodeAL;
import utils.Landmark;

import java.io.*;
import java.util.*;


public class DataManager {
    public static ObservableList<Landmark> landmarks;
    public static ArrayList<GraphNodeAL> graphlist;
    public static ArrayList<GraphNodeAL> waypoints;
    public static ArrayList<GraphNodeAL> avoids;

    public static void createLandmarkList() {
        landmarks = FXCollections.observableArrayList();
    }

    public static void saveLandmarkList() {

    }

    public static void createGraphList() {
        graphlist = new ArrayList<>();
        waypoints = new ArrayList<>();
        avoids = new ArrayList<>();
    }

public static void save() {
    try {
        FileOutputStream fileOut = new FileOutputStream("src/main/resources/org/example/graphlist.txt");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(graphlist);
        out.close();
        fileOut.close();
    } catch( Exception e) {
e.printStackTrace();
    }
}

public static void load() {
    try {
        FileInputStream fileIn = new FileInputStream("src/main/resources/org/example/graphlist.txt");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        graphlist.addAll((ArrayList)in.readObject());
        fileIn.close();
        in.close();
    } catch (Exception e) {
        System.out.println("Load Failure");
    }
}

}
