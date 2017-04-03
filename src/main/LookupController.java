package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        }
    }
}
