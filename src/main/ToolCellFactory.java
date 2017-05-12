package main;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * ToolCellFactory - Used to create ToolCell objects that are inside of a ListView object.
 */
public class ToolCellFactory implements Callback<ListView<Tool>, ListCell<Tool>> {


    @Override
    /** Creates a new ToolCell when called by a ListView object.
     *
     *  ListCell<Tool> call(ListView<Tool> listview) - Creates a new ToolCell object when called by a requesting
     *  ListView.
     *
     *  @return Returns a new ToolCell object.
     */
    public ListCell<Tool> call(ListView<Tool> listview)
    {
        return new ToolCell();
    }
}