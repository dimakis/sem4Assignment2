module org.example {
    requires javafx.controls;
    requires javafx.fxml;
//    requires xstream;
    requires opencsv;

    opens org.example to javafx.fxml;
    exports org.example;
}