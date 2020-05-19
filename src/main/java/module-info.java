module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencsv;
    requires junit;
    requires javafx.swing;
    requires javafx.base;

    opens org.example to javafx.fxml;
    exports org.example;
    exports graphTraversal;

}