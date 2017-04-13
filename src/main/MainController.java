package main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by mwhar on 3/25/2017.
 */
public class MainController {

    private boolean signedIn = false; // boolean to check if the user is signed in

    /** Tab to switch to the Search Inventory screen. */
    public Tab searchTools;

    /** Tab to switch to the Replace Tool screen. */
    public Tab replaceTool;

    /** Tab to switch to the Replace Tool screen. */
    public Tab deleteTool;

    /** Tab to switch to the Modify Inventory screen. */
    public Tab modifyInventory;

    /** Tab to switch to the Report Inventory screen. */
    public Tab reportInventory;

    /** Tab to popup the sign in screen. */
    public Tab tabSignIn;

    /** BorderPane where the main elements of the application will be displayed. */
    public BorderPane borderPane;

    /** Changes the screen shown to the user based on the tab selected.
     *
     *  void changeScreen() - Changes the screen and tool bar buttons shown to the user based on the tab selected.
     *
     *  @throws IOException
     */
    public void changeScreen() throws IOException {
        if(searchTools.isSelected())
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/lookup_tool.fxml")));
        else if(replaceTool.isSelected())
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/add_tool.fxml")));
        else if(deleteTool.isSelected())
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/delete_tool.fxml")));
        else if(modifyInventory.isSelected())
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/modify_inventory.fxml")));
        else if(reportInventory.isSelected())
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/generate_report.fxml")));
        else if (tabSignIn.isSelected()) {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("res/fxml/sign_in_window.fxml"));
            Stage signInWindow = new Stage();
            signInWindow.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root, 400, 200);

            signInWindow.setScene(scene);
            signInWindow.setResizable(false);
            signInWindow.show();
            System.out.printf("here");
        }

    }
}
