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
 * Created by Taylor on 2/9/2017.
 */
public class LoginController {

    public TextField txtUsername;
    public TextField txtPassword;
    public Button btnBack;
    public Button btnLogIn;

    public void submitLogin(ActionEvent actionEvent) {
        String username, password, hashedPW;
        username = txtUsername.getText();
        password = txtPassword.getText();
        hashedPW = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
        System.out.printf("Username: %s\nPassword: %s\nHashed Password: %s\n", username, password, hashedPW);

        //todo: add authentication logic
        ServerRequest request = new ServerRequest();
        HashMap<String, String> POSTdata = new HashMap<>();
        POSTdata.put("username", username);
        POSTdata.put("password", hashedPW);

        String response = request.getResponseFromRequest("login/login.php", POSTdata);

        if(response.equalsIgnoreCase("success")){
            try{
                openAdmin(actionEvent);
            }catch(IOException e){
                System.out.println(e);
            }
        }else{
            //todo: insert GUI error message
            System.out.println("Invalid password.");
        }
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        Stage stage = null;
        Parent root = null;

        stage = (Stage) btnBack.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("main_menu_admin.fxml"));

        if(stage != null && root != null){
            Scene scene = new Scene(root, 640, 480);
            stage.setScene(scene);
            stage.show();
        }else
            System.out.println("Stage or Root null");
    }

    public void openAdmin(ActionEvent actionEvent) throws IOException{
        Stage stage = null;
        Parent root = null;

        stage = (Stage) btnBack.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("admin.fxml"));

        if(stage != null && root != null){
            Scene scene = new Scene(root, 640, 480);
            stage.setScene(scene);
            stage.show();
        }else
            System.out.println("Stage or Root null");
    }
}
