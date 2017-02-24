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

    /**
     * Initializes the controller class.
     */

    
    public Button btnDelToolScan;
    public Button btnDelToolBack;
    public Button btnDelToolDelete;
    public Button btnDelToolRefresh;
    public ListView delToolBoxNumb;
    public MenuButton delToolSelectTool;
    public TextField delToolTagID;
    public Label lblDelToolError;
    public TextField txtDelToolID;
    public TextField txtDelToolName;
    public TextField txtDelToolAddress;

    private HashMap<String, String> addressMap;
    private ReaderThread Reader;
    private String hostname = "169.254.126.52";
    private ArrayList<Tool> currentTools;
    private int toolboxNum;
    
    private void getToolList (int toolNumb)
    {
        ServerRequest myRequest = new ServerRequest();
        JSONdecoder requestDecoder = new JSONdecoder();
        HashMap<String, String> data = new HashMap<>();
        data.put("searchField","toolbox");
        data.put("searchValue",Integer.toString(this.toolboxNum));
        String response = myRequest.getResponseFromRequest("tool-handling/lookup-tool.php", data);
        currentTools = requestDecoder.decodeJSONToolResponse(response);
    }
    
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
    }
    
    public void deleteTool (ActionEvent ae)
    {
        String tool_id = "";
        ServerRequest serverRequest = new ServerRequest();
        HashMap<String, String> POSTdata = new HashMap<>();
                
        POSTdata.put("tagID", tool_id);
        String response = serverRequest.getResponseFromRequest("tool-handling/delete-tool.php", POSTdata);
        
    }


}
