package main;

import main.Tool;

import java.util.ArrayList;

/**
 * Created by Taylor on 4/30/2017.
 *
 * ToolList holds the array list of all the tools names of the current inventory.
 */
public class ToolList {
    ArrayList<Tool> toolList;

    public ToolList(ArrayList<Tool> myList){
        this.toolList = myList;
    }

    public ArrayList<Tool> getToolList(){
        return this.toolList;
    }

    public ArrayList<String> getToolNames(){
        System.out.println("ToolList.getToolNames() Begin");
        ArrayList<String> toolNames = new ArrayList<>();
        for (Tool tool:
             this.toolList) {
            toolNames.add(tool.getName());
        }
        System.out.println("ToolList.getToolNames() End");
        return toolNames;
    }
}
