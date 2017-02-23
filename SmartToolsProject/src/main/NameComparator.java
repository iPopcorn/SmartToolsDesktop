package main;

import java.util.Comparator;

/**
 * Created by Taylor on 2/17/2017.
 */
public class NameComparator implements Comparator<Tool> {


    public int compare(Tool t1, Tool t2){
        String name1 = t1.getName();
        String name2 = t2.getName();
        return name1.compareTo(name2);
    }
}
