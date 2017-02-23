package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mwhar on 1/13/2017.
 */
public class LookupController {
    @FXML
    private TextField searchBox;
    @FXML
    private RadioButton radioByName;
    @FXML
    private RadioButton radioByID;
    @FXML
    private RadioButton radioByAddress;
    @FXML
    private ListView<Tool> toolListView;
    @FXML
    private Button btnBack;

    private ArrayList<Tool> toolList;

    @FXML
    private void initialize() {
        toolList = new ArrayList<>();
        createToolList();
        toolListView.setCellFactory(new ToolCellFactory());

    }

    // Populates the LookupController's toolList with mock tools with unique ids and addresses
    private void createToolList() {

        for (int i = 0; i < 25; i++) {
            if(i < 5) {
                Tool newTool = new Tool("Hammer", Integer.toString(i*236), Integer.toString(i));
                toolList.add(newTool);
            }
            else if(i >= 5 && i <= 13) {
                Tool newTool = new Tool("Screwdriver", Integer.toString(i*72+2), Integer.toString(i));
                toolList.add(newTool);
            }
            else {
                Tool newTool = new Tool("Drill", Integer.toString(i*11+1), Integer.toString(i));
                toolList.add(newTool);
            }

        }
    }

    // Searches for a tool by name, address, or id according to which radio button is selected, returning all tools
    // that match the search criteria
    public ArrayList<Tool> searchTools(String searchText) {

        ArrayList<Tool> foundTools = new ArrayList<>();

        for (Tool tool: toolList) {
            if(radioByName.isSelected() && tool.getName().equalsIgnoreCase(searchText)) {
                foundTools.add(tool);
            }
            else if(radioByAddress.isSelected() && tool.getAddress().equalsIgnoreCase(searchText)) {
                foundTools.add(tool);
            }
            else if(radioByID.isSelected() && tool.getId().equalsIgnoreCase(searchText)) {
                foundTools.add(tool);
            }
        }

        return foundTools;
    }

    public void search(ActionEvent actionEvent) {
        toolListView.getItems().clear();
        toolListView.refresh();
        toolListView.getItems().addAll(searchTools(searchBox.getText()));
    }

    public void gotoMainMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = null;
        Parent root = null;

        if (actionEvent.getSource() == btnBack) {
            stage = (Stage) btnBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("res/fxml/admin.fxml"));
        } else {
            stage = null;
            root = null;
        }

        // create new scene with stage and root
        if (stage != null && root != null) {
            Scene scene = new Scene(root, 640, 480);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Stage or root is null!");
        }
    }
}
