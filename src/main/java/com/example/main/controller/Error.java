package com.example.main.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Generates error window with a custom error message detailing the error.
 * Use beginStart() to launch the window.
 * The error window has focus until the user clicks ok.
 */
public class Error {
    @FXML
    private Label ErrorMessage;
    @FXML
    private Button OK;
    /**
     * Launches the error window.
     * @param errorMessage takes custom error message and displays it to the user.
     *
     * The window has focus until the user clicks ok.
     */
    public void beginStart(String errorMessage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/scheduler/main/error.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            //Creating ErrorDialogueBox instance
            Error errorDialogue = loader.getController();
            errorDialogue.setErrorMessage(errorMessage + "!");
            stage.setTitle("Error");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }catch(Exception e){
            System.out.println("Failed to load Error.fxml");
            e.printStackTrace();
        }
    }
    /**
     * Allows the user to close the window.
     */
    public void okButtonListener(){
        ((Stage) OK.getScene().getWindow()).close();
    }

    /**
     * Sets the error message of the window.
     * @param errorMessage sets the error message of the dialog box.
     */
    public void setErrorMessage(String errorMessage){
        ErrorMessage.setText(errorMessage);
        ErrorMessage.setWrapText(true);
    }
}
