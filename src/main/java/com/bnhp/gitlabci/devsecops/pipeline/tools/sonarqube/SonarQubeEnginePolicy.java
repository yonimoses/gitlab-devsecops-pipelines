package com.bnhp.gitlabci.devsecops.pipeline.tools.sonarqube;

import com.bnhp.gitlabci.devsecops.pipeline.JsonUtils;
import com.bnhp.gitlabci.devsecops.pipeline.tools.aqua.AquaEnginePolicy;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.EnginePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Map;

public class SonarQubeEnginePolicy extends EnginePolicy<SonarQubeEngineResults> {

    public Integer maxHighAllowed;
    public Integer maxLowAllowed;
    public static Logger log = LoggerFactory.getLogger(AquaEnginePolicy.class);

    @Override
    public boolean allowed(SonarQubeEngineResults results) {
        return false;
    }

    @Override
    public String getName() {
        return "sonar";
    }

    public SonarQubeEnginePolicy (){

//        try{
//            Map<String, Object> map = JsonUtils.parse(new ClassPathResource("tools/sonar/policy.json").getInputStream(), Map.class);
//            maxHighAllowed = (Integer) map.get("maxHighAllowed");
//            maxLowAllowed = (Integer) map.get("maxLowAllowed");
//            log.debug("Loaded default policy for snyk");
//        }catch (Exception e){
//            log.error("Cannot load snyk policy " + e.getMessage(), e);
//        }
    }
}
