package com.bnhp.gitlabci.devsecops.pipeline.tools.aqua;

import com.bnhp.gitlabci.devsecops.pipeline.JsonUtils;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.EnginePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Map;

public class AquaEnginePolicy extends EnginePolicy<AquaEngineResults> {
    public Integer maxHighAllowed = 0;
    public Integer maxLowAllowed  = 10;
    public static Logger log = LoggerFactory.getLogger(AquaEnginePolicy.class);

    public AquaEnginePolicy() {
        try{
            Map<String, Object> map = JsonUtils.parse(new ClassPathResource("tools/aqua/policy.json").getInputStream(), Map.class);
            maxHighAllowed = (Integer) map.get("maxHighAllowed");
            maxLowAllowed = (Integer) map.get("maxLowAllowed");
            log.debug("Loaded default policy for aqua");
        }catch (Exception e){
            log.error("Cannot load aqua policy " + e.getMessage(), e);
        }

    }

    @Override
    public boolean allowed(AquaEngineResults results) {
        log.debug("Policy max high is {} while maxHighAllowed {} ", results.getHigh(), maxHighAllowed);
        return maxHighAllowed > results.getHigh();
    }

    @Override
    public String getName() {
        return "aqua";
    }

}
