package com.example.scheduler.viewsAndControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Generates a confirmation box that prompts users to cancel their action or continue.
 * Use beginStart() to launch the window.
 * The window has focus until the user clicks ok
 */
public class Confirm {
    @FXML
    private Button OK;
    @FXML
    private Button Cancel;

    //By default tf is false
    private static boolean action = false;
    /**
     * Launches the confirm window and allows users to select how they wish to continue with their actions.
     * @return action True if the user clicks continue. Returns false if they wish to cancel.
     * The window has focus until the user clicks ok.
     */
    public boolean beginStart(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/scheduler/" +
                    "viewsAndControllers/confirm.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Confirm confirmBox = loader.getController();
            stage.setTitle("Confirm Action");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch(Exception e){
            System.out.println("Failed to load confirm.fxml");
            e.printStackTrace();
        }
        return action;
    }

    /**
     * sets action to true when the user clicks confirm.
     */
    public void confirmButtonListener(){
        action = true;

        ((Stage) OK.getScene().getWindow()).close();
    }
    /**
     * Sets action to false if the user clicks cancel.
     */
    public void cancelButtonListener(){
        action = false;

        ((Stage) Cancel.getScene().getWindow()).close();
    }
}



