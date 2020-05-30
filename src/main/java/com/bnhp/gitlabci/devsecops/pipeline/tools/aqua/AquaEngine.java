package com.bnhp.gitlabci.devsecops.pipeline.tools.aqua;

import com.bnhp.gitlabci.devsecops.pipeline.JsonUtils;
import com.bnhp.gitlabci.devsecops.pipeline.exceptions.EngineParseException;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ReportObject;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Engine;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.EngineResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class AquaEngine extends Engine<AquaEnginePolicy, AquaEngineResults> {

    public static Logger log = LoggerFactory.getLogger(AquaEngine.class);

    private AquaEnginePolicy policy = new AquaEnginePolicy();
    private AquaEngineResults results;

    private AquaEngineResults parse() throws EngineParseException {

        log.info("Aqua is starting.");
        Optional<ReportObject> o = object.getReports().stream().filter(ReportObject::isJson).findFirst();
        if (!o.isPresent()) {
            throw new EngineParseException("Can't find a json to parse. reports didnt include a content type of json");
        } else {
            Map<String, Object> map = JsonUtils.parse(o.get().getContent(), Map.class);
            LinkedHashMap vulnerabilities = ((LinkedHashMap) map.get("vulnerability_summary"));
            LinkedHashMap image_assurance_results = ((LinkedHashMap) map.get("image_assurance_results"));

            return AquaEngineResults.builder()
                    .summary(image_assurance_results.get("disallowed").toString().equals("true")? "According to security policy, Image is disallowed ;( " : "Image is allowed ;)")
                    .technology("docker")
                    .malware(Long.parseLong(vulnerabilities.get("malware").toString()))
                    .total(Long.parseLong(vulnerabilities.get("total").toString()))
                    .high(Long.parseLong(vulnerabilities.get("high").toString()))
                    .malware(Long.parseLong(vulnerabilities.get("high").toString()))
                    .medium(Long.parseLong(vulnerabilities.get("medium").toString()))
                    .low(Long.parseLong(vulnerabilities.get("low").toString())).build();
        }

    }

    @Override
    public void doRun() throws EngineParseException {
        results = parse();
        object.setResults(results);
        object.getResults().setSuccess(policy(object).allowed(results));
    }

    @Override
    public AquaEnginePolicy policy(ScanObject object) {
        return policy;
    }
}
