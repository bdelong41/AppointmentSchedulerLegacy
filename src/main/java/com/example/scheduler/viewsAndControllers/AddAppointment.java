package com.example.scheduler.viewsAndControllers;

import com.example.scheduler.data.AppointmentDAOImpl;
import com.example.scheduler.data.ContactsDAOImpl;
import com.example.scheduler.data.interfaces.AppointmentDAO;
import com.example.scheduler.models.Appointment;
import com.example.scheduler.models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.*;
import java.util.*;

/**
 * Controller for the add appointment and update appointment windows.
 * Allows users to create and update appointments.
 * AddAppointment only operates on an existing Appointment object, meaning an Appointment object must be created
 * externally to this controller and provided through the beginStart() function.
 * Essentially this populates the Appointment data members or updates them.
 */
public class AddAppointment {
    //Text Fields
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField typeField;
    //disabled fields
    @FXML
    private TextField appointmentIDField;
    @FXML
    private TextField customerIDField;
    @FXML
    private TextField userIDField;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;

    //combo boxes
    @FXML
    private ComboBox<Contact> contactComboBox;

    //Date Picker
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;

    //Buttons
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    //error labels
    @FXML
    private Label contactsError;
    @FXML
    private Label startDateError;
    @FXML
    private Label endDateError;

    //ZoneId
    private final ZoneId currentZone = ZoneId.systemDefault();

    private final ZoneId newYork = ZoneId.of("America/New_York");

    public static Appointment appointment;
    private static Contact contact;
    private static ContactsDAOImpl contactsDAO = new ContactsDAOImpl();
    private static final LocalTime officeOpen = LocalTime.parse("08:00");

    private static final LocalTime officeClose = LocalTime.parse("22:00");
    private static LocalTime sTime;
    private static LocalTime eTime;

    private static ObservableList<String> startTimes = FXCollections.observableArrayList();
    private static ObservableList<String> endTimes = FXCollections.observableArrayList();
    private static ObservableList<Contact> contactsList = FXCollections.observableArrayList();

    /**
     * Initializes the contact names to associate with a new or selected appointment .
     * Sets error labels to hidden.
     */
    public void initialize()
    {
        contactsList.clear();
        contactsList.addAll(contactsDAO.getAll());
        contactComboBox.setItems(contactsList);
        //error labels
        contactsError.setVisible(false);
        startDateError.setVisible(false);
        endDateError.setVisible(false);
        loadAppointment();
    }
    /**
     * Starts the window and takes as input the appointment being created or updated.
     * Overrides the windows default exit to cancel().
     *
     * Uses Lambda expression located at {@link #beginStart(Appointment, String)}. Sets stageCloseRequest to
     * execute {@link #cancel()} function instead of using the default javaFX closing method.
     *
     * @param reference holds a reference to the newly created, or existing appointment
     * @param title sets the stage title of the window. Expected values are "Create appointment" or update appointment
     * @return appointment Point where the update Appointment object is returned.
     */
    public Appointment beginStart(Appointment reference, String title)
    {
        //clearing contactsList
        this.appointment = reference;
        URL url = getClass().getResource("/com/example/scheduler/viewsAndControllers/addAppointment.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            if(reference == null) {
                genError("Failed to load Appointment Data");
                return null;
            }
            //overriding the default closing method to the cancel function
            stage.setOnCloseRequest(e->{
                e.consume();
                //run cancel function
                ((AddAppointment) loader.getController()).cancel();
            });
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error Failed to load addAppointment.fxml");
            e.printStackTrace();
            return null;
        }
        return ((AddAppointment) loader.getController()).appointment;
    }

    /**
     * Populates the fields using the existing data within appointment data member.
     * This function checks if any data exists within appointment's members before accessing it.
     */
    private void loadAppointment(){
        appointmentIDField.setText(appointment.getId().toString());
        userIDField.setText(appointment.getUsrID().toString());
        customerIDField.setText(appointment.getCustID().toString());
        if(appointment.getTitle() != null){
            titleField.setText(appointment.getTitle());
        }
        if(appointment.getDescription() != null){
            descriptionField.setText(appointment.getDescription());
        }
        if(appointment.getLocation() != null){
            locationField.setText(appointment.getLocation());
        }
        if(appointment.getType() != null){
            typeField.setText(appointment.getType());
        }
        if(appointment.getContactID() != null){
            //checking if the contact exists in the database
            //if the contact doesn't exist
            // the contact is either not set or the contact has been removed from the database
            Contact retrievedContact = contactsDAO.get(appointment.getContactID());
            contact = retrievedContact;
            if(contact != null) {
                for (Contact con: contactComboBox.getItems()) {
                    if (con.getId() == contact.getId()) {
                        contactComboBox.getSelectionModel().select(con);
                    }
                }
            }
        }
        if(appointment.getStart() != null) {//setting start time
            LocalTime lt = appointment.getStart().toLocalTime();
            startTimeField.setText(lt.toString());
            startDate.setValue(appointment.getStart().toLocalDate());
        }
        if(appointment.getEnd() != null)//end time
        {
            LocalTime lt = appointment.getEnd().toLocalTime();
            endTimeField.setText(lt.toString());
            endDate.setValue(appointment.getEnd().toLocalDate());
        }
    }
    /**
     * Called when the user clicks Submit.
     * Adds data within the window's input fields to appointment's members.
     * Checks if fields are populated first and have the correct data.
     */
    public void add()
    {
        //validating entry
        if(validate())
        {
            //retrieving contact
            contact = contactComboBox.getSelectionModel().getSelectedItem();

            appointment.setTitle(titleField.getText());
            appointment.setDescription(descriptionField.getText());
            appointment.setLocation(locationField.getText());
            appointment.setContactName(contact.getContactName());
            appointment.setType(typeField.getText());
            appointment.setContactID(contact.getId());

            //converting start and end times
            LocalDate sd = startDate.getValue();
            LocalDateTime sldt = LocalDateTime.of(sd, sTime);
            appointment.setStart(sldt);
            LocalDate ed = endDate.getValue();
            LocalDateTime eldt = LocalDateTime.of(ed, eTime);
            appointment.setEnd(eldt);



            //check for time collision
            if (genConfirm()) ((Stage) submitButton.getScene().getWindow()).close();
        }
    }
    /**
     * Handles the user's cancel request.
     */
    public void cancel()
    {
        Confirm confirm = new Confirm();
        if(confirm.beginStart())
        {
            appointment = null;
            ((Stage) cancelButton.getScene().getWindow()).close();
        }
    }
    /**
     * Called within the add() function.
     * Checks the input fields, ComboBoxes, and DateTimePickers are populated.
     * This function also checks if the start and end times entered have zero conflicts, this is done in four ways.
     * First the data is checked to see if it is entered correctly
     * example the start and end times are entered correctly and can be parsed into LocalTime objects.
     * Second the start and end times follow a logical sequence, meaning end time doesn't happen before the start time.
     * Third the start and end times occur within the eastern office hours.
     * Fourth the start and end times are checked if they are available meaning no existing appointments will overlap.
     *
     * If any conflict occurs the user is notified through error labels and/or error messages.
     *
     * @return True if the user has entered the data correctly and have zero conflicts with their entered times, and
     * returns false if fields have been left blank, start and times are not entered correctly, or start and times are
     * contradictions, or appointment date and times collide/overlap with other appointments.
     */
    public boolean validate()
    {
        //checking for blank/unpopulated fields

        if(titleField.getText().isBlank()) {
            genError("Error 'Title' field cannot be blank!");
            titleField.setStyle("-fx-text-box-border: red");

            descriptionField.setStyle("");
            locationField.setStyle("");
            typeField.setStyle("");
            startTimeField.setStyle("");
            endTimeField.setStyle("");
            //error labels
            startDateError.setVisible(false);
            endDateError.setVisible(false);
            contactsError.setVisible(false);
        }
        else if(descriptionField.getText().isBlank()) {
            genError("Error 'Description' field cannot be blank!");
            descriptionField.setStyle("-fx-text-box-border: red");

            titleField.setStyle("");
            locationField.setStyle("");
            typeField.setStyle("");
            startTimeField.setStyle("");
            endTimeField.setStyle("");
            //error labels
            startDateError.setVisible(false);
            endDateError.setVisible(false);
            contactsError.setVisible(false);
        }
        else if(locationField.getText().isBlank()) {
            genError("Error 'Location' field cannot be blank!");
            locationField.setStyle("-fx-text-box-border: red");

            titleField.setStyle("");
            descriptionField.setStyle("");
            typeField.setStyle("");
            startTimeField.setStyle("");
            endTimeField.setStyle("");
            //error labels
            startDateError.setVisible(false);
            endDateError.setVisible(false);
            contactsError.setVisible(false);
        }
        else if(typeField.getText().isBlank()) {
            genError("Error 'Type' field cannot be blank!");
            typeField.setStyle("-fx-text-box-border: red");

            titleField.setStyle("");
            descriptionField.setStyle("");
            locationField.setStyle("");
            startTimeField.setStyle("");
            endTimeField.setStyle("");
            //error labels
            startDateError.setVisible(false);
            endDateError.setVisible(false);
            contactsError.setVisible(false);
        }
        else if(startTimeField.getText().isBlank()){
            genError("Error 'Start Time' cannot be blank!");
            startTimeField.setStyle("-fx-text-box-border: red");

            titleField.setStyle("");
            descriptionField.setStyle("");
            locationField.setStyle("");
            typeField.setStyle("");
            endTimeField.setStyle("");
            //error labels
            startDateError.setVisible(false);
            endDateError.setVisible(false);
            contactsError.setVisible(false);
        }
        else if(endTimeField.getText().isBlank()){
            genError("Error 'End Time' cannot be blank!");
            endTimeField.setStyle("-fx-text-box-border: red");

            titleField.setStyle("");
            descriptionField.setStyle("");
            locationField.setStyle("");
            typeField.setStyle("");
            startTimeField.setStyle("");
            //error labels
            startDateError.setVisible(false);
            endDateError.setVisible(false);
            contactsError.setVisible(false);
        }
        else if(contactComboBox.getSelectionModel().getSelectedItem() == null){
            genError("Error please select a contact");
            contactsError.setVisible(true);

            titleField.setStyle("");
            descriptionField.setStyle("");
            locationField.setStyle("");
            typeField.setStyle("");
            startTimeField.setStyle("");
            endTimeField.setStyle("");
            //error labels
            startDateError.setVisible(false);
            endDateError.setVisible(false);
        }
        else if(startDate.getValue() == null){
            genError("Error Please select a date");
            startDateError.setVisible(true);

            titleField.setStyle("");
            descriptionField.setStyle("");
            locationField.setStyle("");
            typeField.setStyle("");
            startTimeField.setStyle("");
            endTimeField.setStyle("");
            //error labels
            endDateError.setVisible(false);
            contactsError.setVisible(false);
        }
        else if(endDate.getValue() == null){
            genError("Error Please select a date");
            endDateError.setVisible(true);

            titleField.setStyle("");
            descriptionField.setStyle("");
            locationField.setStyle("");
            typeField.setStyle("");
            startTimeField.setStyle("");
            endTimeField.setStyle("");
            //error labels
            startDateError.setVisible(false);
            contactsError.setVisible(false);
        }

        //validating time and date selection

        //checking if the start and end has the correct format
        else if(!validateTimes()) {
            titleField.setStyle("");
            descriptionField.setStyle("");
            locationField.setStyle("");
            typeField.setStyle("");
            //error labels
            startDateError.setVisible(false);
            endDateError.setVisible(false);
            contactsError.setVisible(false);
        }
        //checking if start time is before end time
        else if (!validateTimeSeq()) {
            genError("Error end time cannot occur before start time");
            startTimeField.setStyle("-fx-text-box-border: red");
            endTimeField.setStyle("-fx-text-box-border: red");

            titleField.setStyle("");
            descriptionField.setStyle("");
            locationField.setStyle("");
            typeField.setStyle("");
            //error labels
            startDateError.setVisible(false);
            endDateError.setVisible(false);
            contactsError.setVisible(false);
        }
        //validating if start and end time are valid office hours
        else if(!hasOfficeHours()) {
            startTimeField.setStyle("-fx-text-box-border: red");
            endTimeField.setStyle("-fx-text-box-border: red");

            titleField.setStyle("");
            descriptionField.setStyle("");
            locationField.setStyle("");
            typeField.setStyle("");
            //error labels
            startDateError.setVisible(false);
            endDateError.setVisible(false);
            contactsError.setVisible(false);
        }
        //checking if start and end time and date are not taken
        else if(!hasAvailableTime()){
            startTimeField.setStyle("-fx-text-box-border: red");
            endTimeField.setStyle("-fx-text-box-border: red");

            titleField.setStyle("");
            descriptionField.setStyle("");
            locationField.setStyle("");
            typeField.setStyle("");
            //error labels
            startDateError.setVisible(false);
            endDateError.setVisible(false);
            contactsError.setVisible(false);
        }
        //clearing error fields
        else {
            titleField.setStyle("");
            descriptionField.setStyle("");
            locationField.setStyle("");
            typeField.setStyle("");
            startTimeField.setStyle("");
            endTimeField.setStyle("");

            //error labels
            startDateError.setVisible(false);
            endDateError.setVisible(false);
            contactsError.setVisible(false);
            return true;
        }
        return false;
    }

    /**
     * Generates generic error message using a string
     * @param msg error message that is being conveyed
     */
    public void genError(String msg)
    {
        Error err = new Error();
        err.beginStart(msg);
    }

    /**
     * Sets the end date field using the StartDate
     */
    public void setEndDate()
    {
        LocalDate localDate = startDate.getValue();
        endDate.setValue(localDate);
    }
    /**
     * Generates a confirmation box where the user either clicks continue or cancel.
     * @return Returns true if the user clicks confirm within the confirmation box
     */
    public boolean genConfirm()
    {
        Confirm con = new Confirm();
        return con.beginStart();
    }

    /**
     * Converts the user entered start and end time into ZoneDateTime objects and converts the time zones to
     * Eastern/New-York. The converted times are then checked to see if they fall within the defined office hours.
     * If the start and end times (once converted to eastern) fall on different dates then the times do not fall within
     * the office hours.
     *
     * @return True if the start and end time (once converted) occur on the same day and within the office hours of the
     * eastern office. Returns false otherwise and displays the appropriate error message to the user.
     *
     */
    public boolean hasOfficeHours(){

        //initializing office hours
        LocalDateTime startingTime = LocalDateTime.of(startDate.getValue(), sTime);
        LocalDateTime endingTime = LocalDateTime.of(startDate.getValue(), eTime);
        ZonedDateTime sZonedDateTime = startingTime.atZone(currentZone);
        ZonedDateTime eZoneDateTime = endingTime.atZone(currentZone);

        //conversion
        sZonedDateTime = sZonedDateTime.withZoneSameInstant(newYork);
        eZoneDateTime = eZoneDateTime.withZoneSameInstant(newYork);

        startingTime = sZonedDateTime.toLocalDateTime();
        endingTime = eZoneDateTime.toLocalDateTime();

        LocalTime slt = startingTime.toLocalTime();
        LocalTime elt = endingTime.toLocalTime();

        //appointment start and end must occur on the same day
        if(startingTime.toLocalDate().equals(endingTime.toLocalDate())){
            //checking starting time
            if(!officeOpen.equals(slt)) {
                if (officeOpen.isAfter(slt)) {
                    genError("Error start time occurs before opening hours");
                    return false;
                }
                else if (officeClose.isBefore(slt) || officeClose.equals(slt)) {
                    genError("Error start time occurs after closing hours");
                    return false;
                }
            }
            //checking ending time
            if(!officeClose.equals(elt)){
                if (officeOpen.isAfter(elt) || officeOpen.equals(elt)) {
                    genError("Error end time occurs before opening hours");
                    return false;
                }
                else if (officeClose.isBefore(elt)) {
                    genError("Error end time occurs after closing hours");
                    return false;
                }
            }
        }
        else {
            genError("Error start and end times occur outside of business hours");
            return false;
        }

        return true;
    }

    /**
     * Checks if the user has entered the correct "HH::MM" format for start and end times and if the end and start times
     * are not the same.
     * @return True if the user enters the start and end time in the correct format and the times do not overlap.
     * Returns false otherwise
     */
    public boolean validateTimes(){
        //checking if the entered time can be converted to LocalTime
        try{
            sTime = LocalTime.parse(startTimeField.getText());
        }catch(Exception e){
            genError("Error invalid time please enter in  \"HH:MM\" only");
            startTimeField.setStyle("-fx-text-box-border: red");
            endTimeField.setStyle("");
            return false;
        }
        //checking if the entered time can be converted to LocalTime
        try{
            eTime = LocalTime.parse(endTimeField.getText());
        }catch(Exception e){
            genError("Error invalid time please enter in \"HH:MM\" only");
            endTimeField.setStyle("-fx-text-box-border: red");
            startTimeField.setStyle("");
            return false;
        }

        if(sTime.equals(eTime)){
            genError("Error invalid time: Start time cannot be the same as End Time");
            endTimeField.setStyle("-fx-text-box-border: red");
            startTimeField.setStyle("-fx-text-box-border: red");
            return false;
        }

        startTimeField.setStyle("");
        endTimeField.setStyle("");
        return true;
    }
    /**
     * Checks if the user entered times are logically correct and do not contradict each other.
     * @return True if the start and end time are logical and do not contradict each other. Returns false otherwise
     */
    public boolean validateTimeSeq(){
        LocalDateTime start = LocalDateTime.of(startDate.getValue(), sTime);
        LocalDateTime end = LocalDateTime.of(endDate.getValue(), eTime);
        //if start and end time occur on different dates
        if(endDate.getValue().isBefore(startDate.getValue())) return false;

        else if(start.isBefore(end)){//if start time is earlier than end time

            return true;
        }
        return false;
    }
    /**
     * Checks if the entered start and end times do not overlap with other appointment times.
     * This function generates an error message according to the specific type of time collision, eg "End time already
     * taken", "End time occurs during another appointments start time".
     * @return True if the user enters the start and end time in the correct format and the times do not overlap.
     */
    public boolean hasAvailableTime(){
        AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
        List<Appointment> apps = appointmentDAO.getAll();
        for(Appointment app: apps){
            //ruling out appointments that are not initialized and the same appointment
            if(app.getStart() == null || app.getEnd() == null || app.getId().equals(appointment.getId())) continue;
            //excluding appointments with different dates
            else if(!app.getStart().toLocalDate().equals(startDate.getValue())) continue;
            //start time or end time is the same as the start time of another appointment
            else if(app.getStart().toLocalTime().equals(sTime)){
                genError("Error start time is already taken");
                return false;
            }
            else if (app.getStart().toLocalTime().equals(eTime)){
                genError("Error end time occurs during another appointments start time");
                return false;
            }
            //start time or end time is the same as the end time of another appointment
            else if(app.getEnd().toLocalTime().equals(sTime)) {
                genError("Error start time occurs with another appointments end time");
                return false;
            }
            else if(app.getEnd().toLocalTime().equals(eTime)){
                genError("End time already taken");
                return false;
            }
            //start time occurs during another appointment
            else if(app.getStart().toLocalTime().isBefore(sTime) && app.getEnd().toLocalTime().isAfter(sTime)){
                genError("start time overlaps with another appointment");
                return false;
            }
            //end time occurs during another appointment
            else if(app.getStart().toLocalTime().isBefore(eTime) && app.getEnd().toLocalTime().isAfter(eTime)){
                genError("End time overlaps with another appointment");
                return false;
            }
        }
        return true;
    }
}
