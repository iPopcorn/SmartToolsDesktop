package main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller to manage the profile page once the user is signed in
 */
public class ProfileController {

    // function called when edit password is pressed
    public void editPassword() throws IOException {

        Parent root;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("res/fxml/password_change.fxml"));
        root = fxmlLoader.load();
        Stage changePasswordWindow = new Stage();

        Scene scene = new Scene(root, 600, 400);

        changePasswordWindow.setScene(scene);
        changePasswordWindow.setResizable(false);
        changePasswordWindow.initModality(Modality.APPLICATION_MODAL);
        changePasswordWindow.showAndWait();


    }

}
