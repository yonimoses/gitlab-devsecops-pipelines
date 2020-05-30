package com.bnhp.gitlabci.devsecops.pipeline.objects;

import com.bnhp.gitlabci.devsecops.pipeline.tools.base.EnginePolicy;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.EngineResults;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScanObject {


    public static Logger log = LoggerFactory.getLogger(ScanObject.class);

    public ScanSource source;
    public String[] recipients;
    public String projectUrl;
    public String buildUrl;
    public Boolean failOnScanError;
    public Boolean notify;
    public String date;
    public String commit;
    public String branch;
    public List<ReportObject> reports = new ArrayList<>();
    public EngineResults results;
    public EnginePolicy enginePolicy;

    public ScanObject() {

    }

    public ScanObject(ScanSource source)  {
        this.source = source;
    }

}
