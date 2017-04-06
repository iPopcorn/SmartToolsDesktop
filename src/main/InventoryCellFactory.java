package main;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * Created by mwhar on 1/11/2017.
 */
public class InventoryCellFactory implements Callback<ListView<Tool>, ListCell<Tool>> {

    @Override
    public ListCell<Tool> call(ListView<Tool> listview)
    {
        return new InventoryCell();
    }
}