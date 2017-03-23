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
    private AddToolController addToolParent;

    public ReaderThread(String hostname, String task, Object creator){
        this.hostname = hostname;
        this.reader = new ImpinjReader();

        String parentName = creator.getClass().getSimpleName();
        if(parentName.equalsIgnoreCase("ReportController"))
            this.reportParent = (ReportController) creator;
        else if(parentName.equalsIgnoreCase("AddToolController"))
            this.addToolParent = (AddToolController) creator;
        else if(parentName.equalsIgnoreCase("LookupController"))
            this.lookupParent = (LookupController) creator;

        if(task.equalsIgnoreCase("generate_report")){
            this.runCondition = 1;
        } else if(task.equalsIgnoreCase("add_tool")){
            this.runCondition = 2;
        } else if(task.equalsIgnoreCase("lookup_tool")){
            this.runCondition = 3;
        } else if(task.equalsIgnoreCase("test_case")) {
            this.runCondition = 4;
        } else if(task.equalsIgnoreCase("delete_tool")){
            this.runCondition = 5;
        }
        else{
            this.runCondition = 0;
        }
    }

    /**
     * stopReader() is the function that is called by MainController to manually stop and disconnect from
     * the reader. This is where the data structures from the listener objects get placed into the ReaderThread object,
     * so that they can be accessed by MainController.
     * **/
    public void stopReader(){
        System.out.printf("stopReader()\n");
        try{
            if(this.reader.isConnected()){
                this.reader.stop();
                this.reader.disconnect();
            }
        }catch(Exception e){e.printStackTrace();}
        System.out.printf("Reader Stopped.\n");
        switch(runCondition){
            case 0:{ // default case
                System.out.println("This should never be reached");
                break;
            }
            case 1:{ // generate report
                System.out.println("generate_report processing...");
                ReportListener myListener = (ReportListener) this.reader.getTagReportListener();
                this.setGenReportTagValues(myListener.getReportList());
                break;
            }
            case 2:{ // add tool
                System.out.println("add tool processing...");
                break;
            }
            case 3:{ // lookup tool
                System.out.println("lookup tool processing...");
                break;
            }
            case 4:{
                System.out.println("test case processing...");
                break;
            }
            case 5:{ // delete tool
                System.out.println("delete tool processing...");
                break;
            }
        }

        System.out.println("stopReader() end!");
    }

    /**
     * This is the main function of the ReaderThread class.
     *
     * run() configures the settings for the reader, and starts it.
     * TODO: figure out how to stop the reader.
     * TODO: figure out how to control when reader starts and stops.
     * **/
    public void run(){
        this.running = true;

        System.out.println("Starting ReaderThread...");
        System.out.println("Connecting to: " + this.hostname);

        try{
                // connect to the reader
                this.reader.connect(this.hostname);

                // configure settings
                // TODO: Learn about settings and figure out which settings we need.
                Settings mySettings = reader.queryDefaultSettings();
                this.reader.applySettings(mySettings);
            } catch(Exception e){
                e.printStackTrace();
                switch(runCondition){
                    case 1:{ // generate report
                        // this.reportParent.showError("Reader Failed To Connect!");
                        PopupWindow error = new PopupWindow("Error", "Reader failed to connect!");
                        error.popup();
                    }case 2:{ // add tool

                    }case 3:{ // lookup tool

                    }
                }
            }
        try{
            System.out.printf("ReaderThread.runCondition = %d\n", runCondition);
            switch(runCondition){
                case 0:{ // default case
                    System.out.printf("incorrect task string used.");
                    break;
                }case 1:{ // generate report

                    // connect a listener
                    this.reader.setTagReportListener(new ReportListener());

                    // start reader
                    this.reader.start();
                    System.out.println("generate_report reader started!");

                    break;
                }case 2:{ // add tool
                    // connect a listener
                    this.reader.setTagReportListener(new ReaderListener());

                    // start reader
                    this.reader.start();
                    System.out.println("add_tool reader started!");

                    long start = System.currentTimeMillis();
                    // fail safe timer set to 5 minutes
                    long end = start + (1000 * 1); // 60 seconds * 1000 ms/sec
                    while (System.currentTimeMillis() < end)
                    {
                        // run
                    }
                    this.reader.stop();

                    ReaderListener myListener = (ReaderListener) reader.getTagReportListener();

                    this.setTagValues(myListener.getTagValues());
                    break;
                }case 3:{ // lookup tool
                    break;
                }case 4:{ // test case
                    // connect a listener
                    this.reader.setTagReportListener(new ReaderListener());

                    // start reader
                    this.reader.start();
                    System.out.println("add_tool reader started!");

                    long start = System.currentTimeMillis();
                    // fail safe timer set to 5 minutes
                    long end = start + (1000 * 1); // 60 seconds * 1000 ms/sec
                    while (System.currentTimeMillis() < end)
                    {
                        // run
                    }
                    this.reader.stop();

                    ReaderListener myListener = (ReaderListener) reader.getTagReportListener();

                    this.setTagValues(myListener.getTagValues());
                    break;
                }case 5:{ // delete tool
                    // connect a listener
                    this.reader.setTagReportListener(new ReaderListener());

                    // start reader
                    this.reader.start();
                    System.out.println("add_tool reader started!");

                    long start = System.currentTimeMillis();
                    // run for half a second
                    long end = start + (500);
                    while (System.currentTimeMillis() < end)
                    {
                        // run
                    }
                    this.reader.stop();

                    ReaderListener myListener = (ReaderListener) reader.getTagReportListener();

                    this.setTagValues(myListener.getTagValues());
                    break;
                }
            }

        }catch(OctaneSdkException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
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
