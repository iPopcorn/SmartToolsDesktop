package main;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.util.HashMap;

/**
 * ChangePasswordController will deal with the user wanting to change the admin password with
 * 2-factor authentication. The authentication methods are email authentication, and password
 * authentication. The user will be sent an email to the admin email account with a 4 digit pin.
 * Once the 4 digit pin is entered, the user will be prompted to enter the old password. If they pass
 * that authentication they will then be able to change the password.
 */
public class ChangePasswordController {

    @FXML
    TextField tfPIN, tfPassword, tfNewPassword;
    @FXML
    Label lblNPW, lblBadVer;
    @FXML
    ImageView imgBadPIN;
    @FXML
    Button btnChangePassword;



    private int PIN;
    private String adminEmail;
    private EmailHandler emailHandler;
    private String subject;
    private StringBuilder body;

    public ChangePasswordController () {
        emailHandler = new EmailHandler();
        this.adminEmail = "uta.smart.tools@gmail.com";
        PIN = java.util.concurrent.ThreadLocalRandom.current().nextInt(1000, 10000);
        this.subject = "Change Password";
        body = new StringBuilder();
        body.append("Your 4-digit pin is: ");
        body.append(PIN);

        emailHandler.sendEmail2(adminEmail, subject, body.toString());

    }

    @FXML
    private void verify() {

        // getting the string value of the PIN entered
        String userEnteredPIN = tfPIN.getText();

        if (userEnteredPIN.length() != 4) {
            imgBadPIN.setVisible(true);;
            return;
        } else {
            imgBadPIN.setVisible(false);
        }


        // if the user passes both authenticaton methods
        if(userEnteredPIN.equals(String.valueOf(PIN)) && checkPassword(tfPassword.getText())) {
            lblBadVer.setVisible(false);
            lblNPW.setVisible(true);
            btnChangePassword.setVisible(true);
            tfNewPassword.setVisible(true);
        } else {
            lblBadVer.setVisible(true);
            lblNPW.setVisible(false);
            btnChangePassword.setVisible(false);
            tfNewPassword.setVisible(false);
        }

    }

    // function to check that the password is correct
    private boolean checkPassword(String password) {
        ServerRequest serverRequest = new ServerRequest();
        HashMap<String, String> credentials = new HashMap<>();
        String hashedPW = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);

        credentials.put("username", "admin");
        credentials.put("password", hashedPW);

        String response = serverRequest.getResponseFromRequest("login/login.php", credentials);

        if (response.equalsIgnoreCase("success")) {
            return true;
        } else {
            return false;
        }
    }

    // changePassword() will handle the request of changing the password, with the button
    // change password being shown only when the verification is passed and the password fits the
    // correct format as checked in isValidPassword().
    @FXML
    private void changePassword() {
        String newPassword = tfNewPassword.getText();
        // checking if the new password is valid
        if (!isValidPassword(newPassword)) {
            // TODO: Add logic if the password isn't at least 6 characters
            return;
        }

        // hashing the new password to be sent to the DB
        String hashedPW = org.apache.commons.codec.digest.DigestUtils.sha256Hex(newPassword);

        // creating the server request object to send the change password request
        ServerRequest serverRequest = new ServerRequest();
        // hashmap of the POST data
        HashMap<String, String> POSTdata = new HashMap<>();

        POSTdata.put("action", "change_password");
        POSTdata.put("username", "admin");
        POSTdata.put("password", hashedPW);


        String response = serverRequest.getResponseFromRequest("login/change-password.php", POSTdata);

        if (response.equalsIgnoreCase("success")) {
            Stage myStage = (Stage) btnChangePassword.getScene().getWindow();
            myStage.close();
            PopupWindow popupWindow = new PopupWindow("Password Change", "Successfully Changed Password.");
            popupWindow.popup();
        } else {
            Stage myStage = (Stage) btnChangePassword.getScene().getWindow();
            myStage.close();
            PopupWindow popupWindow = new PopupWindow("Password Change", "Could Not Change Password.");
            popupWindow.popup();
        }

    }

    /*
     * Function to check if the password is the right length
     */
    private boolean isValidPassword(String password) {
        if (password.length() < 4)
            return false;
        return true;
    }

    public void cancel() {
        Stage myStage = (Stage) btnChangePassword.getScene().getWindow();
        myStage.close();
    }

    // this onEnter listener will commence the verify process if pressed on either of
    // the verification fields
    @FXML
    private void onEnter(javafx.event.ActionEvent actionEvent) {
        verify();
    }

    // this onEnter listener will commence the verify process if pressed on either of
    // the verification fields
    @FXML
    private void onEnterChangePW(javafx.event.ActionEvent actionEvent) {
        changePassword();
    }



}
