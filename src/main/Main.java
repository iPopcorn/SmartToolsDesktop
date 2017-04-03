package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("res/fxml/main.fxml"));
        } catch (IOException ioe) {
            System.err.println("Input/Output Exception: " + ioe.getMessage());
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            System.err.println("Null Pointer Exception: " + npe.getMessage());
        }

        if(root != null) {
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        } else {
            System.err.println("The root node was null.");
            Platform.exit();
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
