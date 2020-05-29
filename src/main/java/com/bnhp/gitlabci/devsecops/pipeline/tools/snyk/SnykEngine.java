package com.bnhp.gitlabci.devsecops.pipeline.tools.snyk;

import com.bnhp.gitlabci.devsecops.pipeline.InfoUtils;
import com.bnhp.gitlabci.devsecops.pipeline.JsonUtils;
import com.bnhp.gitlabci.devsecops.pipeline.exceptions.EngineParseException;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ReportObject;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanResults;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Engine;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.tools.checkmarx.CheckmarxPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component(value = "snyk")
public class SnykEngine extends Engine<SnykPolicy> {

    public static Logger log = LoggerFactory.getLogger(SnykEngine.class);

    @Override
    public ScanResults parse(ScanObject object) throws EngineParseException {

        log.info("Snyk is starting.");
        Optional<ReportObject> o = object.getReports().stream().filter(ReportObject::isJson).findFirst();
        if (!o.isPresent()) {
            throw new EngineParseException("Can't find a json to parse. reports didnt include a content type of json");
        } else {
            Map<String, Object> map = JsonUtils.parse(o.get().getContent(), Map.class);
            ArrayList vulnerabilities =  ((ArrayList)map.get("vulnerabilities"));
            return ScanResults.builder()
                    .summary(map.get("summary").toString())
                    .technology(map.get("packageManager").toString())
                    .total(vulnerabilities.size())
                    .high(vulnerabilities.stream().filter(a->((LinkedHashMap)a).get("severity").equals("medium")).count())
                    .medium(vulnerabilities.stream().filter(a->((LinkedHashMap)a).get("severity").equals("medium")).count()).
                    low(vulnerabilities.stream().filter(a->((LinkedHashMap)a).get("severity").equals("medium")).count()).build();
        }

    }


    @Override
    public SnykPolicy policy(ScanObject object) {
        /**
         * need to decide if we allow the users to pass their own custom policy
         */
//        if(object.getPolicy() != null){
//        }
        return _defaultPolicy;
    }


    @PostConstruct
    public void postConstruct() throws IOException {
        _defaultPolicy = JsonUtils.parse(new ClassPathResource("tools/snyk/policy.json").getInputStream(), SnykPolicy.class);
        log.debug("Loaded default policy for snyk");
    }

}
