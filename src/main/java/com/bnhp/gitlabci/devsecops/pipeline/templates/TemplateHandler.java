package com.bnhp.gitlabci.devsecops.pipeline.templates;

import com.bnhp.gitlabci.devsecops.pipeline.exceptions.TemplateRenderException;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class TemplateHandler {
    public static Logger log = LoggerFactory.getLogger(TemplateHandler.class);
    private final Configuration configuration;
    private freemarker.template.Template template;

    @Autowired
    public TemplateHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @PostConstruct
    public void postConstruct() throws Exception {
        template = configuration.getTemplate("scan-report.html");
    }

    public String compile(ScanObject object) throws TemplateRenderException {

        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, object);
        } catch (Exception e) {
           throw new TemplateRenderException("Failed to compile template " + e.getMessage(),e);
        }
    }

}
