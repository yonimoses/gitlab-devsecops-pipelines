package com.bnhp.gitlabci.devsecops.pipeline.tools;

import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class EngineLocator{

    public static Logger log = LoggerFactory.getLogger(EngineLocator.class);

    @Autowired
    private ApplicationContext context;

    private final Map<String, Engine> commands;

    @Autowired
    public EngineLocator(Map<String, Engine> commands) {
        this.commands = commands;
    }


//    public ScanObject doRun(ScanObject object) throws EngineParseException, TemplateRenderException {
//
//        log.info("Running report on command {}", object.getSource());
//
//        Engine engine = get(object.getSource());
//
//        ScanResults results = engine.parse(object);
//        object.setPolicy(engine.policy(object));
//        object.setResults(results);
//        object.getResults().setSuccess(object.getPolicy().allowed(results));
//
//        log.info("Policy for {} is {}", object.getSource(), results.success ? " allowed" : "disallowed");
//
//
//        return object;
//    }
//
//    public Set<String> getAvailableCommands() {
//        return commands.keySet();
//    }
//
//    public Engine get(ScanSource source) {
//        switch (source) {
//            case AQUA:
//                return (AquaEngine) context.getBean("aqua");
//            case SNYK:
//                return (SnykEngine) context.getBean("snyk");
//            case CHECKMARX:
//                return (CheckmarxEngine) context.getBean("checkmarx");
//            case SONARQUBE:
//                return (SonarQubeEngine) context.getBean("sonar");
//        }
//    }
}
