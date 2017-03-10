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
import java.util.HashMap;

/**
 * Created by mwhar on 1/13/2017.
 */
public class LookupController {
    public Button btnScan;
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
    private Button backButton;

    private ArrayList<Tool> toolList;
    private String hostname = "169.254.126.52";

    @FXML
    // The initialize() method is called after the constructor and all the components inside of the LookupController class
    // with the @FXML tag are injected with their proper values. Once this is done we can edit the properties of each
    // component.
    private void initialize() {
        toolList = new ArrayList<>();
        toolListView.setCellFactory(new ToolCellFactory());
    }

    // Searches for a tool by name, address, or id according to which radio button is selected, returning all tools
    // that match the search criteria
    private void getToolList() {
        ServerRequest serverRequest = new ServerRequest();
        JSONdecoder responseDecoder = new JSONdecoder();
        HashMap<String, String> POSTdata = new HashMap<>();
        POSTdata.put("searchField", "name");
        POSTdata.put("searchValue", "hammer");

        String response = serverRequest.getResponseFromRequest("tool-handling/lookup-tool.php", POSTdata);
        responseDecoder.decodeJSONToolResponse(response);
    }

    public ArrayList<Tool> searchTools(String searchText) {

        ServerRequest serverRequest = new ServerRequest();
        JSONdecoder responseDecoder = new JSONdecoder();

        HashMap<String, String> POSTdata = new HashMap<>();
        ArrayList<Tool> foundTools = new ArrayList<>();


        if (radioByName.isSelected()) {
            POSTdata.put("searchField", "name");
            POSTdata.put("searchValue", searchText.toLowerCase());

            String response = serverRequest.getResponseFromRequest("tool-handling/lookup-tool.php", POSTdata);
            if (response != null && !response.isEmpty())
                foundTools = responseDecoder.decodeJSONToolResponse(response);
        } else if (radioByAddress.isSelected()) {
            POSTdata.put("searchField", "address");
            POSTdata.put("searchValue", searchText.toLowerCase());

            String response = serverRequest.getResponseFromRequest("tool-handling/lookup-tool.php", POSTdata);
            if (response != null && !response.isEmpty())
                foundTools = responseDecoder.decodeJSONToolResponse(response);
        } else if (radioByID.isSelected()) {
            POSTdata.put("searchField", "id");
            POSTdata.put("searchValue", searchText.toLowerCase());

            String response = serverRequest.getResponseFromRequest("tool-handling/lookup-tool.php", POSTdata);
            if (response != null && !response.isEmpty())
                foundTools = responseDecoder.decodeJSONToolResponse(response);
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

        if (actionEvent.getSource() == backButton) {
            stage = (Stage) backButton.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("res/fxml/main_menu.fxml"));
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

    public void startScanning(ActionEvent actionEvent) {
        if(this.radioByID.isSelected()){
            // logic to start the scanner
            ReaderThread tempReader = new ReaderThread(this.hostname, "add_tool", this);
            tempReader.run();
        }else{
            // todo: error message here
        }
    }
}
