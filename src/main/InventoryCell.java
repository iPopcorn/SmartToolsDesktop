package main;

import javafx.scene.control.ListCell;

/**
 * Created by mwhar on 1/11/2017.
 */
public class InventoryCell extends ListCell<Tool> {

    // Populates a list cell with a tool's information only if it contains a tool item/isn't empty
    @Override
    public void updateItem(Tool item, boolean empty)
    {
        super.updateItem(item, empty);

        if(item != null || !empty)
        {
            String description = "Name: " + item.getName() + "\n" +
                    "Address: " + item.getAddress(); // todo: extract drawer letter from address
            this.setText(description);
            this.setGraphic(null);
        }
        else {
            this.setGraphic(null);
            this.setText(null);
        }
    }
}