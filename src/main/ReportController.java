package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by taylor on 1/20/17.
 */
public class ReportController {


    public Button btnGenReportBack;
    public Button btnGenReportPull;
    public Button btnGenReportCSV;
    public Button btnGenReportEmail;
    public Button btnGenReportStart;
    public Button btnGenReportStop;
    public ListView genReportList;
    public TextField txtToolboxNum;
    public RadioButton radioName;
    public RadioButton radioAddress;
    public RadioMenuItem radioAll;
    public RadioMenuItem radioHome;
    public RadioMenuItem radioMissing;
    public AnchorPane paneGenReport;
    public Label labelError;
    private ArrayList<String> genReportTagList;
    private HashMap<String, String> addressMap;
    //private ArrayList<CompareTuple<String, String, Boolean>> comparisonMap;
    private ReaderThread currentReader;
    private String hostname = "169.254.126.52";
    private ArrayList<Tool> toolList;
    private ArrayList<Tool> missingTools;
    private ArrayList<Tool> currentList;
    private int toolboxNum = -1;
    private boolean scanning = false;

    public void initialize(){
        this.toolList = new ArrayList<>();
        this.genReportList.setCellFactory(new ToolCellFactory());
    }

    public void openMainMenu(ActionEvent actionEvent) throws IOException{
        Stage stage = null;
        Parent root = null;

        if(actionEvent.getSource() == btnGenReportBack){
            stage = (Stage) btnGenReportBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("res/fxml/main_menu.fxml"));
        }else{
            stage = null;
            root = null;
        }

        // create new scene with stage and root
        if(stage != null && root != null){
            Double width = stage.getWidth();
            Double height = stage.getHeight();
            Scene scene = new Scene(root, width, height);            stage.setScene(scene);
            stage.show();
        }else{
            System.out.println("Stage or root is null!");
        }
    }

    public void genReportDisplay(ActionEvent actionEvent) {
        System.out.println("Begin ReportController.genReportDisplay()");

        // initialize missing tools ArrayList if it hasn't been already.
        if(this.missingTools == null){
            this.missingTools = new ArrayList<>();
        }

        if(this.toolList == null || this.toolList.isEmpty()){ // check if toolList is null or empty
            System.out.println("Error: toolList not initialized");
            // this.showError("Error: toolList not initialized, run scanner first.");
            PopupWindow errorPopup = new PopupWindow("Error", "toolList not initialized, run scanner first!");
            errorPopup.popup();
        }else{// if we have a tool list

            //setup the CellFactory for the listview
            this.genReportList.setCellFactory(new ToolCellFactory());

            // check view selection
            if(this.radioMissing.isSelected()){ // display missing tools
                System.out.println("ReportController.genReportDisplay() missing tools case.");
                this.missingTools.clear();

                // add to missing tools array list
                for(Tool tool: this.toolList){
                    if(tool.getIsHome() != true){
                        System.out.printf("Found Missing Tool: %s\n",tool.toString());
                        missingTools.add(tool);
                    }
                }

                // print missing tools
                /*System.out.println("Missing Tools:");
                for(Tool tool : missingTools)
                    System.out.printf("%s\n", tool);*/

                // Sort list of missing tools, then display it.
                ObservableList<Tool> oMissingTools = FXCollections.observableArrayList(missingTools);
                ObservableList<Tool> sortedTools = this.sortTools(oMissingTools);
                this.genReportList.setItems(null);
                this.genReportList.refresh();
                this.genReportList.setItems(sortedTools);
                this.currentList = missingTools;
            }else if(this.radioHome.isSelected()){ // display at home tools
                System.out.println("ReportController.genReportDisplay() home tools case.");
                ArrayList<Tool> homeTools = new ArrayList<>();

                for(Tool tool: this.toolList){
                    if(tool.getIsHome() == true){
                        homeTools.add(tool);
                    }
                }

                if(homeTools.isEmpty()){
                    // this.showError("Error: No tools at home.");
                    PopupWindow errorPopup = new PopupWindow("Error", "No tools at home!");
                    errorPopup.popup();
                }else{
                    ObservableList<Tool> oHomeTools = FXCollections.observableArrayList(homeTools);
                    ObservableList<Tool> oSortedHomeTools = this.sortTools(oHomeTools);

                    this.genReportList.setItems(null);
                    this.genReportList.refresh();
                    this.genReportList.setItems(oSortedHomeTools);
                }
                this.currentList = homeTools;
            }else{ // display all tools
                System.out.println("ReportController.genReportDisplay() all tools case.");

                ObservableList<Tool> oToolList = FXCollections.observableArrayList(this.toolList);
                ObservableList<Tool> oSortedToolList = this.sortTools(oToolList);

                try{
                    this.genReportList.setItems(null);
                    this.genReportList.refresh();
                    this.genReportList.setItems(oSortedToolList);
                }catch(Exception e){
                    System.out.println(e);
                }

                this.currentList = this.toolList;
            }
        }
        System.out.println("End ReportController.genReportDisplay()");
    }

    public void genReportCSV(ActionEvent actionEvent) {
        boolean success = false;
        System.out.println("ReportController.genReportCSV()");
        System.out.println("Export To CSV PRESSED");

        try{
            Stage stage = new Stage();
            FileChooser fileChooser = new FileChooser();
            configureFileChooser(fileChooser);
            File outFile = fileChooser.showSaveDialog(stage);


            FileWriter fw = new FileWriter(outFile.getAbsolutePath());
            CSVPrinter myPrinter = new CSVPrinter(fw, CSVFormat.EXCEL);


            if(this.toolboxNum == -1)
                this.toolboxNum = Integer.valueOf(this.txtToolboxNum.getText());

            if(this.toolList == null){ // check if toolList is null
                System.out.println("Error: toolList not initialized");
                // this.showError("Error: toolList not initialized");
                PopupWindow errorPopup = new PopupWindow("Error", "toolList not initialized!");
                errorPopup.popup();
            }else{// if we have a tool list
                String record;
                for(Tool tool: this.currentList){
                    record = String.format("%s,%s,%s", tool.getName(), tool.getAddress(), tool.getId());
                    System.out.println(record);
                    myPrinter.printRecord(record);
                }

                /*// check if we want missing or home tools.
                // TODO: show error if missingTools/homeTools array is empty
                if(this.radioMissing.isSelected()){ // we want missing tools
                    // ArrayList to hold missing tools
                    ArrayList<Tool> missingTools = new ArrayList<>();
                    for(Tool tool: this.toolList){
                        if(!tool.getIsHome())
                            missingTools.add(tool);
                    }

                    // Write ArrayList of missing tools
                    String record;
                    for(Tool tool: missingTools){
                        record = String.format("%s,%s,%s", tool.getName(), tool.getAddress(), tool.getId());
                        System.out.println(record);
                        myPrinter.printRecord(record);
                    }
                }else{ // we want home tools
                    ArrayList<Tool> homeTools = new ArrayList<>();
                    for(Tool tool: this.toolList){
                        if(tool.getIsHome())
                            homeTools.add(tool);
                    }

                    // Write ArrayList of home tools
                    String record;
                    for(Tool tool: homeTools){
                        record = String.format("%s,%s,%s", tool.getName(), tool.getAddress(), tool.getId());
                        System.out.println(record);
                        myPrinter.printRecord(record);
                    }
                }*/

                myPrinter.close();
                success = true;
            }
        } catch(NumberFormatException n){
            System.out.println(n);
            success = false;
            // this.showError("Error: Invalid Toolbox Number");
            PopupWindow errorPopup = new PopupWindow("Error", "Invalid Toolbox Number!");
            errorPopup.popup();
        }
        catch(Exception e){
            System.out.println(e);
            success = false;
        }
        if(success){
            // this.showError("Message: CSV creation success.");
            PopupWindow errorPopup = new PopupWindow("Success", "CSV file created!");
            errorPopup.popup();
        }
        System.out.println("genReportCSV() end.");
    }

    public void genReportEmail(ActionEvent actionEvent) {
        try{
            File inventoryReport = this.createFile();
            System.out.println("begin genReportEmail()");
            EmailHandler handler = new EmailHandler();
            handler.sendEmail("taypetrillo@gmail.com", inventoryReport);
            System.out.println("end genReportEmail()");
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void genReportStartScanning(){
        System.out.printf("ReportController.genReportStartScanning()\n");
        try{
            String myToolboxNum = this.txtToolboxNum.getText();
            System.out.printf("myToolboxNum = %s\n", myToolboxNum);
            this.toolboxNum = Integer.valueOf(myToolboxNum);
            System.out.printf("The toolboxNum is: %d\n", this.toolboxNum);
        }catch(NumberFormatException e){
            System.out.println(e);
        }
        if(this.toolboxNum > 0 && this.toolboxNum < 6){
            System.out.println("Correct toolbox number!");
            ReaderThread tempReader = new ReaderThread(this.hostname, "generate_report", this);
            tempReader.run();
            this.currentReader = tempReader;
            System.out.println(tempReader.toString());
        } else {
            System.out.println("Invalid toolbox number!");
            // this.showError("Invalid Toolbox Number.");
            PopupWindow errorPopup = new PopupWindow("Error", "Invalid toolbox number!");
            errorPopup.popup();
        }
    }

    public void showError(String s) {
        this.labelError.setText(s);
    }

    // TODO: optimize comparison algorithm in genReportStopScanning()
    public void genReportStopScanning(){
        System.out.println("ReportController.genReportStopScanning()\n");
        // remove error message
        if(!this.labelError.getText().equalsIgnoreCase("")) // if the label is not "" make it ""
            this.labelError.setText("");
        if(this.currentReader != null){
            this.currentReader.stopReader();

            // grab tag list from reader
            this.genReportTagList = this.currentReader.getGenReportTagValues();

            if(this.genReportTagList != null) {
                System.out.println("tagList set successfully.");
            }else{
                System.out.println("There is no genReportTagValues inside of this.currentReader.");
                // this.showError("There is no genReportTagValues inside of this.currentReader.");
                PopupWindow errorPopup = new PopupWindow("Error", "There is no genReportTagValues inside of this.currentReader!");
                errorPopup.popup();
            }

            // print tag values
            for(String tagID : this.genReportTagList)
                    System.out.println("Tag ID: " + tagID);

            // compare tag list with tool list
            this.toolList = this.getAddressMapFromDB(this.toolboxNum);

            // for each id in the tag list find id in comparisonMap and mark True
            // todo: this is O(n^2), we need to optimize it.
            // todo: this doesn't handle extra tools
            Tool tool;
            ArrayList<Tool> updatedToolList = new ArrayList<>();
            for(String tagId : this.genReportTagList){
                for(int i = 0; i < this.toolList.size(); i++){
                    tool = this.toolList.get(i);
                    if(tagId.equals(tool.getId())){
                        System.out.printf("Found Tool: %s\n", tool.toString());
                        this.toolList.get(i).setIsHome(true);
                        System.out.printf("Tool is now: %s\n", this.toolList.get(i));
                        updatedToolList.add(tool);
                    }
                }
            }
        }else{
            System.out.println("Reader never started!");
            // this.showError("Reader never started!");
            PopupWindow errorPopup = new PopupWindow("Error", "Reader never started!");
            errorPopup.popup();
        }
        System.out.println("ReportController.genReportStopScanning() end!");
    }

    public void scan() {
        if(scanning)
        {
            genReportStopScanning();
            this.btnGenReportStart.getStyleClass().removeAll("stopScanning");
            this.btnGenReportStart.getStyleClass().add("startScanning");
            this.btnGenReportStart.setText("Start Scanning");
            scanning = false;
        }
        else
        {
            genReportStartScanning();
            this.btnGenReportStart.getStyleClass().removeAll("startScanning");
            this.btnGenReportStart.getStyleClass().add("stopScanning");
            this.btnGenReportStart.setText("Stop Scanning");
            scanning = true;
        }
    }
    /**
     * getAddressMapFromDB() grabs the address map from the database
     * TODO: implement logic for the getAddressMapFromDB() method.
     *
     * SELECT * FROM 'Tools' WHERE 'belongs_to' = toolboxNumber;
     * **/
    private ArrayList<Tool> getAddressMapFromDB(int toolboxNumber) {
        /*// temporary logic for the sake of finishing generate report logic.
        ArrayList<Tool> tempList = new ArrayList<>();
        *//*for (int i = 0; i < 25; i++) {
            if(i < 5) {
                Tool newTool = new Tool("Hammer", Integer.toString(i*236), Integer.toString(i));
                tempList.add(newTool);
            }
            else if(i >= 5 && i <= 13) {
                Tool newTool = new Tool("Screwdriver", Integer.toString(i*72+2), Integer.toString(i));
                tempList.add(newTool);
            }
            else {
                Tool newTool = new Tool("Drill", Integer.toString(i*11+1), Integer.toString(i));
                tempList.add(newTool);
            }

        }*//*
        Tool t1 = new Tool("Hammer", "3A1F", "05A01");
        Tool t2 = new Tool("Screw Driver", "3BA0 1F7E 463B", "05A02");
        Tool t3 = new Tool("Ruler", "036F 1358 7584 66A0 07F5", "05A03");
        Tool t4 = new Tool("Drill", "01E9 1109", "05A04");
        tempList.add(t1);
        tempList.add(t2);
        tempList.add(t3);
        tempList.add(t4);

        return tempList;*/
        ServerRequest myRequest = new ServerRequest();
        JSONdecoder requestDecoder = new JSONdecoder();
        HashMap<String, String> data = new HashMap<>();
        data.put("searchField","toolbox");
        data.put("searchValue",Integer.toString(toolboxNumber));
        String response = myRequest.getResponseFromRequest("tool-handling/lookup-tool.php", data);
        System.out.println(response);
        ArrayList<Tool> tempList = requestDecoder.decodeJSONToolResponse(response);
        return tempList;
    }

    /**
     * sortTools() returns a sorted ObservableList based on which radio button is selected at the time that the pull
     * report button is clicked.
     * **/
    public ObservableList sortTools(ObservableList list){
        // return sorted list based on selection
        if(this.radioName.isSelected())
            return list.sorted(new NameComparator());
        else
            return list.sorted(new AddressComparator());
    }

    /**
     *  configureFileChooser confifures the file chooser to start at the default
     *  user.home directory and also limits the output file to .csv
     **/

    private static void configureFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Select location to store CSV");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
    }

    /**
     * createFile() creates a new .csv file based on the selected options.
     * **/
    public File createFile() throws IOException{
        System.out.println("ReportController.createFile() Begin");
        File outFile = new File("InventoryReport.csv");
        // create the file object

        FileWriter fw = new FileWriter(outFile.getAbsolutePath());
        CSVPrinter myPrinter = new CSVPrinter(fw, CSVFormat.EXCEL);

        if(this.toolboxNum == -1)
            this.toolboxNum = Integer.valueOf(this.txtToolboxNum.getText());

        if(this.toolList == null){ // check if toolList is null
            System.out.println("Error: toolList not initialized");
            // this.showError("Error: toolList not initialized");
            PopupWindow errorPopup = new PopupWindow("Error", "toolList not initialized!");
            errorPopup.popup();
        }else {// if we have a tool list
            String record;
            for (Tool tool : this.currentList) {
                record = String.format("%s,%s,%s", tool.getName(), tool.getAddress(), tool.getId());
                System.out.println(record);
                myPrinter.printRecord(record);
            }
        }

        myPrinter.close();
        System.out.println("ReportController.createFile() End");
        return outFile;
    }

    public void scannerConnectionError() {
        PopupWindow error = new PopupWindow("Error", "Reader failed to connect!");
        error.popup();
    }
}
