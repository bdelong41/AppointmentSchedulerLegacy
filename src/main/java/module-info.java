module com.example.scheduler {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    exports com.example.scheduler.models;
    opens com.example.scheduler.models to javafx.fxml;
    exports com.example.scheduler.data;
    opens com.example.scheduler.data to javafx.fxml;
    exports com.example.scheduler.viewsAndControllers;
    opens com.example.scheduler.viewsAndControllers to javafx.fxml;
}