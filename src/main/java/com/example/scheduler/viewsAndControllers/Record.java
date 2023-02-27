package com.example.scheduler.viewsAndControllers;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;

import com.example.scheduler.data.*;
import com.example.scheduler.models.Appointment;
import com.example.scheduler.models.Customer;
import com.example.scheduler.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.function.Predicate;

import static java.lang.Math.abs;

/**
 * Record window controller.
 * Allows users to view all customers and appointments as well as create/update/delete customers and appointments.
 * Use {@link #beginStart(AppointmentDAOImpl, User)} to launch the window.
 *
 */
public class Record {

    //tables
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableView<Appointment> appointmentTable;


    //table columns
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> locationColumn;
    @FXML
    private TableColumn<Customer, Integer> custIDColumn;
    @FXML
    private TableColumn<Customer, String> contactColumn;
    @FXML
    private TableColumn<Customer, String> postalCodeColumn;
    @FXML
    private TableColumn<Customer, String> provinceColumn;

    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> apLocationColumn;
    @FXML
    private TableColumn<Appointment, String> startTimeColumn;
    @FXML
    private TableColumn<Appointment, String> contactNameColumn;
    @FXML
    private TableColumn<Appointment, Integer> custIDAppColumn;
    @FXML
    private TableColumn<Appointment, String> endTimeColumn;
    @FXML
    private TableColumn<Appointment, Integer> apIDColumn;
    @FXML
    private TableColumn<Appointment, String> monthColumn;
    @FXML
    private TableColumn<Appointment, Integer> weekColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> userIDTableColumn;

    //buttons
    @FXML
    private Button updateCustomerButton;
    @FXML
    private Button removeCustomerButton;
    @FXML
    private Button updateAppointmentButton;
    @FXML
    private Button cancelAppointmentButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button addAppointmentButton;

    //textareas
    @FXML
    private TextArea appDescription;

    //observableLists
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    //FilteredLists
    private static FilteredList<Customer> customerFilteredList = new FilteredList<Customer>(allCustomers, p -> true);
    private static FilteredList<Appointment> appointmentFilteredList = new FilteredList<Appointment>(allAppointments, p -> true);

    //DAO objects
    private static AppointmentDAOImpl appointmentDAO = null;
    private static UserDAOImpl userDAO = new UserDAOImpl();
    private static CustomerDAOImpl customerDAO = new CustomerDAOImpl();
    private static ContactsDAOImpl contactsDAO = new ContactsDAOImpl();

    private static User usr = null;

    /**
     * Called when the Record window is launched.
     * Initializes customer and appointment tables.
     * Sets bindings for table columns accessor methods.
     *
     * Lambda expression: Adds listener to {@link #appointmentTable} to execute {@link #loadDescription(String)} each
     * time an appointment record is clicked.
     */
    public void initialize() {
        allAppointments.addAll(appointmentDAO.getAll());
        allCustomers.addAll(customerDAO.getAll());

        //bindings
        customerTable.setPlaceholder(new Label("No customers on record"));
        appointmentTable.setPlaceholder(new Label("No appointments available"));

        //Customers
        nameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("address"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone"));
        custIDColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("postalCode"));
        provinceColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("province"));

        //Appointment
        titleColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("description"));
        apLocationColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("location"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("startString"));
        contactNameColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("contactName"));
        custIDAppColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("custID"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("end"));
        apIDColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("month"));
        weekColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("week"));
        userIDTableColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("usrID"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("type"));

        SortedList<Customer> customerSortedList = new SortedList<>(customerFilteredList);
        SortedList<Appointment> appointmentSortedList = new SortedList<>(appointmentFilteredList);
        customerSortedList.comparatorProperty().bind(customerTable.comparatorProperty());
        appointmentSortedList.comparatorProperty().bind(appointmentTable.comparatorProperty());

        customerTable.setItems(customerSortedList);
        appointmentTable.setItems(appointmentSortedList);

        //making long descriptions visible in full length
        appointmentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (appointmentTable.getSelectionModel().getSelectedItem() != null) {
                loadDescription(appointmentTable.getSelectionModel().getSelectedItem().getDescription());
            }
        });
        weekColumn.setVisible(false);
    }

    /**
     * Method that launches the window.
     * @param app Appointment DAO that provides access to the appointments table.
     * @param user Users information necessary for filling out user information when a customer is
     *             created/updated/deleted or an appointment is created/updated/deleted.
     */
    public void beginStart(AppointmentDAOImpl app, User user) {
        this.appointmentDAO = app;
        this.usr = user;
        URL uri = getClass().getResource("/com/example/scheduler/viewsAndControllers/record.fxml");
        FXMLLoader loader = new FXMLLoader(uri);
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Schedule");
            stage.setScene(new Scene(root));
//            stage.setMaximized(true);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Allows the user to create a customer.
     * A new customer is created in the following order.
     * First, a customer is created using the CustomerDAOImpl create method and the auto-generated customer id is
     * returned.
     * Second, the customer id is provided to the AddCustomer's beginStart method where the user fills out the
     * appropriate fields and the Customer object is created.
     * Third, the customer record is either updated with the relevant information from the beginStart method or the
     * customer record is deleted when the beginStart method returns null (meaning the user canceled).
     * If the user cancels the customer creation then a resulting error dialogue is displayed.
     *
     * If the user successfully creates a new customer then the Customer object is added to allCustomers ObservableList.
     */
    public void addCustomer() {
        int custID = customerDAO.create(usr);
        if(custID == -1) genError("Error failed to access client schedule database");
        else{
            AddCustomer addCust = new AddCustomer();
            addCustomerButton.setDisable(true);
            Customer tmp = addCust.beginStart(custID);
            addCustomerButton.setDisable(false);//preventing the user from opening more than one window.
            if (tmp != null) {
                tmp.setLastUpdatedBy(usr.getName());
                tmp.setCreatedBy(usr.getName());
                if(!customerDAO.update(tmp)){
                    genError("Failed to update database");
                    customerDAO.delete(custID);
                }
                else allCustomers.add(tmp);
            }
            else {
                genError("Failed to add Customer");
                customerDAO.delete(custID);
            }
        }
    }

    /**
     * Allows the user to create an appointment.
     * A new appointment is created in the following order.
     * First, an appointment is created using the AppointmentDAOImpl create method and the auto-generated appointment
     * id is returned.
     * Second, the Appointment object is created using the generated.
     * Third, the Appointment object is provided to the AddAppointment's beginStart method where the user fills out the
     * appropriate fields and the Appointment object is updated.
     * Fourth, the appointment record is either updated with the relevant information from the beginStart method or the
     * appointment record is deleted when the beginStart method returns null (meaning the user canceled).
     * If the user cancels the appointment creation then a resulting error dialogue is displayed.
     *
     * If the user successfully creates a new appointment then the Customer object is added to allAppointments
     * ObservableList.
     */
    public void addAppointment() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if(selected != null) {
            usr.setCustID(selected.getId());
            int appID = appointmentDAO.create(usr);
            if(appID == -1) genError("Error failed to access client schedule database");
            else {
                AddAppointment add = new AddAppointment();
                addAppointmentButton.setDisable(true);
                Appointment tmp = new Appointment(appID);
                tmp.setCustID(selected.getId());
                tmp.setUsrID(usr.getId());
                if(tmp == null) genError("TMP is null: " + appID);
                tmp = add.beginStart(tmp, "Add Appointment");
                addAppointmentButton.setDisable(false);//preventing the user from opening more than one window.
                if (tmp != null) {
                    if (!appointmentDAO.update(tmp)) {
                        genError("Failed to update database");
                        appointmentDAO.delete(appID);
                    } else allAppointments.add(tmp);

                } else {
                    genError("Failed to add Appointment");
                    appointmentDAO.delete(appID);
                }
            }
        }
        else genError("Error please select a customer first");

    }

    /**
     * Allows the user to remove an existing customer.
     * User is prompted with a confirmation dialogue before deletion can occur.
     *
     * Deletion occurs in three steps.
     * First, the Customer being deleted is checked for any associated appointments
     * Second, the customer record is deleted from the customers data table using the customer DAO.
     * Third, the customer object is deleted from allCustomers.
     *
     * Presents the user with an error dialog box if the customer has any associated appointments still active.
     *
     * Lambda expression: Used to create a predicate using the customer id as the comparator in {@link #allCustomers}
     * removeIf function to delete a customer with a specified id.
     */
    public void removeCustomer() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) genError("Customer not selected");
        else {
            Confirm con = new Confirm();
            removeCustomerButton.setDisable(true);//preventing the user from opening more than one window.
            if (con.beginStart()) {
                if(appointmentDAO.countCustomerAppointments(selected.getId()) == 0) {
                    String custName = selected.getName();
                    Predicate<Customer> hasID = (i) -> i.getId() == selected.getId();
                    allCustomers.removeIf(hasID);
                    customerDAO.delete(selected.getId());
                    Error err = new Error();
                    err.beginStart("Customer " + custName + " has been deleted!");
                }
                else{
                    genError("Error You must remove all appointments associated with the customer");
                }
            }
        }
        removeCustomerButton.setDisable(false);
    }

    /**
     * Allows the user to remove an existing Appointment.
     * User is prompted with a confirmation dialogue before deletion can occur.
     *
     * Deletion occurs in two phases.
     * First, appointment record is deleted from the appointments data table using the appointment DAO.
     * Second, the Appointment object is removed from allAppointments.
     *
     * Lambda expression: Used to create a predicate using the appointment id as the comparator in
     * {@link #allAppointments} removeIf() function to delete an appointment that has a specified id.
     */
    public void removeAppointment() {
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) genError("Appointment not selected");
        else {

            Confirm con = new Confirm();
            cancelAppointmentButton.setDisable(true);
            if (con.beginStart()) {
                int id = selected.getId();
                String type = selected.getType();
                Predicate<Appointment> hasID = (i) -> i.getId() == selected.getId();
                allAppointments.removeIf(hasID);//removing non-updated appointment
                if(!appointmentDAO.delete(selected)){
                    genError("Error failed to delete from database");
                }
                Error err = new Error();
                err.beginStart("Appointment #" + id + " of type \"" + type + "\" has been deleted!");
            }
            cancelAppointmentButton.setDisable(false);
        }

    }
    /**
     * Allows the user to update an existing customer.
     *
     * customer update occurs in three steps.
     * First, a customer is selected from the customers table by the user.
     * Second, the Customer object is provided to the AddCustomer controller beginStart() method.
     * Third, the user changes the Customer information using the AddCustomer window.
     * Fourth, the Customer object is returned from the beginStart() method.
     * Fifth, the customer record is updated using the customer DAO.
     * Sixth, the Customer object is removed from allCustomers and the updated object is appended.
     *
     * If the user cancels the update, they are presented with an error dialog stating the update failed.
     * If the user fails to select a customer from the table then they are presented with an error dialog, prompting
     * them to select a customer.
     *
     * Lambda expression: Used to create
     * Lambda expression: Used to create a predicate using the customer id as the comparator in {@link #allCustomers}
     * removeIf function to delete a customer with a specified id.
     */
    public void updateCustomer() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Customer tmp;
            AddCustomer addCust = new AddCustomer();
            updateCustomerButton.setDisable(true);
            tmp = addCust.beginStart(selected);
            updateCustomerButton.setDisable(false);
            if (tmp != null) {
                Predicate<Customer> hasID = (i) -> i.getId() == selected.getId();
                allCustomers.removeIf(hasID);
                if(!customerDAO.update(tmp)){
                    genError("Failed to update database");
                }
               else allCustomers.add(tmp);
               customerTable.refresh();//forcing the table to refresh
            } else genError("Failed to update information");
        } else genError("Error! Please select a customer");
    }

    /**
     * Allows the user to update an existing appointment.
     *
     * appointment update occurs in three steps.
     * First, an appointment is selected from the appointments table by the user.
     * Second, the appointment object is provided to the AddAppointment controller beginStart() method.
     * Third, the user changes the appointment information using the AddAppointment window.
     * Fourth, the Appointment object is returned from the beginStart() method.
     * Fifth, the appointment record is updated using the appointment DAO.
     * Sixth, the appointment object is removed from allAppointments and the updated object is appended.
     *
     * If the user cancels the update, they are presented with an error dialog stating the update failed.
     * If the user fails to select an appointment from the table then they are presented with an error dialog, prompting
     * them to select an appointment.
     *
     * Lambda expression: Used to create a predicate using the appointment id as the comparator in
     * {@link #allAppointments} removeIf() function to delete an appointment that has a specified id.
     */
    public void updateAppointment() {
        Appointment selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Appointment tmp;
            AddAppointment update = new AddAppointment();
            updateAppointmentButton.setDisable(true);
            tmp = update.beginStart(selected, "Update Appointment");
            updateAppointmentButton.setDisable(false);
            if (tmp != null) {
                if(!appointmentDAO.update(tmp)){
                    genError("Failed to update database");
                }
                else {

                    //allAppointments.add(tmp);
                    Predicate<Appointment> hasID = (i) -> i.getId() == (selected.getId());
                    allAppointments.removeIf(hasID);
                    allAppointments.add(tmp);
                    appointmentTable.refresh(); // forcing refresh
                }
            } else genError("Failed to update information");
        } else genError("Error! Please select an appointment");
    }


    /**
     * Provides an alternate view of the appointment description through a multi-line text-field.
     * @param description The appointment description.
     */
    public void loadDescription(String description) {
        appDescription.setText("Description: " + description);
    }

    /**
     * Generates error dialog.
     * @param msg Error message to be displayed.
     */
    public void genError(String msg) {
        Error err = new Error();
        err.beginStart(msg);
    }
    /**
     * Allows the user to view appointments that occur during the same month.
     * Creates an error dialog if there are no appointments available for the current month.
     *
     * Lambda expression: Used to create a predicate using the appointment start time to filter
     * {@link #appointmentTable} based on the current month.
     */
    public void appointmentsThisMonth(){

        appointmentFilteredList.setPredicate(appoint->{

            //current time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM");
            LocalDate currentDate = LocalDate.now();
            LocalDate appointmentDate = appoint.getStart().toLocalDate();
            String currentMonth = currentDate.format(formatter);
            String appointmentMonth = appointmentDate.format(formatter);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp.getTime());
            //creating a string filter
            if(currentMonth.equals(appointmentMonth)){
                return true;
            }
            else{
                return false;
            }
        });
        if(appointmentFilteredList.size() == 0){
            genError("No appointments available for this month");
        }
    }

    /**
     * Allows the user to view appointments that occur during the same week.
     * Creates an error dialog if there are no appointments available for the current week.
     *
     * Lambda expression: Used to create a predicate using the appointment start time to filter
     * {@link #appointmentTable} based on the current week.
     */
    public void appointmentsThisWeek(){
        appointmentFilteredList.setPredicate(appoint->{
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp.getTime());
            //creating a string filter
            Integer currentWeek = calendar.get(Calendar.WEEK_OF_MONTH);
            String currentMonth = (Month.of(calendar.get(Calendar.MONTH)+1)).toString();
            if(appoint.getWeek() == currentWeek && appoint.getMonth().equals(currentMonth)){
                return true;
            }
            else{
                return false;
            }
        });
        if(appointmentFilteredList.size() == 0){
            genError("No appointments available this week");
        }
    }

    /**
     * Removes the current filter on the appointments table allowing the user to see all appointments.
     *
     * Lambda expression: Used to create a predicate that removes a filter from {@link #appointmentTable}.
     */
    public void noFilter(){
        appointmentFilteredList.setPredicate(appoint->{
            return true;
        });
        if(appointmentFilteredList.size() == 0){
            genError("No appointments available this week");
        }
    }

    /**
     * Launches the "generate report" window.
     */
    public void genReport(){
        Report contactsSchedule = new Report();
        contactsSchedule.beginStart();
    }
    /**
     * Allows the user to logout from the application.
     */
    public void logout(){
        ((Stage) appointmentTable.getScene().getWindow()).close();
    }

}
