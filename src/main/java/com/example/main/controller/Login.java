package com.example.main.controller;
import com.example.main.dao.UserDAOImpl;
import com.example.main.model.Appointment;
import com.example.main.model.User;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import static java.lang.Math.abs;

/**
 * Controller for the login window.
 * Validates user credentials using the users table in the database.
 * Retrieves the user information once the correct credentials have been entered.
 * Uses Logger object to record successful/failed login attempts to a log.
 * Use beginStart() to launch the window.
 */
public class Login {

    @FXML
    private TextField usernameField;
    //Buttons
    @FXML
    private TextField passwordField;
    @FXML
    private TextField locationField;
    @FXML
    private Button login;

    //labels
    @FXML
    private Label updateLabel;
    @FXML
    private Label loginAttemptsLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label locationLabel;

    private static String invalidLogin = "Error Username/password is not valid";
    private static String pswdMessage = "Error Password cannot be blank";
    private static String usernameMessage = "Error Username cannot be blank";
    private static String title = "Schedule Login";

    private static String attemptsText = "Login attempts: ";
    private static List<Appointment> appointments;

    private UserDAOImpl userDao;

    private static User usr = null;

    private int loginAttemptsCounter = 0;

    private Logger logBuffer = new Logger();
    /**
     * Called when the Login windows is launched.
     */
    public void initialize() {
        userDao = new UserDAOImpl();
        loginAttemptsLabel.setVisible(false);
        ZoneId zone = ZoneId.systemDefault();
        locationField.setText(zone.toString());
        translateErrorMessages();
        hasUpcomingAppointment();
    }
    /**
     * Launches the Login window.
     * @param appointments list of appointments used to alert the user when an appointment is within fifteen minutes
     * in local time.
     * @return usr Returns the user information retrieved once the user successfully logged in. Returns False when the
     * user cancels or exits the window.
     */
    public User beginStart(List<Appointment> appointments) {

        //initializing members
        this.appointments = appointments;
        URL uri = getClass().getResource("/com/example/main/login.fxml");
        FXMLLoader loader = new FXMLLoader(uri);

        try {

            Parent root = loader.load();
            Stage stage = new Stage();
            Login scenePointer = loader.getController();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.usr;
    }

    /**
     * Connects to jdbc database and compares entered credentials with known users
     * triggers error dialog when the credentials are invalid.
     * validates user input for unfilled fields.
     */
    public void loginButton() {
        //compare credentials with existing users
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        LocalDateTime ldt = timestamp.toLocalDateTime();
        if(validate()) {
            usr = userDao.get(usernameField.getText(), passwordField.getText());
            if (usr == null) {
                genError(invalidLogin);
                loginAttemptsLabel.setVisible(true);
                loginAttemptsCounter += 1;
                loginAttemptsLabel.setText(attemptsText + loginAttemptsCounter);
                logBuffer.loginAttempt(usernameField.getText(), ldt);
            }
            else {
                logBuffer.loginSucessfull(usernameField.getText(), ldt);
                close();
            }

        }
    }
    /**
     * Allows the user to quit the login window.
     */
    public void close() {
        ((Stage) login.getScene().getWindow()).close();
    }

    /**
     * Validates the user entered information.
     * Checks if the input fields are blank and generates the appropriate error message.
     * @return True if the user input fields are not blank. False otherwise.
     */
    public boolean validate (){
        if(usernameField.getText().isEmpty())
        {
            passwordField.setStyle("");
            usernameField.setStyle("-fx-text-box-border: red");
            genError(usernameMessage);
        }
        else if(passwordField.getText().isEmpty())
        {
            usernameField.setStyle("");
            passwordField.setStyle("-fx-text-box-border: red");
            genError(pswdMessage);
        }
        else {
            usernameField.setStyle("");
            passwordField.setStyle("");
            return true;
        }
        return false;
    }

    /**
     * Generates error dialog.
     * @param msg Error message.
     */
    public void genError(String msg)
    {
        Error err = new Error();
        err.beginStart(msg);
    }

    /**
     * Checks for any appointments who start within 15 minutes of the users time.
     * Alerts the user when an appointment is within 15 minutes.
     * Translates the upcoming appointments message and error message to French based on the users display language.
     */
    public void hasUpcomingAppointment()
    {
        try {
            LocalDateTime now = LocalDateTime.now();
            for (Appointment app : appointments) {
                if (app.getStart().toLocalDate().equals(now.toLocalDate())) {

                    if (now.toLocalDate().equals(app.getStart().toLocalDate())) {
                        LocalTime startTime = app.getStart().toLocalTime();
                        if (abs(startTime.until(now, ChronoUnit.MINUTES)) <= 15) {
                            Error err = new Error();
                            String errorMsg = "";
                            String labelMsg = "";
                            //translating messages
                            if (Locale.getDefault().getDisplayLanguage().equals("fr") ||
                                    Locale.getDefault().getDisplayLanguage().equals("français")) {
                                Locale france = new Locale("fr", "FR");
                                ResourceBundle rb = ResourceBundle.getBundle("bundle/Lang", france);
                                errorMsg = rb.getString("Appointment") + " " + app.getTitle() + " " +
                                        rb.getString("within") + " " + rb.getString("fifteen") + " " +
                                        rb.getString("minutes");
                                labelMsg = rb.getString("Appointment") + ": " + app.getTitle() + ", " +
                                        rb.getString("ID") + ": " + app.getId() + ", " + rb.getString("Date") +
                                        ": " + app.getStartString() ;
                            } else {
                                errorMsg = "Appointment " + app.getTitle() + " within fifteen minutes";
                                labelMsg = "Upcoming Appointment " + app.getTitle() + ", ID: " + app.getId() +
                                        ", Date: " + app.getStartString();
                            }

                            err.beginStart(errorMsg);
                            updateLabel.setVisible(true);
                            updateLabel.setText(labelMsg);
                            break;
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Translates error messages, labels, and the window title to French.
     */
    public void translateErrorMessages(){
        try
        {
            if (Locale.getDefault().getDisplayLanguage().equals("français") ||
                    Locale.getDefault().getDisplayLanguage().equals("fr")) {
                Locale france = new Locale("fr", "FR");
                ResourceBundle rb = ResourceBundle.getBundle("bundle/Lang", france);

                invalidLogin = rb.getString("Error") + "! " + rb.getString("Username") + "/" +
                        rb.getString("Password") + " " + rb.getString("is") + " " +
                        rb.getString("not") + " " + rb.getString("valid");

                pswdMessage = rb.getString("Error") + "! " + rb.getString("Password") + " " +
                        rb.getString("cannot") + " " + rb.getString("be") + " " +
                        rb.getString("blank");

                usernameMessage = rb.getString("Error") + "! " + rb.getString("Username") + " " +
                        rb.getString("cannot") + " " + rb.getString("be") + " " +
                        rb.getString("blank");

                title = rb.getString("Schedule") + " " + rb.getString("Login");

                attemptsText = rb.getString("Login") + " " + rb.getString("attempts") + ":";

                usernameLabel.setText(rb.getString("Username"));
                passwordLabel.setText(rb.getString("Password"));
                locationLabel.setText(rb.getString("Location"));

                updateLabel.setText(rb.getString("No") + " " + rb.getString("upcoming") + " " +
                        rb.getString("appointments") + "!");

                login.setText(rb.getString("Login"));

                usernameField.setPromptText(rb.getString("username"));

                locationField.setPromptText(rb.getString("Your") + " " + rb.getString("location") + " " +
                        rb.getString("here"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
