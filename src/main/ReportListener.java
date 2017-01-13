package main;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Taylor on 1/10/2017.
 *
 * The ReportListener is a TagReportListener implementation meant to be used with the Generate Report use case.
 */
public class ReportListener implements TagReportListener {
    private ArrayList<String> tagList;
    public ReportListener(){
        this.tagList = new ArrayList<>();
    }
    // todo: handle duplicate tags
    public void onTagReported(ImpinjReader reader, TagReport report){
        List tagList = report.getTags();
        Tag tempTag;
        String curEPC;

        for(int i = 0; i < tagList.size(); i++){
            // get the tag from the tag list, and grab the epc from the tag
            tempTag = (Tag) tagList.get(i);
            curEPC = tempTag.getEpc().toString();

            tagList.add(curEPC);
        }
    }

    public ArrayList<String> getTagList() {
        return tagList;
    }

    public void setTagList(ArrayList<String> tagList) {
        this.tagList = tagList;
    }
}
