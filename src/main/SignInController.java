package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * The SignInController will handle the operation of signing in and authorizing the user
 * in order to give full access to the program and update the Main Controller which handles
 * the main view of the program.
 */
public class SignInController {
    @FXML
    private TextField tfUserName, tfPassword;
    @FXML
    private Label lblUNEmpty, lblPWEmpty;
    @FXML
    private ImageView imgUNEmpty, imgPWEmpty;
    @FXML
    private Button btnCancel;

    // if the sign in button is pressed the request will be proccessed
    public boolean btnSignInPressed() {
        String username = tfUserName.getText();
        String password = tfPassword.getText();
        imgPWEmpty.setVisible(false);
        lblPWEmpty.setVisible(false);
        imgUNEmpty.setVisible(false);
        lblUNEmpty.setVisible(false);

        // if either the password or username is empty we alert the user
        if (password.isEmpty() || username.isEmpty()) {
            if (password.isEmpty() && username.isEmpty()) {
            imgPWEmpty.setVisible(true);
            lblPWEmpty.setVisible(true);
            imgUNEmpty.setVisible(true);
            lblUNEmpty.setVisible(true);
        } else if (password.isEmpty()) {
            imgPWEmpty.setVisible(true);
            lblPWEmpty.setVisible(true);
        } else if (username.isEmpty()) {
            imgUNEmpty.setVisible(true);
            lblUNEmpty.setVisible(true);
        }

        return false;
    }

        ServerRequest request = new ServerRequest();
        HashMap<String, String> POSTdata = new HashMap<>();
        String hashedPW = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);

        POSTdata.put("username", username);
        POSTdata.put("password", hashedPW);

        String response = request.getResponseFromRequest("login/login.php", POSTdata);

        if (response.equalsIgnoreCase("success")) {
            System.out.println("logged in :)");
            Stage myStage = (Stage) btnCancel.getScene().getWindow();
            myStage.close();
            return true;
        } else {
            System.out.println("not logged in :(");
            System.out.println("bad credentials!");
        }

        return false;
    }

    // if the cancel button is pressed the window will be closed
    public void btnCancelPressed() {
        Stage myStage = (Stage) btnCancel.getScene().getWindow();
        myStage.close();
    }
}
