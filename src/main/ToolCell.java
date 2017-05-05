package main;

import javafx.scene.control.ListCell;

/**
 * Created by mwhar on 1/11/2017.
 */

public class ToolCell extends ListCell<Tool> {

    // Populates a list cell with a tool's information only if it contains a tool item/isn't empty
    @Override
    public void updateItem(Tool item, boolean empty)
    {
        super.updateItem(item, empty);

        if(item != null || !empty)
        {
            String description = "Name: " + item.getName() + "\n" +
                    "ID: " + item.getId() + "\n" +
                    "Toolbox #: " + item.getAddress().substring(0,2) + "\n" +
                    "Drawer: " + item.getAddress().substring(2,3) + "\n" +
                    "Position #: " + item.getAddress().substring(3,5);
            this.setText(description);
            this.setGraphic(null);
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
            this.setGraphic(null);
            this.setText(null);
        }
    }
}