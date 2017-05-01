package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private boolean signedIn = false; // boolean to check if the user is signed in

    /**
     * Tab to switch to the Search Inventory screen.
     */
    @FXML
    public Tab searchTools;

    /**
     * Tab to switch to the Replace Tool screen.
     */
    @FXML
    public Tab replaceTool;

    /**
     * Tab to switch to the Replace Tool screen.
     */
    @FXML
    public Tab deleteTool;

    /**
     * Tab to switch to the Modify Inventory screen.
     */
    @FXML
    public Tab modifyInventory;

    /**
     * Tab to switch to the Report Inventory screen.
     */
    @FXML
    public Tab reportInventory;

    /**
     * Tab to popup the sign in screen.
     */
    @FXML
    public Tab tabSignIn;

    /**
     * Tab to popup the sign in screen.
     */
    @FXML
    public Tab tabProfile;

    /**
     * BorderPane where the main elements of the application will be displayed.
     */
    @FXML
    public BorderPane borderPane;

    // reference to the tabPane, in order to set selected tabs
    @FXML
    private TabPane tabPane;
    // selection model to set the tab selected when logging in to the search tools tab
    private SelectionModel selectionModel;

    /**
     * Changes the screen shown to the user based on the tab selected.
     * <p>
     * void changeScreen() - Changes the screen and tool bar buttons shown to the user based on the tab selected.
     *
     * @throws IOException
     */
    public void changeScreen() throws IOException {
        // if we are signed in
        if (signedIn) {
            if (searchTools.isSelected())
                borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/lookup_tool.fxml")));
            else if (replaceTool.isSelected())
                borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/replace_tool.fxml")));
            else if (deleteTool.isSelected())
                borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/delete_tool.fxml")));
            else if (modifyInventory.isSelected())
                borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/modify_inventory.fxml")));
            else if (reportInventory.isSelected())
                borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/generate_report.fxml")));
            else if (tabSignIn.isSelected()) {
                launchSignOut();
            }
            else if (tabProfile.isSelected()) {
                borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/profile.fxml")));
            }
        } else {
            if (searchTools.isSelected())
                borderPane.setCenter(FXMLLoader.load(getClass().getResource("res/fxml/lookup_tool.fxml")));
            else if (replaceTool.isSelected())
                launchSignIn();
            else if (deleteTool.isSelected())
                launchSignIn();
            else if (modifyInventory.isSelected())
                launchSignIn();
            else if (reportInventory.isSelected())
                launchSignIn();
            else if (tabSignIn.isSelected()) {
                launchSignIn();
//                setSignedIn(true);
            }
        }
    }

    // Launches sign in window and updates UI if needed
    private void launchSignIn() throws IOException {
        Parent root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("res/fxml/sign_in_window.fxml"));
        root = fxmlLoader.load();
        SignInController signInController = fxmlLoader.getController();


        Stage signInWindow = new Stage();
        signInWindow.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root, 400, 200);

        signInWindow.setScene(scene);
        signInWindow.setTitle("Sign In");
        signInWindow.setResizable(false);
        signInWindow.showAndWait();

        setSignedIn(signInController.btnSignInPressed());

        System.out.println(signedIn);
    }

    // Launches sign out window and updates UI if needed
    public void launchSignOut() throws IOException {
        Parent root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("res/fxml/sign_out_window.fxml"));
        root = fxmlLoader.load();
        SignOutController signOutController = fxmlLoader.getController();


        Stage signInWindow = new Stage();
        signInWindow.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root, 400, 200);

        signInWindow.setScene(scene);
        signInWindow.setTitle("Sign Out");
        signInWindow.setResizable(false);
        signInWindow.showAndWait();

        setSignedIn(signOutController.getSSI());

        System.out.println(signedIn);
    }

    // set the signed in state
    public void setSignedIn(boolean signedIn) {

        // if we're switching signed in states
        if (this.signedIn != signedIn) {
            this.signedIn = signedIn;
            if (signedIn) {
                updateSignedInState(true);
            } else {
                updateSignedInState(false);
            }

        } else { // we are not switching signed in states so nothing changes
            selectionModel = tabPane.getSelectionModel();
            selectionModel.select(searchTools);
        }
    }

    /*
     * updateSignedInState(boolean signedIn)
     * Parameter(s): boolean signedIn
     * Output(s): No return values
     *
     * Updates the main view to unlock/lock the administrative functionalities depending on if the user
     * is signed in or not.
     */
    public void updateSignedInState(boolean signedIn) {
        selectionModel = tabPane.getSelectionModel();

        if (signedIn) {
            replaceTool.setDisable(false);
            deleteTool.setDisable(false);
            modifyInventory.setDisable(false);
            reportInventory.setDisable(false);
            tabProfile.setDisable(false);
            selectionModel.select(searchTools);
            // updating the tab text to signed out, since we are signed in
            tabSignIn.setText("Sign Out");

        } else {
            replaceTool.setDisable(true);
            deleteTool.setDisable(true);
            modifyInventory.setDisable(true);
            reportInventory.setDisable(true);
            tabProfile.setDisable(true);
            selectionModel.select(searchTools);
            tabSignIn.setText("Sign In");

        }
    }
}
