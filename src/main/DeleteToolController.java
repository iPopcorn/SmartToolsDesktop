/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * DeleteToolController - Controller for the delete_tool.fxml, implements the functionality of the view.
 */
public class DeleteToolController {

    /** Used to start the scanner, calls scanTool() when pressed*/
    public Button btnDelToolScan;

    /** DEPRECATED - Used to navigate back to the main menu, calls openMainMenu() when pressed*/
    public Button btnDelToolBack;

    /** Used to delete the selected tool, calls deleteTool() when pressed*/
    public Button btnDelToolDelete;

    /** DEPRECATED - Used to display error messages in the GUI*/
    public Label lblDelToolError;

    /** Used in scanTool() displays the ID of the tool that was scanned*/
    public TextField txtDelToolID;

    /** Used in scanTool() displays the name of the tool that was scanned*/
    public TextField txtDelToolName;

    /** Used in scanTool() displays the address of the tool that was scanned*/
    public TextField txtDelToolAddress;

    /** DEPRECATED - Used to display messages in the GUI*/
    public Label lblDelToolMsg;

    /** ip address of the reader, used in scanTool()*/
    private String hostname = "169.254.126.52";

    /** Holds the information of the tool that was scanned. Used in scanTool() and deleteTool()*/
    private Tool resultTool;

    /** Displays a popup error message when reader fails to connect
     *
     * void scannerConnectionError() - used to display a PopupWindow that the user must dismiss in the case that the
     * system cannot connect to the reader
     */
    public void scannerConnectionError() {
        PopupWindow error = new PopupWindow("Error", "Reader failed to connect!");
        error.popup();
    }

    /** Starts the reader and reads an RFID tag into the system
     *
     *  void scanTool() - This method connects to a reader and reads a tag ID into the system. It uses the tag ID to
     *  query the database. If it can't find a tool associated with the ID in the database, then it will display a
     *  popup error dialog. Otherwise it will display the information about the associated tool in the GUI.
     */
    public void scanTool()
    {
        ReaderThread tempReader = new ReaderThread(this.hostname, "delete_tool", this);
        tempReader.run();

        try{
            tempReader.join();
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }
        tempReader.stopReader();
        HashMap<String, Integer> tagValues = tempReader.getTagValues();
        // get tag with highest count
        int curMax = 0;
        String toolID = "Didn't read anything...";
        for(String key: tagValues.keySet()){
            if(tagValues.get(key) > curMax){ // if current count > current max
                toolID = key;
                curMax = tagValues.get(key);
            }
        }
        // use toolID to get tool info
        ServerRequest request = new ServerRequest();
        HashMap<String, String> responseMap = new HashMap<>();
        JSONdecoder decoder = new JSONdecoder();
        ArrayList<Tool> toolList;

        responseMap.put("searchField", "id");
        responseMap.put("searchValue", toolID);
        String responseString = request.getResponseFromRequest("/tool-handling/lookup-tool.php", responseMap);
        System.out.println("responseString = " + responseString);

        if(responseString != null && !responseString.isEmpty()){ // make sure we actually have a string
            if(responseString.contains("fail")){ // if it is a failure message
                if(responseString.equalsIgnoreCase("fail")){ // generic fail
                    PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
                    popupWindow.popup();
                }else if(responseString.equalsIgnoreCase("fail: no_results")){ // tool not found fail
                    PopupWindow popupWindow = new PopupWindow("Lookup Tool", "No tool(s) match the specified criteria");
                    popupWindow.popup();
                }else{ // failsafe fail
                    PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
                    popupWindow.popup();
                }
            }else{ // if it is a success
                toolList = decoder.decodeJSONToolResponse(responseString);
                this.resultTool = toolList.get(0);

                // set tool info into text field
                txtDelToolID.setText(resultTool.getId());
                txtDelToolAddress.setText(resultTool.getAddress());
                txtDelToolName.setText(resultTool.getName());
            }
        }
    }

    /** Deletes the selected tool from the database
     *
     *  void deleteTool() - uses the resultTool object to query the database and remove the associated object from the
     *  'Tools' table of the database.
     */
    public void deleteTool ()
    {
        System.out.println("DeleteToolController.deleteTool() begin");
        ServerRequest serverRequest = new ServerRequest();
        HashMap<String, String> POSTdata = new HashMap<>();
                
        POSTdata.put("tagID", this.resultTool.getId());
        String response = serverRequest.getResponseFromRequest("tool-handling/delete-tool.php", POSTdata);

        if(response.equalsIgnoreCase("success")){
            PopupWindow successPopup = new PopupWindow("Success", "Tool Successfully Deleted!");
            successPopup.popup();
        }else{
            PopupWindow failPopup = new PopupWindow("Fail", "Tool Deletion Failed!");
            failPopup.popup();
        }
        System.out.println("DeleteToolController.deletetool() end");

    }


}
