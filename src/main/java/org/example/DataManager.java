package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utils.Landmark;

import java.io.*;
import java.util.*;


public class DataManager {
    public static ObservableList landmarks;

    public static void createLandmarkList(){
        landmarks = FXCollections.observableArrayList();
    }
    public static void saveLandmarkList() {

    }

    public static void saveArraylistToCSV(String pathname, ObservableList list) {
        File file = new File(pathname);

        try {
            FileWriter out = new FileWriter(file);
            CSVWriter writer = new CSVWriter(out);
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"XCoord", "YCoord", "Name",});

            for (Object obj: list) {
                Landmark lk = (Landmark) obj;
                data.add(new String[]{String.valueOf(lk.x), String.valueOf(lk.y),lk.landmarkName});
            }
            writer.writeAll(data);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CSV Save Failure");
        }

    }

    public static void loadFromCSV(String file, List list) throws IOException, CsvValidationException {
        try {
            FileReader reader = new FileReader(file);
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            String[] next;
            while ((next = csvReader.readNext()) != null) {
                for (String obj : next) {
                    landmarks.add(obj);
                    System.out.println(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CSV Load Failure");
        }
    }
}
