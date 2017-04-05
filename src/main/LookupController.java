package main;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mwhar on 1/13/2017.
 */
public class LookupController {

    @FXML
    /** Used to input the search criteria. */
    private TextField searchBox;

    @FXML
    /** Used to search the SmartTools database by tool name. */
    private RadioButton radioByName;

    @FXML
    /** Used to search the SmartTools database by RFID tag. */
    private RadioButton radioByID;

    @FXML
    /** Used to search the SmartTools database by Address. */
    private RadioButton radioByAddress;

    @FXML
    /** Used to display the results of a search of the SmartTools database. */
    private ListView<Tool> toolListView;

    private ArrayList<Tool> toolList;

    /** Hostname of the RFID reader that will be used to read tool tags. */
    private String hostname = "169.254.126.52";

    /** Handles the response from server requests made by the LookupController.
     *
     *  ArrayList<Tool> handleResponse(String response) - This function takes in the String response after querying
     *  the server to see the response received from the server and handle it accordingly. If the response contains
     *  fail, the appropriate failure message popup window is created and an empty ArrayList of the tool object is
     *  returned. Else if the response was not a failure (1 or more tools matching the criteria was found) the response
     *  is a JSON string that is then parsed to create the tool object(s) received and put into the ArrayList and
     *  returned.
     *
     * @returns Returns an ArrayList containing the tools requested by LookupController.
     */
    private ArrayList<Tool> handleResponse(String response) {
        ArrayList<Tool> foundTools = new ArrayList<>(); // array list to be returned
        JSONdecoder responseDecoder; // JSON decoder, only initialized if the response is a JSON string

        // Check to see if the response is empty or null
        if (response != null && !response.isEmpty()) {
            if (response.contains("fail")) {
                // If the response is failure then alert the user to the specific type of failure encountered
                if (response.equalsIgnoreCase("fail")) { // If the query fails with unknown reason (bad connection, error in server)
                    PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
                    popupWindow.popup();
                } else if (response.equalsIgnoreCase("fail: no_results")) { // If the query fails because no results found
                    PopupWindow popupWindow = new PopupWindow("Lookup Tool", "No tool(s) match the specified criteria");
                    popupWindow.popup();
                } else { // If the query fails with unknown reason (bad connection, error in server, etc.)
                    PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
                    popupWindow.popup();
                }
            } else { // If the response didn't fail decode it and add the requested tools to foundTools
                responseDecoder = new JSONdecoder();
                foundTools = responseDecoder.decodeJSONToolResponse(response);
            }
        } else { // If the query fails with unknown reason (bad connection, error in server)
            PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
            popupWindow.popup();
        }
        return foundTools;
    }

    /** Initializes the tool list view before the rest of the scene is loaded. */
    private void initialize() {
        toolList = new ArrayList<>();
        toolListView.setCellFactory(new ToolCellFactory());
    }

    /** Shows a popup informing the user that they have failed to connect to the RFID reader. */
    public void scannerConnectionError() {
        PopupWindow error = new PopupWindow("Error", "Reader failed to connect!");
        error.popup();
    }

    /** Searches the SmartTools database for a specified tool name, ID, or address and returns the tools listed
     *
     *  void searchTools() - Takes the tool name, ID, or address typed into a search box and makes a request to the
     *  SmartTools database for all tools matching the given criteria. Once all matching tools are found their data is
     *  displayed inside of a ListView cell.
     */
    public void searchTools() {

        // Get the text inside of the search box
        String searchText = searchBox.getText();
        ServerRequest serverRequest = new ServerRequest();

        HashMap<String, String> POSTdata = new HashMap<>();
        ArrayList<Tool> foundTools = new ArrayList<>();

        // Figure out if the user is trying to search by tool name, ID, or address
        if (radioByName.isSelected()) {
            // Add tool name parameters for the request to the database
            POSTdata.put("searchField", "name");
            POSTdata.put("searchValue", searchText.toLowerCase());

            // Send a request to the database to find tools with the given tool name and retrieve its response
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
            // If for some reason none of the radio buttons are selected show a popup error
            PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
            popupWindow.popup();
        }

        // Clear the list view and then add the newly found tools to it
        toolListView.getItems().clear();
        toolListView.refresh();
        toolListView.getItems().addAll(foundTools);
    }

    /** Connects to a RFID reader and scans in the RFID tag of a tool
     *
     *  void scanTool() - Reads the RFID tag in front of the RFID reader and prints out its ID and the number of times
     *  it read the tag.
     *
     *  @throws IOException
     */
    public void scanTool() throws IOException {
        // If the user is searching by ID
        if (this.radioByID.isSelected()) {
            // Create and start a connection to the RFID reader
            ReaderThread myReaderThread = new ReaderThread(this.hostname, "lookup_tool", this);
            myReaderThread.run();
            try {
                // Pause the main thread until the RFID reader is done scanning
                myReaderThread.join();
            } catch (java.lang.InterruptedException ie) {
                System.err.println(ie.getMessage());
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
                // Set the text inside of the search box to the scanned Tag ID
                this.searchBox.setText(resultEpc);
            } catch (NullPointerException npe) {
                System.err.println(npe.getMessage());
            }
        } else {
            PopupWindow error = new PopupWindow("Error", "Must Select ID to scan");
            error.popup();
        }
    }
}
