package main;

/**
 * Created by mwhar on 1/10/2017.
 */
public class Tool {

    private String name;
    private String id;
    private String address;
    private boolean isHome;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Tool(String name, String id, String address) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.isHome = false;
    }

    public Tool(String name, String id, String address, boolean isHome) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.isHome = isHome;
    }

    @Override
    public String toString() {
        return "Tool{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public void setIsHome(boolean isHome) {
        this.isHome = isHome;
    }

    public boolean getIsHome() {
        return isHome;
    }
}
