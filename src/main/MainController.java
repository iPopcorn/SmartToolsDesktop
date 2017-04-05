package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Created by mwhar on 3/25/2017.
 */
public class MainController {
    /** Tab to switch to the Search Inventory screen. */
    public Tab searchInventory;

    /** Tab to switch to the Modify Inventory screen. */
    public Tab modifyInventory;

    /** Tab to switch to the Report Inventory screen. */
    public Tab reportInventory;

    /** BorderPane where the main elements of the application will be displayed. */
    public BorderPane borderPane;


    /** Changes the screen shown to the user based on the tab selected.
     *
     *  void changeScreen() - Changes the screen and tool bar buttons shown to the user based on the tab selected.
     *
     *  @throws IOException
     */
    public void changeScreen() throws IOException {
        if(searchInventory.isSelected())
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/lookup_tool.fxml")));
        else if(modifyInventory.isSelected())
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/add_tool.fxml")));
        else
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/generate_report.fxml")));

    }
}
