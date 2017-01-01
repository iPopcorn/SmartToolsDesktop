package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public Stage myStage;
    public Scene mainMenu;
    public Scene lookupTool;
    public Scene generateReport;

    /*
     * TEST CLASS
     */

    ServerResponse serverResponse;

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*
         * TEST
         */

        serverResponse = new ServerResponse();
        String response = serverResponse.getResponse("cse-4322/invitations/invitations.php");
        System.out.println(response);

        // set stage
        myStage = primaryStage;

        // create root objects for scene, connecting them to fxml files
        Parent mainMenuRoot = FXMLLoader.load(getClass().getResource("main_menu.fxml"));
        //Parent lookupToolRoot = FXMLLoader.load(getClass().getResource("lookup_tool.fxml"));
        mainMenu = new Scene(mainMenuRoot, 640, 480);
        myStage.setTitle("Desktop App");
        myStage.setScene(mainMenu);
        myStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
