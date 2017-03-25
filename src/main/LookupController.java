package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mwhar on 1/13/2017.
 */
public class LookupController {
    public Button btnScan;
    public Label lblError;
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
    @FXML
    private Button searchButton;
    @FXML
    private Label searchByName;
    @FXML
    private Label searchByID;
    @FXML
    private Label searchByAddress;

    private ArrayList<Tool> toolList;
    private final String hostname = "169.254.126.52";

    @FXML
    // The initialize() method is called after the constructor and all the components inside of the LookupController class
    // with the @FXML tag are injected with their proper values. Once this is done we can edit the properties of each
    // component.
    private void initialize() {
        toolList = new ArrayList<>();
        toolListView.setCellFactory(new ToolCellFactory());

        radioByName.getStyleClass().add("radio-button");
        radioByID.getStyleClass().add("radio-button");
        radioByAddress.getStyleClass().add("radio-button");

        backButton.getStyleClass().add("smallButton");
        btnScan.getStyleClass().add("smallButton");
        searchButton.getStyleClass().add("smallButton");

        searchBox.getStyleClass().add("searchBox");

    }

    public void scannerConnectionError() {
        PopupWindow error = new PopupWindow("Error", "Reader failed to connect!");
        error.popup();
    }

    private void showError(String errorMessage) {
        this.lblError.setText(errorMessage);
    }

    // Populates the LookupController's toolList with mock tools with unique ids and addresses
    private void createToolList() {

        for (int i = 0; i < 25; i++) {
            if (i < 5) {
                Tool newTool = new Tool("Hammer", Integer.toString(i * 236), Integer.toString(i));
                toolList.add(newTool);
            } else if (i >= 5 && i <= 13) {
                Tool newTool = new Tool("Screwdriver", Integer.toString(i * 72 + 2), Integer.toString(i));
                toolList.add(newTool);
            } else {
                Tool newTool = new Tool("Drill", Integer.toString(i * 11 + 1), Integer.toString(i));
                toolList.add(newTool);
            }

        }
    }

//    ServerRequest serverRequest;
//    JSONdecoder responseDecoder;
//
//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        /*
//         * TEST
//         */
//
//        serverRequest = new ServerRequest();
//        responseDecoder = new JSONdecoder();
//        HashMap<String, String> POSTdata = new HashMap<>();
//        POSTdata.put("searchField", "name");
//        POSTdata.put("searchValue", "hammer");
//
//        String response = serverRequest.getResponseFromRequest("tool-handling/lookup-tool.php", POSTdata);
//        responseDecoder.decodeJSONToolResponse(response);
//
//
//        // set stage
//        myStage = primaryStage;
//
//        // create root objects for scene, connecting them to fxml files
//        Parent mainMenuRoot = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
//        //Parent lookupToolRoot = FXMLLoader.load(getClass().getResource("lookup_tool.fxml"));
//        mainMenu = new Scene(mainMenuRoot, 640, 480);
//        myStage.setTitle("Desktop App");
//        myStage.setScene(mainMenu);
//        myStage.show();

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

        HashMap<String, String> POSTdata = new HashMap<>();
        ArrayList<Tool> foundTools = new ArrayList<>();


        if (radioByName.isSelected()) {
            POSTdata.put("searchField", "name");
            POSTdata.put("searchValue", searchText.toLowerCase());

            String response = serverRequest.getResponseFromRequest("tool-handling/lookup-tool.php", POSTdata);

            foundTools = handleResponse(response);
        } else if (radioByAddress.isSelected()) {
            POSTdata.put("searchField", "address");
            POSTdata.put("searchValue", searchText.toLowerCase());

            String response = serverRequest.getResponseFromRequest("tool-handling/lookup-tool.php", POSTdata);

            foundTools = handleResponse(response);
        } else if (radioByID.isSelected()) {
            POSTdata.put("searchField", "id");
            POSTdata.put("searchValue", searchText.toLowerCase());

            String response = serverRequest.getResponseFromRequest("tool-handling/lookup-tool.php", POSTdata);

            foundTools = handleResponse(response);
        } else {
            PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
            popupWindow.popup();
        }

        return foundTools;
    }


    /**
     * ArrayList<Tool> handleResponse(String response)
     * This function takes in the String response after querying the server to see the response
     * received from the server and handle it accordingly. If the response contains fail, the appropriate
     * failure message popup window is created and an empty ArrayList of the tool object is returned.
     * Else if the response was not a failure (1 or more tools matching the criteria was found) the
     * response is a JSON string that is then parsed to create the tool object(s) received and
     * put into the ArrayList and returned.
     */
    private ArrayList<Tool> handleResponse(String response) {
        ArrayList<Tool> foundTools = new ArrayList<>(); // array list to be returned
        JSONdecoder responseDecoder; // JSON decoder, only initialized if the response is a JSON string

        if (response != null && !response.isEmpty()) {
            if (response.contains("fail")) { // if failure code
                if (response.equalsIgnoreCase("fail")) { // if the query fails with unknown reason (bad connection, error in server)
                    PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
                    popupWindow.popup();
                } else if (response.equalsIgnoreCase("fail: no_results")) { // if the query fails because no results found
                    PopupWindow popupWindow = new PopupWindow("Lookup Tool", "No tool(s) match the specified criteria");
                    popupWindow.popup();
                } else { // if the query fails with unknown reason (bad connection, error in server)
                    PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
                    popupWindow.popup();
                }

            } else { // if the response isn't a fail message
                responseDecoder = new JSONdecoder();
                foundTools = responseDecoder.decodeJSONToolResponse(response);
            }
        } else { // if the query fails with unknown reason (bad connection, error in server)
            PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
            popupWindow.popup();
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

    public void scanTool(ActionEvent actionEvent) throws IOException {
        if (this.radioByID.isSelected()) {
            ReaderThread myReaderThread = new ReaderThread(this.hostname, "lookup_tool", this);
            myReaderThread.run();
            try {
                myReaderThread.join();
            } catch (java.lang.InterruptedException ie) {
                System.out.println(ie);
            }
            myReaderThread.stopReader();
            HashMap<String, Integer> tagValues = myReaderThread.getTagValues();
            int curMax = 0;
            String resultEpc = "Didn't read anything...";

            try {
                for (String key : tagValues.keySet()) {
                    System.out.printf("Tag ID: %s\nCount: %d\n", key, tagValues.get(key));
                    if (tagValues.get(key) > curMax) {
                        resultEpc = key;
                        curMax = tagValues.get(key);
                    }
                }
                this.searchBox.setText(resultEpc);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {
            PopupWindow error = new PopupWindow("Error", "Must Select ID to scan");
            error.popup();
            /*System.out.println("Must Select ID to scan");
            this.showError("Error: ID Not Selected!");*/
        }
    }
}
