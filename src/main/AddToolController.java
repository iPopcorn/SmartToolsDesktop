package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by mwhar on 2/10/2017.
 */
public class AddToolController {

    @FXML
    public Button btnBack;
    @FXML
    public ChoiceBox btnToolAddress;
    //public Button btnRefresh;
    //public Button btnSubmit;
    //public Button btnSwitch;
    @FXML
    public ChoiceBox btnToolName;
    public TextField txtTagID;
    //private ReaderThread currentReader;
    private String hostname = "169.254.126.52";

    @FXML
    private void initialize() {
        btnToolName.setItems(FXCollections.observableArrayList("Drill", "Screwdriver", "Ruler", "Knife"));
        btnToolAddress.setItems(FXCollections.observableArrayList("05A01", "05A02", "05A03", "05A04", "05A05"));
    }

    /**
     * submitTool() is the function that is called when the submit button of the add tool screen is pressed.
     * This function grabs the data from the GUI and places it in a HashMap.
     **/
    public void submitTool() {
        System.out.println("addToolSubmit()");

        // read string from text field
        String tagID = txtTagID.getText();

        String path = "tool-handling/insert-tool.php";

        // create HashMap to store values
        HashMap<String, String> queryValues = new HashMap(3);

        // get list of items for the tools and the addresses
        // TODO: address list should be generated based on tool list selection
        ObservableList<MenuItem> toolList = btnToolName.getItems();
        ObservableList<MenuItem> addressList = btnToolAddress.getItems();
        CheckMenuItem temp, lastSelectedTool = null, lastSelectedAddress = null;

        // iterate through the list of tool names, only used the last selected tool
        // TODO: think of some way to limit user to select only 1 tool
        for (int i = 0; i < toolList.size(); i++) {
            temp = (CheckMenuItem) toolList.get(i);

            if (temp.isSelected()) {
                //System.out.printf("%s is selected\n", temp.getId());
                lastSelectedTool = temp;
            }
        }

        // iterate through address list, only use the last selected address
        for (int i = 0; i < addressList.size(); i++) {
            temp = (CheckMenuItem) addressList.get(i);

            if (temp.isSelected()) {
                //System.out.printf("%s is selected\n", temp.getId());
                lastSelectedAddress = temp;
            }
        }

        if (lastSelectedAddress != null && lastSelectedTool != null) {
            queryValues.put("tagID", tagID);
            queryValues.put("toolName", lastSelectedTool.getText());
            queryValues.put("toolAddress", lastSelectedAddress.getText());
            //queryValues.put("toolAddress", "03b11");
            System.out.printf("tag ID: %s\nTool Name: %s\nAddress: %s\n", queryValues.get("tagID"), queryValues.get("toolName"), queryValues.get("toolAddress"));
        } else {
            // TODO: add error label for non-selection
            System.out.printf("Either address or tool name not selected.");
        }

        ServerRequest request = new ServerRequest();
        request.getResponseFromRequest(path, queryValues);
        //return queryValues;
    }

    /**
     * scanTool() is the function that is called when the 'refresh' button of the add tool screen is pressed.
     * This function reads the rfid tag infront of the RFID reader and prints out its ID and the number of times it
     * read the tag.
     **/
    public void scanTool(ActionEvent actionEvent) throws IOException {
        ReaderThread myReaderThread = new ReaderThread(this.hostname, "add_tool", this);
        myReaderThread.start();
        try {
            myReaderThread.join();
        } catch (java.lang.InterruptedException ie) {
            System.out.println(ie);
        }
        myReaderThread.stopReader();
        HashMap<String, Integer> tagValues = myReaderThread.getTagValues();
        int curMax = 0;
        String resultEpc = "Didn't read anything...";

        try {
            for (String key : tagValues.keySet()) {
                System.out.printf("Tag ID: %s\nCount: %d\n", key, tagValues.get(key));
                if (tagValues.get(key) > curMax)
                    resultEpc = key;
            }
            this.txtTagID.setText(resultEpc);
        } catch (NullPointerException e) {
            System.out.print(e);
        }
    }

    /**
     * gotoMainMenu() is the function that is called when the 'back' button of the add tool screen is pressed.
     * This function returns the user to the main menu of the program.
     **/
    public void gotoMainMenu(ActionEvent actionEvent) throws IOException {
        Stage stage = null;
        Parent root = null;

        if (actionEvent.getSource() == btnBack) {
            stage = (Stage) btnBack.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("res/fxml/main_menu.fxml"));
        } else {
            stage = null;
            root = null;
        }

        // create new scene with stage and root
        if (stage != null && root != null) {
            Scene scene = new Scene(root, 640, 480);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("Stage or root is null!");
        }
    }
}
