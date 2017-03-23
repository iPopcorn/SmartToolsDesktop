/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sam
 */
public class DeleteToolController {


    public Button btnDelToolScan;
    public Button btnDelToolBack;
    public Button btnDelToolDelete;
    public Label lblDelToolError;
    public TextField txtDelToolID;
    public TextField txtDelToolName;
    public TextField txtDelToolAddress;
    public Label lblDelToolMsg;
    private String hostname = "169.254.126.52";
    private Tool resultTool;
    
    public void openMainMenu(ActionEvent actionEvent) throws IOException
    {
        Stage stage = null;
        Parent root = null;

        if(actionEvent.getSource() == btnDelToolBack){
            stage = (Stage) btnDelToolBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("res/fxml/main_menu.fxml"));
        }else{
            stage = null;
            root = null;
        }

        // create new scene with stage and root
        if(stage != null && root != null){
            Scene scene = new Scene(root, 640, 480);
            stage.setScene(scene);
            stage.show();
        }else{
            System.out.println("Stage or root is null!");
        }
    }
    
    public void scanTool()
    {
        ReaderThread tempReader = new ReaderThread(this.hostname, "delete_tool", this);
        tempReader.run();

        try{
            tempReader.join();
        }catch(InterruptedException ie){
            System.out.println(ie);
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
    
    public void deleteTool ()
    {
        System.out.println("DeleteToolController.deleteTool() begin");
        ServerRequest serverRequest = new ServerRequest();
        HashMap<String, String> POSTdata = new HashMap<>();
                
        POSTdata.put("tagID", this.resultTool.getId());
        String response = serverRequest.getResponseFromRequest("tool-handling/delete-tool.php", POSTdata);

        if(response.equalsIgnoreCase("success")){
            // this.lblDelToolMsg.setText("Delete Success");
            PopupWindow successPopup = new PopupWindow("Success", "Tool Successfully Deleted!");
            successPopup.popup();
        }else{
            // this.lblDelToolError.setText("Delete Fail");
            PopupWindow failPopup = new PopupWindow("Fail", "Tool Deletion Failed!");
            failPopup.popup();
        }
        System.out.println("DeleteToolController.deletetool() end");

    }


}
