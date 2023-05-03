module com.example.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.main to javafx.fxml;
    exports com.example.main;
    exports com.example.main.controller;
    opens com.example.main.controller to javafx.fxml;
    exports com.example.main.model;
    opens com.example.main.model to javafx.fxml;
}