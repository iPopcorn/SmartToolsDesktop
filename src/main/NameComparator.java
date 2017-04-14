package main;

import java.util.Comparator;

/**
 * NameComparator - Compares Tool objects based on their name.
 */
public class NameComparator implements Comparator<Tool> {

    /** Compares the name of 2 tools, returning 0 if equal
     *
     * int compare(Tool t1,Tool t2) - Compares the name of 2 tools and returns the integer of the comparison. It will
     * return 0 if the names are equal, a positive integer if t1's name is greater than t2's and a negative integer
     * otherwise
     *
     * @param t1 The tool to compare against tool 2
     * @param t2 The tool to be compared against.
     * @return 0 if the names are equal, a positive integer if t1's name is greater than t2's, and a negative otherwise
     * */
    public int compare(Tool t1, Tool t2){
        String name1 = t1.getName();
        String name2 = t2.getName();
        return name1.compareTo(name2);
    }
}
