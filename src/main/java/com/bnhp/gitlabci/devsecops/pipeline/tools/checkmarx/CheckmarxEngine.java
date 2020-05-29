package com.bnhp.gitlabci.devsecops.pipeline.tools.checkmarx;

import com.bnhp.gitlabci.devsecops.pipeline.JsonUtils;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanResults;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Engine;
import com.bnhp.gitlabci.devsecops.pipeline.tools.snyk.SnykEngine;
import com.bnhp.gitlabci.devsecops.pipeline.tools.snyk.SnykPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component(value = "checkmarx")
public class CheckmarxEngine extends Engine<CheckmarxPolicy> {

    public static Logger log = LoggerFactory.getLogger(CheckmarxEngine.class);

    @Override
    public ScanResults parse(ScanObject object) {
        //parse and set the results
        return ScanResults.builder().high(3).medium(1).low(1).build();
    }

    @Override
    public CheckmarxPolicy policy(ScanObject object) {
            /**
             * need to decide if we allow the users to pass their own custom policy
             */
//        if(object.getPolicy() != null){
//        }
            return _defaultPolicy;
    }


    @PostConstruct
    public void postConstruct() throws IOException {
        _defaultPolicy = JsonUtils.parse(new ClassPathResource("tools/checkmarx/policy.json").getInputStream(), CheckmarxPolicy.class);
        log.debug("Loaded default policy for snyk");
    }
}
