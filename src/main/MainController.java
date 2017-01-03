/**
 * MainController.java is the class that holds all the logic connected to the GUIs.
 *
 * TODO: Keep description of MainController.java updated
 * **/

package main;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    public Button btnOpenLookup;
    public Button btnOpenAddTool;
    public Button btnOpenReport;
    public Button btnOpenAdmin;
    public Button btnLookupBack;
    public Button btnGenReportBack;
    public Button btnAddToolBack;
    public Button btnAdminBack;
    public Button btnAddToolSubmit;
    public Button btnSwitchAddTool;

    public MenuButton btnAddToolAddress;
    public MenuButton btnAddToolName;
    public TextField txtTagID;

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
        }else if(actionEvent.getSource() == btnOpenAdmin) {
            stage = (Stage) btnOpenAdmin.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("admin.fxml"));
        }else{
            stage = null;
            root = null;
        }

        // create new scene with stage and root
        if(stage != null && root != null){
            Scene scene = new Scene(root, 640, 480);
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
        }else if(actionEvent.getSource() == btnAddToolBack){ //TODO: btnAddToolBack is not being read from add_tool.fxml
            stage = (Stage) btnAddToolBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
            System.out.println("Back Button Pressed in Add Tool");
        }else if(actionEvent.getSource() == btnGenReportBack){
            stage = (Stage) btnGenReportBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        }else if(actionEvent.getSource() == btnAdminBack) {
            stage = (Stage) btnAdminBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        }/*else if(actionEvent.getSource() == btnAddToolBack){
            stage = (Stage) btnAddToolBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        }*/else{
            stage = null;
            root = null;
        }

        // create new scene with stage and root
        if(stage != null && root != null){
            Scene scene = new Scene(root, 640, 480);
            stage.setScene(scene);
            stage.show();
        }else{
            System.out.println("Stage or root is null!");
        }

    }

    /**
     * addToolSubmit() is the function that is called when the submit button of the add tool screen is pressed.
     * This function grabs the data from the GUI and returns it as an array of strings, ready to be entered into
     * a DB query
     * **/
    public String[] addToolSubmit(){
        System.out.println("addToolSubmit()");

        // read string from text field
        String tagID = txtTagID.getText();

        // store all values in an array, ready to be inserted into a query
        String[] queryValues = new String[3];

        // get list of items for the tools and the addresses
        // TODO: address list should be generated based on tool list selection
        ObservableList<MenuItem> toolList = btnAddToolName.getItems();
        ObservableList<MenuItem> addressList = btnAddToolAddress.getItems();
        CheckMenuItem temp, lastSelectedTool = null, lastSelectedAddress = null;

        // iterate through the list of tool names, only used the last selected tool
        // TODO: think of some way to limit user to select only 1 tool
        for(int i = 0; i < toolList.size(); i++){
            temp = (CheckMenuItem) toolList.get(i);

            if(temp.isSelected()) {
                //System.out.printf("%s is selected\n", temp.getId());
                lastSelectedTool = temp;
            }
        }

        // iterate through address list, only use the last selected address
        for(int i = 0; i < addressList.size(); i++){
            temp = (CheckMenuItem) addressList.get(i);

            if(temp.isSelected()) {
                //System.out.printf("%s is selected\n", temp.getId());
                lastSelectedAddress = temp;
            }
        }

        if(lastSelectedAddress != null && lastSelectedTool != null){
            queryValues[0] = tagID;
            queryValues[1] = lastSelectedTool.getText();
            queryValues[2] = lastSelectedAddress.getText();
            System.out.printf("tag ID: %s\nTool Name: %s\nAddress: %s\n", queryValues[0], queryValues[1], queryValues[2]);
        }else{
            // TODO: add error label for non-selection
            System.out.printf("Either address or tool name not selected.");
        }

        return queryValues;
    }

    public void switchToAddTool(ActionEvent actionEvent) throws IOException {


        if( actionEvent.getSource() == btnSwitchAddTool)
        {
            System.out.println("btnSwitchAddTool");
            System.out.println(btnSwitchAddTool.getScene().getRoot().getId().toString());
            for(Node node: btnSwitchAddTool.getScene().getRoot().getChildrenUnmodifiable())
            {
                if(node.getId() != null) {
                    System.out.println(node.getId());
                    if(node.getId().matches("adminPane")) {
                        System.out.println("adminPane");
                        Parent root = FXMLLoader.load(getClass().getResource("add_tool.fxml"));
                        ((Pane) node).getChildren().add(root);
                    }
                }
            }
        }

    }
}
