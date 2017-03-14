package main;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by gabinoluna on 3/14/17.
 */
public class PopupWindow {
    private String titleText;
    private String dialogText;

    public PopupWindow (String titleText, String dialogText) {
        this.titleText = titleText;
        this.dialogText = dialogText;
    }

    public void popup() {
        final Stage dialog = new Stage();
        Button btnOk = new Button("Ok");
        Label labelDialog = new Label(dialogText);
        labelDialog.setFont(Font.font(null, FontWeight.BOLD, 14));

        dialog.setTitle(titleText);
        dialog.initModality(Modality.APPLICATION_MODAL);

        HBox dialogHbox = new HBox(20);
        dialogHbox.setAlignment(Pos.CENTER);

        VBox dialogVbox1 = new VBox(40);
        dialogVbox1.setAlignment(Pos.CENTER);



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
        Scene dialogScene = new Scene(dialogHbox, 500, 60);
        dialog.setScene(dialogScene);
        dialog.show();




    }



}
