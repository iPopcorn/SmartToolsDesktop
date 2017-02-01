package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class Main extends Application {
    public Stage myStage;
    public Scene mainMenu;
    public Scene lookupTool;
    public Scene generateReport;

    /*
     * TEST CLASS
     */

    ServerRequest serverRequest;
    JSONdecoder responseDecoder;

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*
         * TEST
         */

        serverRequest = new ServerRequest();
        responseDecoder = new JSONdecoder();
        HashMap<String, String> POSTdata = new HashMap<>();
        POSTdata.put("searchField", "name");
        POSTdata.put("searchValue", "hammer");

        String response = serverRequest.getResponseFromRequest("tool-handling/lookup-tool.php", POSTdata);
        responseDecoder.decodeJSONToolResponse(response);


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
