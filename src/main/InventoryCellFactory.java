package main;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class InventoryCellFactory implements Callback<ListView<Tool>, ListCell<Tool>> {

    @Override
    public ListCell<Tool> call(ListView<Tool> listview)
    {
        return new InventoryCell();
    }
}