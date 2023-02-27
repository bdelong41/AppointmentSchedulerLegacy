package com.example.scheduler.viewsAndControllers;
import com.example.scheduler.data.AppointmentDAOImpl;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

import com.example.scheduler.models.*;

/**
 * Main entrance to the application
 */
public class Main extends Application {
    private User usr;

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
}
