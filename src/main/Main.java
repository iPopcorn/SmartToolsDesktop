package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/** Main - This is the main class that runs the application.*/
public class Main extends Application {

    /** Sets up the GUI that will be displayed when the application starts
     *  void start(Stage) - This method will try to load the main.fxml file. It is called when the program starts.
     *
     *  @param primaryStage The first Stage that the application uses to hold the Scene that displays the GUI.
     *
     */
    @Override
    public void start(Stage primaryStage) {

        BorderPane root = null;

        // Try to load the main layout of the entire application
        try {
            root = FXMLLoader.load(getClass().getResource("res/fxml/main.fxml"));
        } catch (IOException ioe) {
            System.err.println("{Main Class}Input/Output Exception: " + ioe.getMessage());
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            System.err.println("{Main Class} Null Pointer Exception: " + npe.getMessage());
            npe.printStackTrace();
        }

        // If the root is not null we begin the application in a maximized screen otherwise close the application
        if(root != null) {
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.setMinWidth(820.0);
            primaryStage.setMinHeight(640.0);
            primaryStage.setTitle("Smart Tools");
            primaryStage.show();
        } else {
            System.err.println("The root node was null.");
            Platform.exit();
            System.exit(-1);
        }
    }

    /** The main method of the program, it calls the method launch()
     *
     *  void main(String[]) - Calls the launch() method, and passes in the given arguments.
     *  @param args Arguments given to the program, currently doesn't explicitly handle any arguments other than
     *              defaults
     */
    public static void main(String[] args) {
        launch(args);
    }
}
