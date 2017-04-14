package main.res.fxml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Taylor on 4/14/2017.
 */
public class NewToolController {
    public TextField tfToolName;
    public TextField tfDrawerLetter;
    public Label lblDrawerEmpty;
    public Label lblToolEmpty;
    public ImageView imgDrawerEmpty;
    public ImageView imgToolEmpty;
    public Button btnSubmit;
    public Button btnCancel;
    private HashMap<String, String> toolData;
    /*public ComboBox cbDrawerLetter;
    private ObservableList<String> obsDrawerList = FXCollections.observableArrayList();*/

    /*public NewToolController(){
        ArrayList<String> drawerList = new ArrayList<>();
        drawerList.add("A");
        drawerList.add("B");
        drawerList.add("C");
        drawerList.add("D");
        drawerList.add("E");
        drawerList.add("F");
        drawerList.add("G");
        // cbDrawerLetter.getItems().clear();
        obsDrawerList.addAll(drawerList);
        // cbDrawerLetter.setItems(obsDrawerList);
        // cbDrawerLetter = new ComboBox(obsDrawerList);
    }*/

    public boolean submitCallback() {
        System.out.println("NewToolController.submitCallback() Begin");
        boolean success;
        lblDrawerEmpty.setVisible(false);
        lblToolEmpty.setVisible(false);
        imgDrawerEmpty.setVisible(false);
        imgToolEmpty.setVisible(false);

        String toolName = tfToolName.getText();
        String drawerLetter = tfDrawerLetter.getText();
        System.out.printf("String drawerLetter length = %d\n", drawerLetter.length());

        if(toolName.isEmpty() || drawerLetter.isEmpty()){
            if(toolName.isEmpty() && drawerLetter.isEmpty()){
                lblToolEmpty.setVisible(true);
                imgToolEmpty.setVisible(true);
                lblDrawerEmpty.setVisible(true);
                imgDrawerEmpty.setVisible(true);
            }else if(toolName.isEmpty()){
                lblToolEmpty.setVisible(true);
                imgToolEmpty.setVisible(true);
            }else{
                lblDrawerEmpty.setVisible(true);
                imgDrawerEmpty.setVisible(true);
            }
            success = false;
        }else if(drawerLetter.length() != 1){
            success = false;
        }else if(drawerLetter.charAt(0) < 'A' || drawerLetter.charAt(0) > 'G'){
            success = false;
        }else{
            Stage myStage = (Stage) btnCancel.getScene().getWindow();
            myStage.close();
            success = true;

            toolData = new HashMap<>();
            toolData.put("name",toolName);
            toolData.put("drawer",drawerLetter);
        }



        System.out.println("NewToolController.submitCallback() End");
        return success;
    }

    public void cancelCallback(ActionEvent actionEvent) {
    }

    public HashMap<String, String> getToolData(){
        return this.toolData;
    }
}
