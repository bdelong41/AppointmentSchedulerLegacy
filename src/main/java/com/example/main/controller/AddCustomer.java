package com.example.main.controller;

import com.example.main.dao.CountryDAOImpl;
import com.example.main.dao.DivisionsDAOImpl;
import com.example.main.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * AddCustomer controller object.
 * Allows users to create customers and alter customer information.
 * Use {@link #beginStart(Customer)} or {@link #beginStart(Integer)} to launch the window.
 */
public class AddCustomer {

    //text fields
    @FXML
    private TextField name;
    @FXML
    private TextField IDField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private Button addCustomerField;

    //combo boxes
    @FXML
    private ComboBox<String> stringComboBox;
    @FXML
    private ComboBox<String> division;

    //labels
    @FXML
    private Label countryErrorLabel;
    @FXML
    private Label divisionErrorLabel;

    public static Customer cust;
    private static Integer id;

    //data
    private static CountryDAOImpl countryDAO = new CountryDAOImpl();
    private static DivisionsDAOImpl divisionsDAO = new DivisionsDAOImpl();

    private static ObservableList<String> countries = FXCollections.observableArrayList();
    private static ObservableList<String> divisions = FXCollections.observableArrayList();

    /**
     * Initializes the countries and divisions ComboBoxes and populates the input fields.
     */
    public void initialize()
    {

        countries.clear();
        divisions.clear();
        divisions.add("select a country first");
        countries.addAll(countryDAO.getAll());
        stringComboBox.setItems(countries);
        division.setItems(divisions);

        createID();
    }

    /**
     * Starts the window.
     *
     * Lambda expression: Sets stageCloseRequest to execute {@link #cancel()} function instead of using the default
     * javaFX closing method.
     * @param id holds a reference to the newly created, or existing appointment
     * @return cust The newly created customer. Returns null if the user clicks the cancel button.
     */
    public Customer beginStart(Integer id)
    {
        this.id = id;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/main/addCustomer.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Customer");
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(e->{
                e.consume();
                //run cancel function
                ((AddCustomer) loader.getController()).cancel();
            });
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.cust;
    }
    /**
     * Starts the window.
     * This is called if a customer is being altered.
     *
     * Lambda expression: Sets stageCloseRequest to execute {@link #cancel()} function instead of using the default
     * javaFX closing method.
     * @param customer Reference to an existing customer within the database
     * @return cust Contains the updated information. Returns null if the user clicks the cancel button.
     */
    public Customer beginStart(Customer customer)
    {
        this.cust = customer;
        this.id = customer.getId();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/scheduler/viewsAndControllers/" +
                "addCustomer.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Update Customer");
            stage.setScene(new Scene(root));
            AddCustomer scenePointer = loader.getController();
            scenePointer.setUpdate(customer);

            stage.setOnCloseRequest(e->{
                e.consume();
                //run cancel function
                ((AddCustomer) loader.getController()).cancel();
            });
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ((AddCustomer) loader.getController()).cust;
    }
    /**
     * Sets the customer id field within the window.
     */
    public void createID()
    {
        IDField.setText(id.toString());
    }
    /**
     * Populates the relevant customer fields when an existing customer is being altered/updated.
     * @param customer Customer object being updated.
     */
    public void setUpdate(Customer customer)
    {
        if(customer != null) {
            cust = customer;
            addCustomerField.setText("Update Customer");
            name.setText(customer.getName());
            addressField.setText(customer.getAddress());
            postalCodeField.setText(customer.getPostalCode());
            phoneNumberField.setText(customer.getPhone());

            stringComboBox.getSelectionModel().select(cust.getCountry());
            genUpdate();
            division.getSelectionModel().select(cust.getProvince());
        }
    }
    /**
     * Verifies the user input and creates a new Customer object with the user input.
     * Creates a confirmation box to prompt the user if they wish to continue.
     */
    public void addCustomer()
    {
        if(verifyInput()) {
            cust = new Customer(
                    name.getText(),
                    addressField.getText(),
                    phoneNumberField.getText(),
                    stringComboBox.getSelectionModel().getSelectedItem(),
                    division.getSelectionModel().getSelectedItem(),
                    postalCodeField.getText(), id);
            if (genConfirm()) ((Stage) addCustomerField.getScene().getWindow()).close();
        }
    }

    /**
     * Checks if the user has filled all necessary fields and none remain blank.
     * @return True if all necessary fields are populated. Returns false otherwise.
     */
    public boolean verifyInput()
    {
        if(name.getText().isBlank())
        {
            genError("Error 'Name' field cannot be blank!");
            name.setStyle("-fx-text-box-border: red");
            addressField.setStyle("");
            postalCodeField.setStyle("");
            phoneNumberField.setStyle("");
            divisionErrorLabel.setVisible(false);
            countryErrorLabel.setVisible(false);
        }
        else if(addressField.getText().isBlank()) {
            genError("Error 'Address' field cannot be blank!");
            addressField.setStyle("-fx-text-box-border: red");
            name.setStyle("");
            postalCodeField.setStyle("");
            phoneNumberField.setStyle("");
            divisionErrorLabel.setVisible(false);
            countryErrorLabel.setVisible(false);
        }
        else if(postalCodeField.getText().isBlank()) {
            genError("Error 'PostalCode' field cannot be blank!");
            postalCodeField.setStyle("-fx-text-box-border: red");
            name.setStyle("");
            addressField.setStyle("");
            phoneNumberField.setStyle("");
            divisionErrorLabel.setVisible(false);
            countryErrorLabel.setVisible(false);;
        }
        else if(phoneNumberField.getText().isBlank()) {
            genError("Error 'PhoneNumber' field cannot be blank!");
            phoneNumberField.setStyle("-fx-text-box-border: red");
            name.setStyle("");
            addressField.setStyle("");
            postalCodeField.setStyle("");
            divisionErrorLabel.setVisible(false);
            countryErrorLabel.setVisible(false);
        }
        else if(stringComboBox.getSelectionModel().getSelectedItem() == null)
        {
            genError("Error Country cannot remain unselected!");
            countryErrorLabel.setVisible(true);
            name.setStyle("");
            addressField.setStyle("");
            postalCodeField.setStyle("");
            phoneNumberField.setStyle("");
            divisionErrorLabel.setVisible(false);
        }
        else if(division.getSelectionModel().getSelectedItem() == null)
        {
            genError("Error Division cannot remain unselected!");
            countryErrorLabel.setVisible(true);
            name.setStyle("");
            addressField.setStyle("");
            postalCodeField.setStyle("");
            phoneNumberField.setStyle("");
            countryErrorLabel.setVisible(false);
        }
        else if(division.getSelectionModel().getSelectedItem().equals("select a country first")){
            genError("Error Country cannot remain unselected!");
            name.setStyle("");
            addressField.setStyle("");
            postalCodeField.setStyle("");
            phoneNumberField.setStyle("");
            countryErrorLabel.setVisible(false);
        }
        else {
            name.setStyle("");
            addressField.setStyle("");
            postalCodeField.setStyle("");
            phoneNumberField.setStyle("");
            divisionErrorLabel.setVisible(false);
            countryErrorLabel.setVisible(false);
            return true;
        }
        return false;
    }

    /**
     * Called when the user clicks the cancel button and sets cust to null.
     */
    public void cancel()
    {
        cust = null;
        ((Stage) addCustomerField.getScene().getWindow()).close();
    }
    /**
     * Generates an error message for the user to view.
     * @param msg error message to display.
     */
    public void genError(String msg)
    {
        Error err = new Error();
        err.beginStart(msg);
    }
    /**
     * Generates a confirmation box for the user to choose to continue their action or cancel.
     * @return True if the user clicks continue, false otherwise.
     */
    public boolean genConfirm()
    {
        Confirm con = new Confirm();
        return con.beginStart();
    }
    /**
     * Retrieves the correct division information according to the country being selected and adds them to the divisions
     * ComboBox.
     */
    public void genUpdate()
    {
        String country = stringComboBox.getSelectionModel().getSelectedItem();
        divisions.clear();
        if(country != null) divisions.addAll(divisionsDAO.get(country));
    }
}
