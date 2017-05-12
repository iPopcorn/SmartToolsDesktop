package main;

/**
 * Tool - A representation for any type of hardware tool found inside of the Senior Design Lab
 */
public class Tool {

    /** The name of the Tool object */
    private String name;
    /** The id of the Tool object */
    private String id;
    /** The toolbox address of the Tool object */
    private String address;
    /** Where or not the tool is inside its assigned toolbox */
    private boolean isHome;

    /** Constructor for Tool object, creates a Tool object
     *
     *  Tool(String name, String id, String address) - Creates a Tool object.
     *
     */
    public Tool(String name, String id, String address) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.isHome = false;
    }

    /** Constructor for Tool object, creates a Tool object
     *
     *  Tool(String name, String id, String address) - Creates a Tool object.
     *
     */
    public Tool(String name, String id, String address, boolean isHome) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.isHome = isHome;
    }

    /** Gets the name of the calling Tool object.
     *
     *  @return Returns the name of the calling Tool object.
     *
     */
    public String getName() {
        return name;
    }

    /** Sets the name of the calling Tool object. */
    public void setName(String name) {
        this.name = name;
    }

    /** Gets the ID of the calling Tool object.
     *
     *  @return Returns the ID of the calling Tool object.
     *
     */
    public String getId() {
        return id;
    }

    /** Sets the ID of the calling Tool object. */
    public void setId(String id) {
        this.id = id;
    }

    /** Gets the toolbox address of the calling Tool object.
     *
     *  @return Returns the toolbox address of the calling Tool object.
     *
     */
    public String getAddress() {
        return address;
    }

    /** Sets the toolbox address of the calling Tool object. */
    public void setAddress(String address) {
        this.address = address;
    }

    /** Gets the isHome status of the calling Tool object.
     *
     *  @return Returns the status of the Tool object, whether it is in its assigned toolbox or not.
     *
     */
    public boolean getIsHome() {
        return isHome;
    }

    /** Sets the status of whether the tool is inside its assigned toolbox of the calling Tool object. */
    public void setIsHome(boolean isHome) {
        this.isHome = isHome;
    }

    @Override
    public String toString() {
        return "Tool{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", isHome='" + isHome + '\'' +
                '}';
    }


}
