package main;

import com.impinj.octane.ImpinjReader;
import com.impinj.octane.Tag;
import com.impinj.octane.TagReport;
import com.impinj.octane.TagReportListener;

import java.util.List;

/**
 * Created by Taylor on 1/3/2017.
 */
public class ReaderListener implements TagReportListener {

    public ReaderListener(){}

    public void onTagReported(ImpinjReader reader, TagReport report){
        List tagList = report.getTags();
        Tag tempTag;
        for(int i = 0; i < tagList.size(); i++){
            tempTag = (Tag) tagList.get(i);
            System.out.printf("Tag ID: %s\nTag EPC: %s\n",tempTag.getTid().toString(),tempTag.getEpc().toString());
        }
    }
}
