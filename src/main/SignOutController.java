package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The SignOutController will be a simple controller that will delegate to the main controller
 * whether the user wants to log out or not.
 */
public class SignOutController {
    @FXML
    private Button btnYes, btnNo;

    private boolean ssi;

    public void btnYesPressed () throws IOException {
        stillSignedIn(false);
    }

    public void btnNoPressed () throws IOException {
        stillSignedIn(true);
    }

    public void stillSignedIn(boolean ssi) {
        this.ssi = ssi;
        System.out.println("Closing Window " + ssi);
        Stage myStage = (Stage) btnNo.getScene().getWindow();
        myStage.close();
    }

    public boolean getSSI() {
        return this.ssi;
    }
}
