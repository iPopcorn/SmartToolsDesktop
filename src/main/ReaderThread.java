package main;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.Settings;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Taylor on 1/3/2017.
 */
public class ReaderThread extends Thread {
    private String hostname;
    private ImpinjReader reader;
    private HashMap<String, Integer> tagValues;
    private ArrayList<String> genReportTagValues;
    private int runCondition;
    private boolean running;
    private ReportController reportParent;
    private LookupController lookupParent;
    private ReplaceToolController addToolParent;
    private DeleteToolController deleteParent;

    public ReaderThread(String hostname, String task, Object creator) {
        this.hostname = hostname;
//        this.hostname = "192.168.1.3";
        this.reader = new ImpinjReader();
        //this.hostname = "197.168.1.3";
        this.reader = new ImpinjReader(this.hostname, "MyReader", 500);
        this.reader.setConnectTimeout(500);

        String parentName = creator.getClass().getSimpleName();
        if (parentName.equalsIgnoreCase("ReportController"))
            this.reportParent = (ReportController) creator;
        else if (parentName.equalsIgnoreCase("ReplaceToolController"))
            this.addToolParent = (ReplaceToolController) creator;
        else if (parentName.equalsIgnoreCase("LookupController"))
            this.lookupParent = (LookupController) creator;
        else if (parentName.equalsIgnoreCase("DeleteToolController"))
            this.deleteParent = (DeleteToolController) creator;

        if (task.equalsIgnoreCase("generate_report")) {
            this.runCondition = 1;
        } else if (task.equalsIgnoreCase("add_tool")) {
            this.runCondition = 2;
        } else if (task.equalsIgnoreCase("lookup_tool")) {
            this.runCondition = 3;
        } else if (task.equalsIgnoreCase("test_case")) {
            this.runCondition = 4;
        } else if (task.equalsIgnoreCase("delete_tool")) {
            this.runCondition = 5;
        } else {
            this.runCondition = 0;
        }
    }

    /**
     * stopReader() is the function that is called by MainController to manually stop and disconnect from
     * the reader. This is where the data structures from the listener objects get placed into the ReaderThread object,
     * so that they can be accessed by MainController.
     **/
    public void stopReader() {
        System.out.printf("stopReader()\n");
        try {
            System.out.println("Check if the reader is connected.");
            if (this.reader.isConnected()) {
                this.reader.stop();
                this.reader.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.printf("Reader Stopped.\n");
        switch (runCondition) {
            case 0: { // default case
                System.out.println("This should never be reached");
                break;
            }
            case 1: { // generate report
                System.out.println("generate_report processing...");
                ReportListener myListener = (ReportListener) this.reader.getTagReportListener();
                this.setGenReportTagValues(myListener.getReportList());
                break;
            }
            case 2: { // add tool
                System.out.println("add tool processing...");
                break;
            }
            case 3: { // lookup tool
                System.out.println("lookup tool processing...");
                break;
            }
            case 4: {
                System.out.println("test case processing...");
                break;
            }
            case 5: { // delete tool
                System.out.println("delete tool processing...");
                break;
            }
        }

        System.out.println("stopReader() end!");
    }

    /**
     * This is the main function of the ReaderThread class.
     * <p>
     * run() configures the settings for the reader, and starts it.
     * TODO: figure out how to stop the reader.
     * TODO: figure out how to control when reader starts and stops.
     **/
    public void run() {
        this.running = true;

        System.out.println("Starting ReaderThread...");
        System.out.println("Connecting to: " + this.hostname);
        Settings mySettings = null;
        try {
            // connect to the reader
            //this.reader.connect(this.hostname);
            //this.reader.connect()
            this.reader.connect();

            // configure settings
            // TODO: Learn about settings and figure out which settings we need.
            mySettings = reader.queryDefaultSettings();
            //short[] disable_ants = {2};
            //mySettings.getAntennas().disableById(disable_ants);
            this.reader.applySettings(mySettings);
        }
        catch (OctaneSdkException e) {
            System.out.println(e.getMessage());
            switch (runCondition) {
                case 1: { // generate report
                    this.reportParent.scannerConnectionError();
                }
                case 2: { // add tool
                    this.addToolParent.scannerConnectionError();
                }
                case 3: { // lookup tool
                    this.lookupParent.scannerConnectionError();
                }
                case 5: { // delete tool
                    this.deleteParent.scannerConnectionError();
                }
            }
        }
        try {
            System.out.printf("ReaderThread.runCondition = %d\n", runCondition);
            switch (runCondition) {
                case 0: { // default case

                    mySettings.getAntennas().getAntenna(2).setEnabled(false);
                    mySettings.getAntennas().getAntenna(3).setEnabled(false);

//                    short[] disable_ants = {2,3};
                    this.reader.applySettings(mySettings);

                    System.out.printf("incorrect task string used.");
                    break;
                }
                case 1: { // generate report

                    short[] disable_ants = {1};
                    mySettings.getAntennas().disableById(disable_ants);
                    this.reader.applySettings(mySettings);

                    // connect a listener
                    this.reader.setTagReportListener(new ReportListener());

                    // start reader
                    this.reader.start();
                    System.out.println("generate_report reader started!");

                    break;
                }
                case 2: { // add tool

                    System.out.println("HEREEEE");
                    Settings mySettings2 = reader.queryDefaultSettings();
                    mySettings2.getAntennas().getAntenna(2).setEnabled(false);
                    mySettings2.getAntennas().getAntenna(3).setEnabled(false);

//                    short[] disable_ants = {2,3};
                    this.reader.applySettings(mySettings2);

                    // connect a listener
                    this.reader.setTagReportListener(new ReaderListener());

                    // start reader
                    this.reader.start();
                    System.out.println("add_tool reader started!");

                    long start = System.currentTimeMillis();
                    // fail safe timer set to 5 minutes
                    long end = start + (1000 * 1); // 60 seconds * 1000 ms/sec
                    while (System.currentTimeMillis() < end) {
                        // run
                    }
                    this.reader.stop();

                    ReaderListener myListener = (ReaderListener) reader.getTagReportListener();

                    this.setTagValues(myListener.getTagValues());
                    break;
                }
                case 3: { // lookup tool

                    short[] disable_ants = {2,3};
                    mySettings.getAntennas().disableById(disable_ants);
                    this.reader.applySettings(mySettings);

                    // connect a listener
                    this.reader.setTagReportListener(new ReaderListener());

                    // start reader
                    this.reader.start();
                    System.out.println("add_tool reader started!");

                    long start = System.currentTimeMillis();
                    // fail safe timer set to 5 minutes
                    long end = start + (1000 * 1); // 60 seconds * 1000 ms/sec
                    while (System.currentTimeMillis() < end) {
                        // run
                    }
                    this.reader.stop();

                    ReaderListener myListener = (ReaderListener) reader.getTagReportListener();

                    this.setTagValues(myListener.getTagValues());
                    break;
                }
                case 4: { // test case

                    short[] disable_ants = {2,3};
                    mySettings.getAntennas().disableById(disable_ants);
                    this.reader.applySettings(mySettings);

                    // connect a listener
                    this.reader.setTagReportListener(new ReaderListener());

                    // start reader
                    this.reader.start();
                    System.out.println("add_tool reader started!");

                    long start = System.currentTimeMillis();
                    // fail safe timer set to 5 minutes
                    long end = start + (1000 * 1); // 60 seconds * 1000 ms/sec
                    while (System.currentTimeMillis() < end) {
                        // run
                    }
                    this.reader.stop();

                    ReaderListener myListener = (ReaderListener) reader.getTagReportListener();

                    this.setTagValues(myListener.getTagValues());
                    break;
                }
                case 5: { // delete tool

                    short[] disable_ants = {2,3};
                    mySettings.getAntennas().disableById(disable_ants);
                    this.reader.applySettings(mySettings);

                    // connect a listener
                    this.reader.setTagReportListener(new ReaderListener());

                    // start reader
                    this.reader.start();
                    System.out.println("add_tool reader started!");

                    long start = System.currentTimeMillis();
                    // run for half a second
                    long end = start + (500);
                    while (System.currentTimeMillis() < end) {
                        // run
                    }
                    this.reader.stop();

                    ReaderListener myListener = (ReaderListener) reader.getTagReportListener();

                    this.setTagValues(myListener.getTagValues());
                    break;
                }
            }

        } catch (OctaneSdkException ose) {
            ose.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Integer> getTagValues() {
        return tagValues;
    }

    public void setTagValues(HashMap<String, Integer> tagValues) {
        this.tagValues = tagValues;
    }

    public ArrayList<String> getGenReportTagValues() {
        return genReportTagValues;
    }

    public void setGenReportTagValues(ArrayList<String> genReportTagValues) {
        this.genReportTagValues = genReportTagValues;
    }
}
