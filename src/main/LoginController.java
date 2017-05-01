package main;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

/**
 * LoginController - Controller for the login.fxml view, implements the functionality of the view
 */
public class LoginController {

    /** Text field that holds the username*/
    public TextField txtUsername;

    /** Text field that holds the password*/
    public TextField txtPassword;

    /** Button that returns the user back to the main menu*/
    public Button btnBack;

    /** Button that submits login credentials to the system*/
    public Button btnLogIn;

    /** Grabs the text from the GUI and sends it to the database for authentication.
     *
     * void submitLogin(ActionEvent) - submitLogin pulls the text from the GUI. It hashes the password text, and then
     * packages the username and hashed password into a HashMap that is sent to the database. The response will be
     * parsed, and if the authentication is successful, the ActionEvent will be passed to the openAdmin() method.
     * Otherwise, an error message will pop up.
     *
     * @param actionEvent The ActionEvent that triggered this callback.
     * */
    public void submitLogin(ActionEvent actionEvent) {
        String username, password, hashedPW;
        username = txtUsername.getText();
        password = txtPassword.getText();
        hashedPW = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
        System.out.printf("Username: %s\nPassword: %s\nHashed Password: %s\n", username, password, hashedPW);

        ServerRequest request = new ServerRequest();
        HashMap<String, String> POSTdata = new HashMap<>();
        POSTdata.put("username", username);
        POSTdata.put("password", hashedPW);

        String response = request.getResponseFromRequest("login/login.php", POSTdata);

        if(response.equalsIgnoreCase("success")){
            try{
                openAdmin(actionEvent);
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            //todo: insert GUI error message
            System.out.println("Invalid password.");
            PopupWindow error = new PopupWindow("Error", "Invalid Password!");
            error.popup();
        }
    }

    /** Opens the Admin screen
     *
     *  void openAdmin(ActionEvent) - openAdmin() displays the Admin screen in the GUI. It is called by submitLogin()
     *  upon a successful authentication attempt.
     *
     *  @param actionEvent The ActionEvent passed in by submitLogin()
     * */
    public void openAdmin(ActionEvent actionEvent) throws IOException{
        Stage stage = null;
        Parent root = null;

        stage = (Stage) btnBack.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("res/fxml/admin.fxml"));

        if(stage != null && root != null){
            Scene scene = new Scene(root, 640, 480);
            stage.setScene(scene);
            stage.show();
        }else
            System.out.println("Stage or Root null");
    }
}
