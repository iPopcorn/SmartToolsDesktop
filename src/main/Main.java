package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/** Main - This is the main class that runs the application.*/
public class Main extends Application {

    public static ToolList inventoryList;

    /**
     * ArrayList<Tool> handleResponse(String response)
     * This function takes in the String response after querying the server to see the response
     * received from the server and handle it accordingly. If the response contains fail, the appropriate
     * failure message popup window is created and an empty ArrayList of the tool object is returned.
     * Else if the response was not a failure (1 or more tools matching the criteria was found) the
     * response is a JSON string that is then parsed to create the tool object(s) received and
     * put into the ArrayList and returned.
     *
     * TODO: Duplicated code from ModifyInventoryController.java - Refactor this
     */
    private ArrayList<Tool> handleResponse(String response) {
        System.out.println("Main.handleResponse() Begin");
        ArrayList<Tool> foundTools = new ArrayList<>(); // array list to be returned
        JSONdecoder responseDecoder; // JSON decoder, only initialized if the response is a JSON string

        if (response != null && !response.isEmpty()) {
            if (response.contains("fail")) { // if failure code
                if (response.equalsIgnoreCase("fail")) { // if the query fails with unknown reason (bad connection, error in server)
                    PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
                    popupWindow.popup();
                } else if (response.equalsIgnoreCase("fail: no_results")) { // if the query fails because no results found
                    PopupWindow popupWindow = new PopupWindow("Lookup Tool", "No tool(s) match the specified criteria");
                    popupWindow.popup();
                } else { // if the query fails with unknown reason (bad connection, error in server)
                    PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
                    popupWindow.popup();
                }

            } else { // if the response isn't a fail message
                responseDecoder = new JSONdecoder();
                foundTools = responseDecoder.decodeJSONToolListResponse(response);
            }
        } else { // if the query fails with unknown reason (bad connection, error in server)
            PopupWindow popupWindow = new PopupWindow("Lookup Tool", "Error processing the request");
            popupWindow.popup();
        }
        System.out.println("Main.handleResponse() End");
        return foundTools;
    }

    public void setToolList(){
        System.out.println("Main.setToolList() begin");
        // Query database for a list of all the tools currently in the inventory
        // This query will grab from the belongs_here column of the address table
        ServerRequest serverRequest = new ServerRequest();
        ArrayList<Tool> toolList = new ArrayList<>();
        HashMap<String, String> POSTdata = new HashMap<>();
        POSTdata.put("action","get-tool-list");

        String response = serverRequest.getResponseFromRequest("tool-handling/tool-list.php",POSTdata);
        toolList = handleResponse(response);

        ToolList listOfTools = new ToolList(toolList);
        this.inventoryList = listOfTools;
        System.out.println("Main.setToolList() end");

    }
    /** Sets up the GUI that will be displayed when the application starts
     *  void start(Stage) - This method will try to load the main.fxml file. It is called when the program starts.
     *
     *  @param primaryStage The first Stage that the application uses to hold the Scene that displays the GUI.
     *
     */
    @Override
    public void start(Stage primaryStage) {

        this.setToolList();
        BorderPane root = null;

        // Try to load the main layout of the entire application
        try {
            root = FXMLLoader.load(getClass().getResource("res/fxml/main.fxml"));
        } catch (IOException ioe) {
            System.err.println("Input/Output Exception: " + ioe.getMessage());
        } catch (NullPointerException npe) {
            System.err.println("Null Pointer Exception: " + npe.getMessage());
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
