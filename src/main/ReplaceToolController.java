package main;

import com.impinj.octane.OctaneSdkException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ReplaceToolController - Controller for the AddTool FXML view, implements the functionality of the view.
 */
public class ReplaceToolController {

    /** Used to select the address to assign a tool that is being added. */
    public ComboBox btnAddress;

    /** Used to begin the scanning of a tool. */
    public Button btnScan;

    /** Used to submit the assignment of a tool to a selected address. */
    public Button btnSubmit;

    /** Used to enter the tool name where open toolbox spots are available. */
    public TextField txtToolName;

    /** Used to show the RFID tag EPC for the tool that has been scanned. */
    public TextField txtTagID;

    /** Used to show status messages to user. */
    public Label lblError;

    /** Used to show status messages to user. */
    public Label lblMsg;

    /** Hostname of the RFID reader that will be used to read tool tags. */
    private String hostname = "169.254.126.52";

    /** List of strings containing open addresses for the entered tool name. */
    private ObservableList<String> addressList = FXCollections.observableArrayList();

    @FXML
    private void initialize(){
        // set up autocomplete text field
        TextFields.bindAutoCompletion(this.txtToolName, Main.inventoryList.getToolNames());
    }

    /** Connects to and starts the RFID reader so the user can scan tools.
     *
     *  ReaderThread connectToReader() - Starts a new ReaderThread that has connected to the RFID reader and then
     *  returns the ReaderThread. Through the ReaderThread information about the RFID tagged tools can be read in.
     *
     *  @return Returns a ReaderThread that is connected to the RFID reader and allows for scanning tools.
     *  @throws OctaneSdkException
     */
    private ReaderThread connectToReader() throws OctaneSdkException{
        ReaderThread reader = new ReaderThread(this.hostname, "add_tool", this);
        reader.run();
        return reader;
    }

    /** Shows a popup informing the user that they have failed to connect to the RFID reader. */
    public void scannerConnectionError() {
        PopupWindow error = new PopupWindow("Error", "Reader failed to connect!");
        error.popup();
    }

    /** Compiles a list of open tool address spaces and then fills the btnAddress ComboBox with them.
     *
     *  void searchOpenAddresses() - Searches the SmartTools database for open tool addresses for "tool name", adds them
     *  to a list of open addresses, and then adds the found open addresses to the btnAddress ComboBox for the user to
     *  select from.
     *
     */
    public void searchOpenAddresses() {
        // Create new server request and JSON decoder objects
        ServerRequest serverRequest = new ServerRequest();
        JSONdecoder responseDecoder = new JSONdecoder();

        // Create a hashmap to store the data that we would like to query the database with
        HashMap<String, String> POSTdata = new HashMap<>();

        // Create a list to hold the open addresses that we find
        ArrayList<String> foundAddresses = new ArrayList();

        // Add the tool name entered by the user as a query parameter
        POSTdata.put("toolname", txtToolName.getText());

        // Send a the server request and retrieve the server response as a string
        String response = serverRequest.getResponseFromRequest("tool-handling/open-addresses.php", POSTdata);

        // If the response came back with open address data we add that data to the foundAddresses list and then to
        // the  btnAddress ComboBox
        if (response != null && !response.isEmpty()) {
            foundAddresses = responseDecoder.decodeJSONAddressResponse(response);
            btnAddress.getItems().clear();
            addressList.addAll(foundAddresses);
            btnAddress.setItems(addressList);
        }
    }

    /** Attempts to insert the a new tool into the SmartTools database.
     *
     *  void submitTool() - Grabs data the user has entered through the GUI and creates queries using a HashMap. Sends
     *  request to add a new tool to the SmartTools database.
     *
     **/
    public void submitTool() {
        System.out.println("addToolSubmit()");

        // Read string from Tag ID textfield
        String tagID = txtTagID.getText();

        // Path to database tool insertion php script
        String path = "tool-handling/insert-tool.php";
        String selectedAddress;

        // Create HashMap to store values
        HashMap<String, String> queryValues = new HashMap(3);

        // Get the user selected tool address value
        if (btnAddress.getValue() != null) {
            selectedAddress = btnAddress.getValue().toString();
        } else {
            PopupWindow error = new PopupWindow("Error", "Please Select an Address");
            error.popup();
            return;
        }

        // Get the user entered tool name
        String selectedToolName = txtToolName.getText();

        // If the user selected address and entered tool name are valid then add them as query parameters to the HashMap
        if (selectedAddress != null && !selectedToolName.isEmpty()) {
            queryValues.put("tagID", tagID);
            queryValues.put("toolName", selectedToolName);
            queryValues.put("toolAddress", selectedAddress);
            System.out.printf("tag ID: %s\nTool Name: %s\nAddress: %s\n", queryValues.get("tagID"), queryValues.get("toolName"), queryValues.get("toolAddress"));
        } else {
            PopupWindow error = new PopupWindow("Error", "Either address or tool name not selected.");
            error.popup();
        }

        // Create and send the query request with our parameters and retrieve the server response
        ServerRequest request = new ServerRequest();
        String response = request.getResponseFromRequest(path, queryValues);

        // If the server response is success alert the user
        if (response.equalsIgnoreCase("success")) {
            txtTagID.clear();
            txtToolName.clear();
            PopupWindow popupWindow = new PopupWindow("ADD TOOL", "Successfully added tool");
            popupWindow.popup();
        } else if (response.equalsIgnoreCase("fail")) {
            PopupWindow popupWindow = new PopupWindow("ADD TOOL", "Failed to insert tool");
            popupWindow.popup();
        } else if (response.contains("fail:")) {
            String errorCode = response.split(" ")[1];
            // If the server responds with a specific failure alert the user of the specific reason
            if (errorCode.equalsIgnoreCase("address_taken")) {
                PopupWindow popupWindow = new PopupWindow("ADD TOOL", "Failed to insert tool: Address in use");
                popupWindow.popup();
            } else if (errorCode.equalsIgnoreCase("id_in_use")) {
                PopupWindow popupWindow = new PopupWindow("ADD TOOL", "Failed to insert tool: Tool ID in use");
                popupWindow.popup();
            } else {
                PopupWindow popupWindow = new PopupWindow("ADD TOOL", "Failed to insert tool");
                popupWindow.popup();
            }

        } else {
            PopupWindow popupWindow = new PopupWindow("ADD TOOL", "Failed to insert tool");
            popupWindow.popup();
        }


        System.out.println(response);
    }

    /** Connects to a RFID reader and scans in the RFID tag of a tool
     *
     *  void scanTool() - Reads the RFID tag in front of the RFID reader and displays its ID in the GUI.
     *
     **/
    public void scanTool() throws IOException {
        try{
            // Attempt to connect to the RFID reader
            ReaderThread myReaderThread = this.connectToReader();
            try {
                // Pause the main thread until the RFID reader is done scanning
                myReaderThread.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            myReaderThread.stopReader();

            // Get the tool Tag ID and the number of times it was scanned from the RFID reader
            HashMap<String, Integer> tagValues = myReaderThread.getTagValues();

            int curMax = 0;
            String resultEpc = "Didn't read anything...";
            try {
                // Go through all of the tool Tag IDs and find the tag that was read the most
                for (String key : tagValues.keySet()) {
                    System.out.printf("Tag ID: %s\nCount: %d\n", key, tagValues.get(key));
                    if (tagValues.get(key) > curMax) {
                        resultEpc = key;
                        curMax = tagValues.get(key);
                    }
                }
                // Set the Tag textfield to the most read tag ID
                this.txtTagID.setText(resultEpc);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }catch(OctaneSdkException ose){
            ose.printStackTrace();
        }
    }

}
