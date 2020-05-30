package com.bnhp.gitlabci.devsecops.pipeline;

import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PipelineFinalizer {
    public static Logger log = LoggerFactory.getLogger(PipelineFinalizer.class);

    public static void exit(ScanObject object) {
        if (object.getFailOnScanError() && !object.getResults().getSuccess()) {
            log.error("BNHP_FAIL_PIPELINE_WHEN_SCAN_FAILED is true and scan result was false. exiting with code 1");
            System.exit(1);
        }
    }
}
