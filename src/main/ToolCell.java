package main;

import javafx.scene.control.ListCell;

/**
 * ToolCell - A cell (row) inside of a ListView object meant to display information about a Tool object.
 */
public class ToolCell extends ListCell<Tool> {


    @Override
    /** Updates the display of an ToolCell
     *
     */
    public void updateItem(Tool item, boolean empty)
    {
        super.updateItem(item, empty);

        // If the ToolCell inside of the ListView is occupied by a Tool object then we display its information,
        // otherwise we show nothing
        if(item != null || !empty)
        {
            // Create a string containing all of the Tool object's information
            String description = "Name: " + item.getName() + "\n" +
                    "ID: " + item.getId() + "\n" +
                    "Toolbox #: " + item.getAddress().substring(0,2) + "\n" +
                    "Drawer: " + item.getAddress().substring(2,3) + "\n" +
                    "Position #: " + item.getAddress().substring(3,5);

            // Display the Tool object's information
            this.setText(description);
            this.setGraphic(null);

            // Non-Functioning - Supposed to change the style of a ToolCell so if it was found to be inside of its
            // assigned toolbox then it had a green background and red otherwise
            if(item.getIsHome()) {
                this.getStyleClass().removeAll("list-cell-missing");
                this.getStyleClass().add("list-cell-found");
            }
            else {
                this.getStyleClass().removeAll("list-cell-found");
                this.getStyleClass().add("list-cell-missing");
            }
        }
        else {
            // Display a blank ToolCell
            this.setGraphic(null);
            this.setText(null);
        }
    }
}