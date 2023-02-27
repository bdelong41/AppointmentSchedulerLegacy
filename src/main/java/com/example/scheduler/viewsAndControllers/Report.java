package com.example.scheduler.viewsAndControllers;

import com.example.scheduler.data.AppointmentDAOImpl;
import com.example.scheduler.data.ContactsDAOImpl;
import com.example.scheduler.models.Appointment;
import com.example.scheduler.models.Contact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;

/**
 * Allows the user to view a general report based on the appointment attributes and associated contacts.
 * Includes three separate report sections.
 * 1. The user can view the total number of appointments by appointment type and month it takes place on.
 * 2. The user can view the schedule of a selected contact in the appointments table.
 * 3. The user can view the total number of customers associated with a selected contact.
 *
 * Use {@link #beginStart()} to launch the window.
 *
 */
public class Report {
    //tables
    @FXML
    private TableView<Contact> contactsTable;
    @FXML
    private TableView<Appointment> appointmentsTable;

    //table columns
    @FXML
    private TableColumn<Contact, Integer> contactIDColumn;
    @FXML
    private TableColumn<Contact, String> contactNameColumn;
    @FXML
    private TableColumn<Contact, String> contactEmailColumn;
    @FXML
    private TableColumn<Appointment, Integer> apIDColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> startDateColumn;
    @FXML
    private TableColumn<Appointment, String> startTimeColumn;
    @FXML
    private TableColumn<Appointment, String> endDateColumn;
    @FXML
    private TableColumn<Appointment, String> endTimeColumn;
    @FXML
    private TableColumn<Appointment, Integer> custIDAppColumn;

    //combo boxes
    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private ComboBox<String> contactsComboBox;

    //TextFields
    @FXML
    private TextField totalMonthAndType;
    @FXML
    private TextField totalCustomers;

    //buttons
    @FXML
    private Button closeButton;

    //labels
    @FXML
    private Label typeReportError;
    @FXML
    private Label customerReportError;
    @FXML
    private Label noTypesFoundError;

    private ObservableList<Contact> allContacts = FXCollections.observableArrayList();
    private ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private ObservableList<String> allMonths = FXCollections.observableArrayList();
    private ObservableList<String> allTypes = FXCollections.observableArrayList();
    private ObservableList<String> contactNames = FXCollections.observableArrayList();
    private FilteredList<Contact> contactFilteredList = new FilteredList<>(allContacts, p->true);
    private FilteredList<Appointment> appointmentFilteredList = new FilteredList<>(allAppointments, p->true);

    AppointmentDAOImpl appointmentDAO = new AppointmentDAOImpl();
    ContactsDAOImpl contactsDAO = new ContactsDAOImpl();

    /**
     * Initializes the contents of the months combobox, appointments and contacts tables.
     * Sets the bindings for each table column and placeholders for each table.
     * Adds listener to the contacts table to allow the user to view only the associated appointments.
     * Creates a predicate for the appointments table based on clicking a contact from the contacts table.
     *
     * Lambda expression: Adds listener to {@link #contactsTable} to allow users to filter appointments based on the
     * contact they select from the contacts table.
     * Lambda expression: Adds predicate to {@link #appointmentsTable} to filter appointments based on the appointment
     * associated with a selected contact, allowing the user to view the contact's schedule.
     */
    public void initialize(){

        typeReportError.setVisible(false);
        customerReportError.setVisible(false);
        noTypesFoundError.setVisible(false);

        //initializing observable arrays
        allMonths.addAll(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December"));
        allContacts.addAll(contactsDAO.getAll());
        allAppointments.addAll(appointmentDAO.getAll());
        allTypes.addAll(appointmentDAO.getAllTypes());

        for(int index = 0; index < allContacts.size(); index++){
            contactNames.add(allContacts.get(index).getContactName());
        }

        //ComboBox bindings
        monthComboBox.setItems(allMonths);
        typeComboBox.setItems(allTypes);
        contactsComboBox.setItems(contactNames);

        //validating appointment types
        validateTypes();

        //tables

        contactsTable.setPlaceholder(new Label("No Customers on record"));
        appointmentsTable.setPlaceholder(new Label("No appointments available"));

        //table bindings
        contactsTable.setItems(contactFilteredList);
        appointmentsTable.setItems(appointmentFilteredList);

        contactIDColumn.setCellValueFactory(new PropertyValueFactory<Contact, Integer>("id"));
        contactNameColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("contactName"));
        contactEmailColumn.setCellValueFactory(new PropertyValueFactory<Contact, String>("email"));

        apIDColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startTime"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("endDate"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("endTime"));
        custIDAppColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("custID"));

        //Allowing the user to view the appointments associated with a contact
        contactsTable.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue)-> {
            //Setting Predicate for filtered list
            appointmentFilteredList.setPredicate(appointment -> {
                //creating a string filter
                //Using the current value stored in Search_TextArea
                if (newValue == null || appointment.getContactID() == newValue.getId()) {
                    return true;
                } else {
                    return false;
                }
            });
            if(appointmentFilteredList.size() == 0 && contactsTable.getSelectionModel().getSelectedItem()!= null){
                genError("No appointments scheduled for: " +
                        contactsTable.getSelectionModel().getSelectedItem().getContactName());
            }
        });

        contactsTable.setItems(contactFilteredList);
        appointmentsTable.setItems(appointmentFilteredList);
    }

    /**
     * Used to launch the window.
     */
    public void beginStart() {
        URL uri = getClass().getResource("/com/example/scheduler/viewsAndControllers/generateReport.fxml");
        FXMLLoader loader = new FXMLLoader(uri);

        //Loader
        try {
            Parent root = loader.load();
            Stage stage = new Stage();

            //Creating ErrorDialogueBox instance
            stage.setTitle("Contact Schedule");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Counts the number of appointments with the selected month and selected type and displays the total to the user.
     * Generates an error dialog if the user doesn't select a month or type.
     */
    public void countByMonthAndType(){
        if(monthComboBox.getSelectionModel().getSelectedItem() == null) return;
        if(typeComboBox.getSelectionModel().getSelectedItem() == null) return;
        Integer total = 0;
        for(int index = 0; index < allAppointments.size(); index++){

            if(allAppointments.get(index).getMonth().toLowerCase().equals(
                    monthComboBox.getSelectionModel().getSelectedItem().toLowerCase())) {
                if (typeComboBox.getSelectionModel().getSelectedItem().equals(allAppointments.get(index).getType())) {
                    total += 1;
                }
            }
        }
        totalMonthAndType.setText(total.toString());
        if(total <1) {
            genError("No appointments for the selected month and type");
            typeReportError.setVisible(true);
        }
        else{
            typeReportError.setVisible(false);
        }
    }

    /**
     * Counts the number of customers associated with the selected contact displays the total to the user.
     * Generates and error dialog if the user doesn't select a contact.
     */
    public void countCustomersByContact(){
        if(contactsComboBox.getSelectionModel().getSelectedItem() == null) return;
        Integer count = 0;
        for(int index = 0; index < allAppointments.size(); index++){
            if(allAppointments.get(index).getContactID() ==
                    allContacts.get(contactsComboBox.getSelectionModel().getSelectedIndex()).getId()){
                count+=1;
            }
        }
        totalCustomers.setText(count.toString());
        if(count < 1) {
            genError("No appointments available for selected contact");
            customerReportError.setVisible(true);
        }
        else{
            customerReportError.setVisible(false);
        }
    }
    /**
     * Closes the window then the user clicks the close button.
     */
    public void close(){
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    /**
     * Generates error dialog.
     * @param msg Error message to be displayed.
     */
    public void genError(String msg)
    {
        Error err = new Error();
        err.beginStart(msg);
    }

    /**
     * Checks if there are any unique appointment types in the appointments data table.
     * Generates an error dialog when there are no appointment types found.
     */
    public void validateTypes(){
        if(allTypes.size() == 0){
            genError("Error no appointment types found in database");
            noTypesFoundError.setVisible(true);
        }
        else{
            typeComboBox.getSelectionModel().select(1);
            monthComboBox.getSelectionModel().select(1);
            //initial startup
            if(monthComboBox.getSelectionModel().getSelectedItem() == null) return;
            if(typeComboBox.getSelectionModel().getSelectedItem() == null) return;
            Integer total = 0;
            for(int index = 0; index < allAppointments.size(); index++){

                if(allAppointments.get(index).getMonth().toLowerCase().equals(
                        monthComboBox.getSelectionModel().getSelectedItem().toLowerCase())) {
                    if (typeComboBox.getSelectionModel().getSelectedItem().equals(allAppointments.get(index).getType()))
                    {
                        total += 1;
                    }
                }
            }
            totalMonthAndType.setText(total.toString());
        }
    }
}
