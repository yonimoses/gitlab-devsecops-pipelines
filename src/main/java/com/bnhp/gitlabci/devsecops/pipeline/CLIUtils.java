package com.bnhp.gitlabci.devsecops.pipeline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CLIUtils {
    public static Logger log = LoggerFactory.getLogger(CLIUtils.class);

    public static void error(String msg){
        log.error("**********************************");
        log.error("ERROR {}", msg);
        log.error("**********************************");

    }
}
