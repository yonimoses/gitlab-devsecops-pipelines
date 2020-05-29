package com.bnhp.gitlabci.devsecops.pipeline.tools;

import com.bnhp.gitlabci.devsecops.pipeline.InfoUtils;
import com.bnhp.gitlabci.devsecops.pipeline.exceptions.EngineParseException;
import com.bnhp.gitlabci.devsecops.pipeline.exceptions.TemplateRenderException;
import com.bnhp.gitlabci.devsecops.pipeline.integrations.mail.MailIntegrationService;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanResults;
import com.bnhp.gitlabci.devsecops.pipeline.templates.TemplateCustomization;
import com.bnhp.gitlabci.devsecops.pipeline.templates.TemplateHandler;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

@Component
public class EngineLocator {

    public static Logger log = LoggerFactory.getLogger(EngineLocator.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private MailIntegrationService mailIntegrationService;
    private final Map<String, Engine> commands;

    @Autowired
    public EngineLocator(Map<String, Engine> commands) {
//        this.loader = loader;
//        this.mustacheCompiler = mustacheCompiler;
        this.commands = commands;
    }

    @PostConstruct
    public void postConstruct() throws Exception {

        log.debug("Template loaded");

    }

    public ScanObject doRun() throws EngineParseException, TemplateRenderException {

        ScanObject object = ScanObject.newScanObject();
        InfoUtils.prettyFormat(object);
        log.info("Running report on command {}", object.getSource());

        Engine engine = commands.get(object.getSource().asString());

        ScanResults results =  engine.parse(object);
        object.setPolicy(engine.policy(object));
        object.setResults(results);
        object.getResults().success = object.getPolicy().invoke(results);

        log.info("Policy for {} is {}", object.getSource(), results.success ? " allowed" : "disallowed");


        /**
         * Dont like it, but can't help it for now.
         *
         */
        TemplateCustomization customization = new TemplateCustomization(object.getResults().success);
        object.setTemplateCustomization(customization);

        /**
         * Let's render the template
         */
        InfoUtils.prettyFormat(object);

        TemplateHandler handler = (TemplateHandler) context.getBean("templateHandler");
        String templateContent = handler.compile(object);

        /**
         * Let's send the email
         */
        mailIntegrationService.send(object,templateContent);
        return object;
    }

    public Set<String> getAvailableCommands() {
        return commands.keySet();
    }
}
