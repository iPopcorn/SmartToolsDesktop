package main;

import java.util.Comparator;

/**
 * AddressComparator Class
 */
public class AddressComparator implements Comparator<Tool> {

    /** Compares the addresses of two tools returning zero if equal.
     *
     * public int compare(Tool t1, Tool t2) - Compares the addresses of two tools and returns an integer of the
     * comparison. Returns zero if the addresses were equal, a positive integer if the tool1's address is greater than
     * tool2's, and a negative integer if tool1's address is less than tool2's.
     * @param t1 The tool to compare against Tool 2.
     * @param t2 The tool to be compared against.
     * @return Zero if the addresses are equal, positive if Tool1's address > Tool2's, negative otherwise.
     */
    @Override
    public int compare(Tool t1, Tool t2){
        String add1 = t1.getAddress();
        String add2 = t2.getAddress();
        return add1.compareTo(add2);

    }
}
