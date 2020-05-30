package com.bnhp.gitlabci.devsecops.pipeline.tools.snyk;

import com.bnhp.gitlabci.devsecops.pipeline.JsonUtils;
import com.bnhp.gitlabci.devsecops.pipeline.exceptions.EngineParseException;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ReportObject;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class SnykEngine extends Engine<SnykEnginePolicy, SnykEngineResults> {

    private SnykEngineResults results;
    private SnykEnginePolicy policy = new SnykEnginePolicy();
    public static Logger log = LoggerFactory.getLogger(SnykEngine.class);


    @Override
    public void doRun() throws EngineParseException {
        results = parse();
        object.setResults(results);
        object.getResults().setSuccess(policy(object).allowed(results));
    }


    public SnykEngineResults parse() throws EngineParseException {

        log.info("Snyk is starting.");
        Optional<ReportObject> o = object.getReports().stream().filter(ReportObject::isJson).findFirst();
        if (!o.isPresent()) {
            throw new EngineParseException("Can't find a json to parse. reports didnt include a content type of json");
        } else {
            Map<String, Object> map = JsonUtils.parse(o.get().getContent(), Map.class);
            ArrayList vulnerabilities = ((ArrayList) map.get("vulnerabilities"));
            return SnykEngineResults.builder()
                    .summary(map.get("summary").toString())
                    .technology(map.get("packageManager").toString())
                    .total(vulnerabilities.size())
                    .high(vulnerabilities.stream().filter(a -> ((LinkedHashMap) a).get("severity").equals("medium")).count())
                    .medium(vulnerabilities.stream().filter(a -> ((LinkedHashMap) a).get("severity").equals("medium")).count()).
                            low(vulnerabilities.stream().filter(a -> ((LinkedHashMap) a).get("severity").equals("medium")).count()).build();
        }

    }


    @Override
    public SnykEnginePolicy policy(ScanObject object) {
        return policy;
    }


}
