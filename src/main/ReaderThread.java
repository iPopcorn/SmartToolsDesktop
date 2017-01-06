package main;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.Settings;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Taylor on 1/3/2017.
 */
public class ReaderThread extends Thread {
    private String hostname;
    private ImpinjReader reader;

    public ReaderThread(String hostname){
        this.hostname = hostname;
        this.reader = new ImpinjReader();
    }

    /**
     * This is the main function of the ReaderThread class.
     *
     * run() configures the settings for the reader, and starts it.
     * TODO: figure out how to stop the reader.
     * TODO: figure out how to control when reader starts and stops.
     * **/
    public void run(){
        try{
            System.out.println("Starting ReaderThread...");
            System.out.println("Connecting to: " + this.hostname);

            // connect to the reader
            this.reader.connect(this.hostname);

            // configure settings
            // TODO: Learn about settings and figure out which settings we need.
            Settings mySettings = reader.queryDefaultSettings();
            this.reader.applySettings(mySettings);

            // connect a listener
            this.reader.setTagReportListener(new ReaderListener());

            // start reader
            this.reader.start();
            long start = System.currentTimeMillis();
            long end = start + 1000; // 60 seconds * 1000 ms/sec
            while (System.currentTimeMillis() < end)
            {
                // run
            }
            this.reader.stop();


        }catch(OctaneSdkException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }
}
