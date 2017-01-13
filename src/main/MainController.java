/**
 * MainController.java is the class that holds all the logic connected to the GUIs.
 *
 * TODO: Keep description of MainController.java updated
 * TODO: addTool needs to handle when a duplicate tagID is used, confirm overwrite from user
 * **/

package main;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.xml.ws.Action;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainController {

    private ReaderThread currentReader;
    private String hostname = "169.254.126.52";
    private ArrayList<String> genReportTagList;
    private HashMap<String, String> addressMap;
    private ArrayList<Triplet<String, String, Boolean>> comparisonMap;

    public Button btnAddToolRefresh;
    public Button btnOpenLookup;
    public Button btnOpenAddTool;
    public Button btnOpenReport;
    public Button btnOpenAdmin;
    public Button btnLookupBack;
    public Button btnGenReportBack;
    public Button btnAddToolBack;
    public Button btnAdminBack;
    public Button btnAddToolSubmit;
    public Button btnSwitchAddTool;

    public MenuButton btnAddToolAddress;
    public MenuButton btnAddToolName;
    public TextField txtTagID;

    public void genReportStartScanning(){
        System.out.printf("genReportStartScanning()");
        this.currentReader = new ReaderThread(this.hostname, "generate_report");
    }

    // TODO: optimize comparison algorith in genReportStopScanning()
    public void genReportStopScanning(){
        System.out.printf("genReportStopScanning()");
        this.currentReader.stopReader();

        // grab tag list from reader
        this.genReportTagList = this.currentReader.getGenReportTagValues();

        // compare tag list with address map
        this.addressMap = this.getAddressMapFromDB();
        this.comparisonMap = new ArrayList<>();
        Triplet<String, String, Boolean> temp;

        // populate comparisonMap, setting every triplet to false
        for (String key:this.addressMap.keySet()) { // for each tagID in the tagList
            temp = new Triplet(key, this.addressMap.get(key), false);
            comparisonMap.add(temp);
        }
        // for each id in the tag list find id in comparisonMap and mark True
        // todo: this is O(n^2), we need to optimize it.
        // todo: this doesn't handle extra tools
        for(String tagId : this.genReportTagList){
            for(Triplet item : comparisonMap){
                if(tagId.equals(item.getFirst())){
                    item.setThird(true);
                }
            }
        }
    }

    /**
     * getAddressMapFromDB() grabs the address map from the database
     * TODO: implement logic for the getAddressMapFromDB() method.
     * **/
    private HashMap<String, String> getAddressMapFromDB() {
        // temporary logic for the sake of finishing generate report logic.
        HashMap<String, String> tempMap = new HashMap<>();
        tempMap.put("EPC1", "1A1");
        tempMap.put("EPC2", "1A2");

        return tempMap;
    }

    /**
     * changeScene() is the function that is called when a button on the main menu is pressed.
     * changeScene() takes 1 parameter which is an ActionEvent object. The ActionEvent's getSource()
     * method is used to find out which button was clicked. Depending on which button is clicked, a different root
     * is used for the scene that is created.
     * **/
    public void changeScene(ActionEvent actionEvent) throws IOException {
        Stage stage;
        Parent root;
        if(actionEvent.getSource() == btnOpenLookup){
            // get reference to button's stage
            stage = (Stage) btnOpenLookup.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("lookup_tool.fxml"));
        }else if(actionEvent.getSource() == btnOpenAddTool){
            stage = (Stage) btnOpenAddTool.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("add_tool.fxml"));
        }else if(actionEvent.getSource() == btnOpenReport){
            stage = (Stage) btnOpenReport.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("generate_report.fxml"));
        }else if(actionEvent.getSource() == btnOpenAdmin) {
            stage = (Stage) btnOpenAdmin.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("admin.fxml"));
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

    public void openMainMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = null;
        Parent root = null;

        if(actionEvent.getSource() == btnLookupBack){
            // get reference to button's stage
            stage = (Stage) btnLookupBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        }else if(actionEvent.getSource() == btnAddToolBack){ //TODO: btnAddToolBack is not being read from add_tool.fxml
            stage = (Stage) btnAddToolBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
            System.out.println("Back Button Pressed in Add Tool");
        }else if(actionEvent.getSource() == btnGenReportBack){
            stage = (Stage) btnGenReportBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        }else if(actionEvent.getSource() == btnAdminBack) {
            stage = (Stage) btnAdminBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        }/*else if(actionEvent.getSource() == btnAddToolBack){
            stage = (Stage) btnAddToolBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        }*/else{
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

    /**
     * addToolSubmit() is the function that is called when the submit button of the add tool screen is pressed.
     * This function grabs the data from the GUI and places it in a HashMap.
     * **/
    public void addToolSubmit(){
        System.out.println("addToolSubmit()");

        // read string from text field
        String tagID = txtTagID.getText();

        String path = "tool-handling/insert-tool.php";

        // create HashMap to store values
        HashMap<String,String> queryValues = new HashMap(3);

        // get list of items for the tools and the addresses
        // TODO: address list should be generated based on tool list selection
        ObservableList<MenuItem> toolList = btnAddToolName.getItems();
        ObservableList<MenuItem> addressList = btnAddToolAddress.getItems();
        CheckMenuItem temp, lastSelectedTool = null, lastSelectedAddress = null;

        // iterate through the list of tool names, only used the last selected tool
        // TODO: think of some way to limit user to select only 1 tool
        for(int i = 0; i < toolList.size(); i++){
            temp = (CheckMenuItem) toolList.get(i);

            if(temp.isSelected()) {
                //System.out.printf("%s is selected\n", temp.getId());
                lastSelectedTool = temp;
            }
        }

        // iterate through address list, only use the last selected address
        for(int i = 0; i < addressList.size(); i++){
            temp = (CheckMenuItem) addressList.get(i);

            if(temp.isSelected()) {
                //System.out.printf("%s is selected\n", temp.getId());
                lastSelectedAddress = temp;
            }
        }

        if(lastSelectedAddress != null && lastSelectedTool != null){
            queryValues.put("tagID", tagID);
            queryValues.put("toolName", lastSelectedTool.getText());
            queryValues.put("toolAddress", lastSelectedAddress.getText());
            System.out.printf("tag ID: %s\nTool Name: %s\nAddress: %s\n", queryValues.get("tagID"), queryValues.get("toolName"), queryValues.get("toolAddress"));
        }else{
            // TODO: add error label for non-selection
            System.out.printf("Either address or tool name not selected.");
        }

        ServerRequest request = new ServerRequest();
        request.getResponseFromRequest(path, queryValues);
        //return queryValues;
    }

    public void addToolRefresh(ActionEvent actionEvent) throws IOException {
        ReaderThread myReaderThread = new ReaderThread(this.hostname, "add_tool");
        myReaderThread.run();
        while(myReaderThread.isAlive()){}
        HashMap<String, Integer> tagValues = myReaderThread.getTagValues();
        int curMax = 0;
        String resultEpc = "Didn't read anything...";
        for(String key : tagValues.keySet()){
            System.out.printf("Tag ID: %s\nCount: %d\n", key, tagValues.get(key));
            if(tagValues.get(key) > curMax)
                resultEpc = key;
        }
        this.txtTagID.setText(resultEpc);
    }

    public void switchToAddTool(ActionEvent actionEvent) throws IOException {


        if( actionEvent.getSource() == btnSwitchAddTool)
        {
            System.out.println("btnSwitchAddTool");
            System.out.println(btnSwitchAddTool.getScene().getRoot().getId().toString());
            for(Node node: btnSwitchAddTool.getScene().getRoot().getChildrenUnmodifiable())
            {
                if(node.getId() != null) {
                    System.out.println(node.getId());
                    if(node.getId().matches("adminPane")) {
                        System.out.println("adminPane");
                        Parent root = FXMLLoader.load(getClass().getResource("add_tool.fxml"));
                        ((Pane) node).getChildren().add(root);
                    }
                }
            }
        }

    }

    public void genReportDisplay(ActionEvent actionEvent) {
    }

    public void genReportCSV(ActionEvent actionEvent) {
    }

    public void genReportEmail(ActionEvent actionEvent) {
    }
}
