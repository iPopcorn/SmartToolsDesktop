package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Taylor on 4/5/2017.
 */
public class ModifyInventoryController {
    public ListView toolListView;
    public Button btnBack;
    public Button btnRemove;
    public Button btnAdd;
    private ArrayList<Tool> toolList;

    @FXML
    private void initialize(){
        this.toolList = new ArrayList<>();
        toolListView.setCellFactory(new ToolCellFactory());
        this.populateListView();
    }

    private void populateListView(){
        System.out.println("ModifyInventoryController.populateListView() Begin");

        // Query database for a list of all the tools currently in the inventory
        // This query will grab from the belongs_here column of the address table
        ServerRequest serverRequest = new ServerRequest();

        HashMap<String, String> POSTdata = new HashMap<>();
        ArrayList<Tool> foundTools = new ArrayList<>();

        String response = serverRequest.getResponseFromRequest("tool-handling/tool-list.php",POSTdata);
        this.toolList = handleResponse(response);

        // update list view
        this.toolListView.getItems().clear();
        this.toolListView.refresh();
        this.toolListView.getItems().addAll(this.toolList);
        System.out.println("ModifyInventoryController.populateListView() End");
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
                foundTools = responseDecoder.decodeJSONToolListResponse(response);
            }
        } else { // if the query fails with unknown reason (bad connection, error in server)
            PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
            popupWindow.popup();
        }
        return foundTools;
    }
}
