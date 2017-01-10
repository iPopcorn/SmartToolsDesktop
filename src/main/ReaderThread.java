package main;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.Settings;

import java.io.Reader;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Taylor on 1/3/2017.
 */
public class ReaderThread extends Thread {
    private String hostname;
    private ImpinjReader reader;
    private HashMap<String, Integer> TagValues;
    private int runCondition;
    private boolean running;

    public ReaderThread(String hostname, String task){
        this.hostname = hostname;
        this.reader = new ImpinjReader();

        if(task.equalsIgnoreCase("generate_report")){
            this.runCondition = 1;
        } else if(task.equalsIgnoreCase("lookup_tool")){
            this.runCondition = 2;
        } else if(task.equalsIgnoreCase("add_tool")){
            this.runCondition = 3;
        } else{
            this.runCondition = 0;
        }
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
        }
            try{
                switch(runCondition){
                    case 0:{ // default case
                        System.out.printf("incorrect task string used.");
                        break;
                    }case 1:{ // generate report

                        // connect a listener
                        this.reader.setTagReportListener(new ReportListener());

                        // start reader
                        this.reader.start();
                        System.out.println("Reader started!");

                        break;
                    }case 2:{ // add tool
                        // connect a listener
                        this.reader.setTagReportListener(new ReaderListener());

                        // start reader
                        this.reader.start();
                        System.out.println("Connected!");

                        long start = System.currentTimeMillis();
                        // fail safe timer set to 5 minutes
                        long end = start + (1000 * 300); // 60 seconds * 1000 ms/sec
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
        return TagValues;
    }

    public void setTagValues(HashMap<String, Integer> tagValues) {
        TagValues = tagValues;
    }

    public void stopReader(){
        System.out.printf("stopReader()");
        try{
            if(this.reader.isConnected()){
                this.reader.stop();
            }
        }catch(Exception e){e.printStackTrace();}
        System.out.printf("Reader Stopped.");
    }
}
