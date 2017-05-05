package main;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taylor on 1/10/2017.
 *
 * The ReportListener is a TagReportListener implementation meant to be used with the Generate Report use case.
 */
public class ReportListener implements TagReportListener {
    private ArrayList<String> reportList;
    public ReportListener(){
        this.reportList = new ArrayList<>();
    }
    // todo: optimize handling duplicates
    public void onTagReported(ImpinjReader reader, TagReport report){
        List tagList = report.getTags();
        Tag tempTag;
        String curEPC;

        for(int i = 0; i < tagList.size(); i++){
            // get the tag from the tag list, and grab the epc from the tag
            tempTag = (Tag) tagList.get(i);
            curEPC = tempTag.getEpc().toString();

            if(!this.containsEPC(curEPC)){ // if we haven't read this EPC before
                System.out.println("Read New Tag: " + curEPC);
                this.reportList.add(curEPC);
            }
        }
    }

    public ArrayList<String> getReportList() {
        return reportList;
    }

    public void setReportList(ArrayList<String> tagList) {
        this.reportList = tagList;
    }

    private boolean containsEPC(String mEPC){
        boolean flag = false;
        for(String cur: this.reportList){
            if(cur.equalsIgnoreCase(mEPC)){
                flag = true;
            }
        }
        return flag;
    }
}
