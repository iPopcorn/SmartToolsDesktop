/**
 * MainController.java is the class that holds all the logic connected to the GUIs.
 *
 * TODO: Keep description of MainController.java updated
 * **/
package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    public Button btnOpenLookup;
    public Button btnOpenAddTool;
    public Button btnOpenReport;
    public Button btnLookupBack;
    public Button btnGenReportBack;
    public Button btnAddToolBack;

    /**
     * changeScene() is the function that is called when a button on the main menu is pressed.
     * changeScene() takes 1 parameter which is an ActionEvent object. The ActionEvent's getSource()
     * method is used to find out which button was clicked. Depending on which button is clicked, a different root
     * is used for the scene that is created.
     * **/
    public void changeScene(ActionEvent actionEvent) throws IOException {
        Stage stage;
        Parent root;
        if(actionEvent.getSource() == btnOpenLookup){
            // get reference to button's stage
            stage = (Stage) btnOpenLookup.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("lookup_tool.fxml"));
        }else if(actionEvent.getSource() == btnOpenAddTool){
            stage = (Stage) btnOpenAddTool.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("add_tool.fxml"));
        }else if(actionEvent.getSource() == btnOpenReport){
            stage = (Stage) btnOpenReport.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("generate_report.fxml"));
        }else{
            stage = null;
            root = null;
        }

        // create new scene with stage and root
        if(stage != null && root != null){
            Scene scene = new Scene(root, 500, 500);
            stage.setScene(scene);
            stage.show();
        }else{
            System.out.println("Stage or root is null!");
        }
    }

    public void openMainMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = null;
        Parent root = null;

        if(actionEvent.getSource() == btnLookupBack){
            // get reference to button's stage
            stage = (Stage) btnLookupBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        }else if(actionEvent.getSource() == btnAddToolBack){
            stage = (Stage) btnAddToolBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        }else if(actionEvent.getSource() == btnGenReportBack){
            stage = (Stage) btnGenReportBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        }else{
            stage = null;
            root = null;
        }

        // create new scene with stage and root
        if(stage != null && root != null){
            Scene scene = new Scene(root, 500, 500);
            stage.setScene(scene);
            stage.show();
        }else{
            System.out.println("Stage or root is null!");
        }

    }
}
