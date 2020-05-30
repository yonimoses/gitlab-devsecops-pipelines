package com.bnhp.gitlabci.devsecops.pipeline.tools.checkmarx;

import com.bnhp.gitlabci.devsecops.pipeline.JsonUtils;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.EnginePolicy;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.util.Map;

@Data
public class CheckmarxEnginePolicy extends EnginePolicy<CheckmarxEngineResults> {
    public Integer maxHighAllowed;
    public Integer maxLowAllowed;
    public static Logger log = LoggerFactory.getLogger(CheckmarxEnginePolicy.class);

    @Override
    public boolean allowed(CheckmarxEngineResults results) {
        return false;
    }

    public String getName() {
        return "Checkmarx";
    }

    public CheckmarxEnginePolicy() {

        try {
            Map<String, Object> map = JsonUtils.parse(new ClassPathResource("tools/checkmarx/policy.json").getInputStream(), Map.class);
            maxHighAllowed = (Integer) map.get("maxHighAllowed");
            maxLowAllowed = (Integer) map.get("maxLowAllowed");
            log.debug("Loaded default policy for Checkmarx");
        } catch (Exception e) {
            log.error("Cannot load Checkmarx policy " + e.getMessage(), e);
        }
    }


}
