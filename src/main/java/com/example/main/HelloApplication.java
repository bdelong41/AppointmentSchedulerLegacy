package com.example.main;

import com.example.main.controller.Logger;
import com.example.main.controller.Login;
import com.example.main.controller.Record;
import com.example.main.dao.AppointmentDAOImpl;
import com.example.main.dao.Data;
import com.example.main.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static User usr;
    /**
     * Entrance function.
     * Launches a login screen and validates a users credentials.
     * If the user has logged in then a record window is created.
     */
    @Override
    public void start(Stage stage) throws IOException
    {
        Logger log = new Logger();
        log.createFile();
        AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
        Login lgn = new Login();
        usr = lgn.beginStart(appointmentDAO.getAll());
        if(usr != null)
        {
                Record rec = new Record();
                rec.beginStart(appointmentDAO, usr);
        }

    }

    public static void main(String[] args) {
        launch();
    }
}