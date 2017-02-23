package main;

import java.util.Comparator;

/**
 * Created by Taylor on 2/17/2017.
 */
public class AddressComparator implements Comparator<Tool> {
    @Override
    public int compare(Tool t1, Tool t2){
        String add1 = t1.getAddress();
        String add2 = t2.getAddress();
        return add1.compareTo(add2);

    }
}
