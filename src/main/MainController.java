package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    public Button btnOpenLookup;

    public void openLookupScene(ActionEvent actionEvent) throws IOException {
        Stage stage;
        Parent root;
        if(actionEvent.getSource() == btnOpenLookup){
            // get reference to button's stage
            stage = (Stage) btnOpenLookup.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("lookup_tool.fxml"));
        }else{
            stage = null;
            root = null;
        }

        // create new scene with stage and root
        if(stage != null && root != null){
            Scene scene = new Scene(root, 500, 500);
            stage.setScene(scene);
            stage.show();
        }else{
            System.out.println("Stage or root is null!");
        }
    }

    public void openGenerateReportScene(ActionEvent actionEvent) {
    }

    public void openMainMenu(ActionEvent actionEvent) {
    }
}
