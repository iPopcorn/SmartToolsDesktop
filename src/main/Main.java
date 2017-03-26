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

    ServerRequest serverRequest;
    JSONdecoder responseDecoder;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*
         * TEST
         */

       /* serverRequest = new ServerRequest();
        responseDecoder = new JSONdecoder();
        HashMap<String, String> POSTdata = new HashMap<>();
        POSTdata.put("searchField", "name");
        POSTdata.put("searchValue", "hammer");

        String response = serverRequest.getResponseFromRequest("tool-handling/lookup-tool.php", POSTdata);
        responseDecoder.decodeJSONToolResponse(response);*/
        boolean login = false;


        // set stage
        myStage = primaryStage;
        Parent mainMenuRoot;

        // create root objects for scene, connecting them to fxml files
        if(login)
            mainMenuRoot = FXMLLoader.load(getClass().getResource("res/fxml/main_menu_admin.fxml"));
        else
            mainMenuRoot = FXMLLoader.load(getClass().getResource("res/fxml/main_menu.fxml"));
        //Parent lookupToolRoot = FXMLLoader.load(getClass().getResource("lookup_tool.fxml"));
        mainMenu = new Scene(mainMenuRoot, 640, 480);
        myStage.setTitle("Desktop App");
        myStage.setScene(mainMenu);
        myStage.setMinWidth(640.0);
        myStage.setMinHeight(480.0);
        myStage.show();
    }
}
