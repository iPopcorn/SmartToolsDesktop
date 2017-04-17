package main;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Taylor on 4/5/2017.
 *
 * ModifyInventoryController - This is the controller class for modify_inventory.fxml It holds the logic for the Modify
 * Inventory module.
 */
public class ModifyInventoryController {
    public ListView toolListView;
    public Button btnBack;
    public Button btnRemove;
    public Button btnAdd;
    private ArrayList<Tool> toolList;
    private HashMap<String, String> toolData;

    @FXML
    private void initialize(){
        this.toolList = new ArrayList<>();
        toolListView.setCellFactory(new InventoryCellFactory());
        this.populateListView();
    }

    /** Opens a window that allows the user to add a new tool to the system.*/
    public void newTool(){
        System.out.println("ModifyInventoryController.newTool() Begin");
        try{
            Parent root;
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("res/fxml/new_tool_window.fxml"));
            root = fxmlLoader.load();
            NewToolController newToolController = fxmlLoader.getController();

            Stage newToolWindow = new Stage();
            newToolWindow.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root, 400, 200);

            newToolWindow.setScene(scene);
            newToolWindow.setResizable(false);
            newToolWindow.showAndWait();

            boolean success = newToolController.submitCallback();
            if(success){
                System.out.println("newToolController.submitCallback() returned true");
                toolData = newToolController.getToolData();
            }else{
                System.out.println("newToolController.submitCallback() returned false");
                return;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        String toolName = toolData.get("name");
        String toolDrawer = toolData.get("drawer");
        HashMap<String, String> POSTdata = new HashMap<>();
        POSTdata.put("action","insert-to-inventory");
        POSTdata.put("tool_name", toolName);
        POSTdata.put("drawer", toolDrawer);
        ServerRequest request = new ServerRequest();
        String responseString = request.getResponseFromRequest("/tool-handling/modify-inventory.php", POSTdata);
        System.out.println("responseString = " + responseString);

        System.out.println("ModifyInventoryController.newTool() End");
    }

    /** Deletes the selected tool from the system entirely*/
    public void deleteTool(){
        System.out.println("ModifyInventoryController.deleteTool() Begin");

        // Get the tool object from the selection in the list view.
        ObservableList selection = toolListView.getSelectionModel().getSelectedItems();
        Tool selectedTool = (Tool) selection.get(0);

        // Set up strings for the query
        StringBuilder drawerStringBuilder = new StringBuilder();
        StringBuilder sectionStringBuilder = new StringBuilder();

        // get the chars from the address
        char drawer = selectedTool.getAddress().charAt(2);
        char sectionChars[] = new char[2];
        sectionChars[0] = selectedTool.getAddress().charAt(3);
        sectionChars[1] = selectedTool.getAddress().charAt(4);

        // add the chars to the strings
        drawerStringBuilder.append(drawer);
        sectionStringBuilder.append(sectionChars[0]);
        sectionStringBuilder.append(sectionChars[1]);

        // get all the strings ready to be put in the HashMap
        String drawerString = drawerStringBuilder.toString();
        String sectionString = sectionStringBuilder.toString();
        String nameString = selectedTool.getName();

        // prepare the data to be sent to the server
        HashMap<String, String> POSTdata = new HashMap<>();
        POSTdata.put("action", "delete-from-inventory");
        POSTdata.put("tool_name", nameString);
        POSTdata.put("drawer", drawerString);
        POSTdata.put("section", sectionString);

        // send the data to the server and store the response in a string
        ServerRequest request = new ServerRequest();
        String response = request.getResponseFromRequest("tool-handling/modify-inventory.php", POSTdata);

        // handle the response based on the value of the string
        if(response.equalsIgnoreCase("success")){
            PopupWindow successPopup = new PopupWindow("Success", "Tool deleted successfully");
            successPopup.popup();
        }else{
            PopupWindow failPopup = new PopupWindow("Fail", "Error Processing Request");
            failPopup.popup();
        }
        System.out.println("ModifyInventoryController.deleteTool() End");
    }

    private void populateListView(){
        System.out.println("ModifyInventoryController.populateListView() Begin");

        // Query database for a list of all the tools currently in the inventory
        // This query will grab from the belongs_here column of the address table
        ServerRequest serverRequest = new ServerRequest();

        HashMap<String, String> POSTdata = new HashMap<>();
        POSTdata.put("action","get-tool-list");

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
        System.out.println("ModifyInventoryController.handleResponse() Begin");
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
        System.out.println("ModifyInventoryController.handleResponse() End");
        return foundTools;
    }
}
