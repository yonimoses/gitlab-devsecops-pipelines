package com.bnhp.gitlabci.devsecops.pipeline.tools.snyk;

import com.bnhp.gitlabci.devsecops.pipeline.JsonUtils;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.EnginePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

public class SnykEnginePolicy extends EnginePolicy<SnykEngineResults> {

    public Integer maxHighAllowed;
    public Integer maxLowAllowed;
    public static Logger log = LoggerFactory.getLogger(SnykEnginePolicy.class);

    @Override
    public boolean allowed(SnykEngineResults results) {
        log.debug("Policy max high is {} while maxHighAllowed {} ", results.getHigh(), maxHighAllowed);
        return maxHighAllowed > results.getHigh();
    }


    public String getName() {
        return "Snyk";
    }

    public SnykEnginePolicy (){

        try{
            Map<String, Object> map = JsonUtils.parse(new ClassPathResource("tools/snyk/policy.json").getInputStream(), Map.class);
            maxHighAllowed = (Integer) map.get("maxHighAllowed");
            maxLowAllowed = (Integer) map.get("maxLowAllowed");
            log.debug("Loaded default policy for snyk");
        }catch (Exception e){
            log.error("Cannot load snyk policy " + e.getMessage(), e);
        }
    }


}
