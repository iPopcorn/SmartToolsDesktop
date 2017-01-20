package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by taylor on 1/20/17.
 */
public class ReportController {


    private ArrayList<String> genReportTagList;
    private HashMap<String, String> addressMap;
    //private ArrayList<CompareTuple<String, String, Boolean>> comparisonMap;
    private ReaderThread currentReader;
    private String hostname = "169.254.126.52";
    private ArrayList<Tool> toolList;
    private int toolboxNum;

    public Button btnGenReportBack;
    public Button btnGenReportPull;
    public Button btnGenReportCSV;
    public Button btnGenReportEmail;
    public Button btnGenReportStart;
    public Button btnGenReportStop;
    public ListView genReportList;
    public TextField txtToolboxNum;

    public void initialize(){
        this.toolList = new ArrayList<>();
        this.genReportList.setCellFactory(new ToolCellFactory());
    }

    public void openMainMenu(ActionEvent actionEvent) throws IOException{
        Stage stage = null;
        Parent root = null;

        if(actionEvent.getSource() == btnGenReportBack){
            stage = (Stage) btnGenReportBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
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

    public void genReportDisplay(ActionEvent actionEvent) {
        //todo: remove this test case
        this.toolList = getAddressMapFromDB(this.toolboxNum);

        if(this.toolList == null){ // check if toolList is null
            System.out.println("Error: toolList not initialized");
        }else{// if we have a tool list

            //setup the CellFactory for the listview
            this.genReportList.setCellFactory(new ToolCellFactory());

            // ArrayList to hold missing tools
            ArrayList<Tool> missingTools = new ArrayList<>();
            for(Tool tool: this.toolList){
                if(!tool.getIsHome())
                    missingTools.add(tool);
            }

            // Display ArrayList of missing tools
            this.genReportList.getItems().clear();
            this.genReportList.refresh();
            this.genReportList.getItems().addAll(missingTools);
        }

    }

    public void genReportCSV(ActionEvent actionEvent) {
    }

    public void genReportEmail(ActionEvent actionEvent) {
    }

    public void genReportStartScanning(){
        System.out.printf("genReportStartScanning()\n");
        if(this.txtToolboxNum.getText() != null){
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
                //this.currentReader = new ReaderThread(this.hostname, "generate_report");
            } else {
                System.out.println("Invalid toolbox number!");
            }
        }else{
            System.out.println("textbox is empty.");
        }


    }

    // TODO: optimize comparison algorith in genReportStopScanning()
    public void genReportStopScanning(){
        if(this.currentReader != null){
            System.out.printf("genReportStopScanning()");
            this.currentReader.stopReader();

            // grab tag list from reader
            this.genReportTagList = this.currentReader.getGenReportTagValues();

            // compare tag list with tool list
            this.toolList = this.getAddressMapFromDB(this.toolboxNum);

            // for each id in the tag list find id in comparisonMap and mark True
            // todo: this is O(n^2), we need to optimize it.
            // todo: this doesn't handle extra tools
            for(String tagId : this.genReportTagList){
                for(Tool tool : this.toolList){
                    if(tagId.equals(tool.getId())){
                        tool.setIsHome(true);
                    }
                }
            }
        }else{
            System.out.println("Reader never started!");
        }

    }

    /**
     * getAddressMapFromDB() grabs the address map from the database
     * TODO: implement logic for the getAddressMapFromDB() method.
     *
     * SELECT * FROM 'Tools' WHERE 'belongs_to' = toolboxNumber;
     * **/
    private ArrayList<Tool> getAddressMapFromDB(int toolboxNumber) {
        // temporary logic for the sake of finishing generate report logic.
        ArrayList<Tool> tempList = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
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

        }

        return tempList;
    }
}
