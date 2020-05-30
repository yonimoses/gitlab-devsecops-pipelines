package com.bnhp.gitlabci.devsecops.pipeline.tools.checkmarx;

import com.bnhp.gitlabci.devsecops.pipeline.exceptions.EngineParseException;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckmarxEngine extends Engine<CheckmarxEnginePolicy, CheckmarxEngineResults> {

    public static Logger log = LoggerFactory.getLogger(CheckmarxEngine.class);
    public  CheckmarxEngineResults results;
    public  CheckmarxEnginePolicy policy = new CheckmarxEnginePolicy();


    @Override
    public void doRun() throws EngineParseException {
        results = parse();
        object.setResults(results);
        object.getResults().setSuccess(policy(object).allowed(results));
    }


    public CheckmarxEngineResults parse() {
        //parse and set the results
        return CheckmarxEngineResults.builder().high(3).medium(1).low(1).build();
    }

    @Override
    public CheckmarxEnginePolicy policy(ScanObject object) {
        /**
         * need to decide if we allow the users to pass their own custom policy
         */
//        if(object.getPolicy() != null){
//        }
        return policy;
    }


}
