package com.bnhp.gitlabci.devsecops.pipeline.tools.sonarqube;

import com.bnhp.gitlabci.devsecops.pipeline.exceptions.EngineParseException;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.tools.aqua.AquaEnginePolicy;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

public class SonarQubeEngine extends Engine<SonarQubeEnginePolicy, SonarQubeEngineResults> {
    public static Logger log = LoggerFactory.getLogger(SonarQubeEngine.class);

    public SonarQubeEnginePolicy policy = new SonarQubeEnginePolicy();
    public SonarQubeEngineResults results;

    @Override
    public void doRun() throws EngineParseException {
        results = parse();
        object.setResults(results);
        object.getResults().setSuccess(policy(object).allowed(results));
    }


    protected SonarQubeEngineResults parse() throws EngineParseException {
        return null;
    }

    @Override
    public SonarQubeEnginePolicy policy(ScanObject object) {
        return policy;
    }
}
