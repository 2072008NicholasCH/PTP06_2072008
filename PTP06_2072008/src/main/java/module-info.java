module com.example.ptp04_2072008 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;

    exports com.example.ptp06_2072008;
    exports com.example.ptp06_2072008.model;
    opens com.example.ptp06_2072008.model to javafx.fxml;
    exports com.example.ptp06_2072008.dao;
    opens com.example.ptp06_2072008.dao to javafx.fxml;
    exports com.example.ptp06_2072008.controller;
    opens com.example.ptp06_2072008.controller to javafx.fxml;
    exports com.example.ptp06_2072008.util;
    opens com.example.ptp06_2072008.util to javafx.fxml;
}