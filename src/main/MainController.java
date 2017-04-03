package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Created by mwhar on 3/25/2017.
 */
public class MainController {

    @FXML
    public Tab searchInventory;
    @FXML
    public Tab modifyInventory;
    @FXML
    public Tab reportInventory;
    @FXML
    public BorderPane borderPane;

    @FXML
    public void changeScreen() throws IOException {
        if(searchInventory.isSelected())
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/lookup_tool.fxml")));
        else if(modifyInventory.isSelected())
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/add_tool.fxml")));
        else if(reportInventory.isSelected())
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/generate_report.fxml")));
        else
            System.out.println("No option selected.");

    }
}
