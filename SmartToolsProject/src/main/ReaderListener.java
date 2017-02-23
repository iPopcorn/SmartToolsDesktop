package main;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Taylor on 1/3/2017.
 */
public class ReaderListener implements TagReportListener {
    public int count = 0;
    HashMap<String,Integer> tagValues = new HashMap();
    ArrayList<String> myTags = new ArrayList();
    public ReaderListener(){}

    public void onTagReported(ImpinjReader reader, TagReport report){
        List tagList = report.getTags();
        Tag tempTag;
        String curEPC;

        for(int i = 0; i < tagList.size(); i++){
            // get the tag from the tag list, and grab the epc from the tag
            tempTag = (Tag) tagList.get(i);
            curEPC = tempTag.getEpc().toString();

            if(tagValues.containsKey(curEPC)){ //if HashMap contains key, increment value
                int temp = tagValues.get(curEPC);
                temp++;
                tagValues.put(curEPC, temp);
            }else{ //else set to 1
                tagValues.put(curEPC, 1);
            }
           // System.out.printf("%d Tag ID: %s\nTag EPC: %s\n",count,tempTag.getTid().toString(),tempTag.getEpc().toString());
        }
    }

    public HashMap<String, Integer> getTagValues() {
        return tagValues;
    }

    public void setTagValues(HashMap<String, Integer> tagValues) {
        this.tagValues = tagValues;
    }
}
