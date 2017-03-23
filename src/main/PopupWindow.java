package main;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.awt.*;


/**
 * PopupWindow is a general purpose popup window creation class that will
 * create a popup window object with the specified window title and dialog text
 * that is specified in the constructor of the object.
 *
 * The window can then be displayed by calling the object's popop() method
 * and the main application will not be interactable until the "ok" button is
 * pressed and the window is closed.
 */
public class PopupWindow {
    private String titleText;
    private String dialogText;

    public PopupWindow(String titleText, String dialogText) {
        this.titleText = titleText;
        this.dialogText = dialogText;
    }

    public void popup() {
        final Stage dialog = new Stage(); // creating the stage for the popup
        Button btnOk = new Button("Ok"); // single OK button
        Label labelDialog = new Label(dialogText); // creating the label for the dialog text

//        labelDialog.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 14)); // not needed anymore since we're using CSS style sheet

        dialog.setTitle(titleText);
        dialog.initModality(Modality.APPLICATION_MODAL);

        HBox dialogHbox = new HBox(10);
        dialogHbox.setAlignment(Pos.CENTER);

        VBox dialogVbox1 = new VBox(20);
        dialogVbox1.setAlignment(Pos.CENTER);

        Scene dialogScene = new Scene(dialogHbox, 450, 60);
        dialogScene.getStylesheets().add("/main/res/stylesheet/popup_window.css");

        dialogHbox.getStyleClass().add("background");
//        labelDialog.getStyleClass().add("dialogWindow");
        btnOk.getStyleClass().add("btnOk");
        labelDialog.getStyleClass().add("dialogText");


        dialogHbox.getChildren().add(labelDialog);
        dialogVbox1.getChildren().add(btnOk);

        btnOk.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        dialog.close();
                    }
                });

        dialogHbox.getChildren().addAll(dialogVbox1);

        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.show();

    }

}
