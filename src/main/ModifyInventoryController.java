package main;

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
            }else
                System.out.println("newToolController.submitCallback() returned false");
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

    public void deleteTool(){}

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
