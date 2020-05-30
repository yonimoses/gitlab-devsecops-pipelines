package com.bnhp.gitlabci.devsecops.pipeline.tools.base;

import com.bnhp.gitlabci.devsecops.pipeline.EnvParameters;
import com.bnhp.gitlabci.devsecops.pipeline.exceptions.EngineParseException;
import com.bnhp.gitlabci.devsecops.pipeline.exceptions.TemplateRenderException;
import com.bnhp.gitlabci.devsecops.pipeline.integrations.NotificationService;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanSource;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
public abstract class Engine<T extends EnginePolicy<R>, R extends EngineResults> {

    public static Logger log = LoggerFactory.getLogger(Engine.class);
    public ScanObject object;
    public abstract void doRun() throws EngineParseException ;


//    protected abstract R parse() throws EngineParseException;

    public void init(ScanSource source) {
        object = new ScanObject(source);
        object.notify = EnvParameters.asBoolean("BNHP_PIPELINE_NOTIFY", true);
        object.failOnScanError = EnvParameters.asBoolean("BNHP_FAIL_PIPELINE_WHEN_SCAN_FAILED", true);
        object.branch = EnvParameters.asString("CI_COMMIT_BRANCH", EnvParameters.asString("CI_COMMIT_REF_NAME", "unknown"));
        object.recipients = EngineHelper.parseRecipients();
        object.projectUrl = EnvParameters.asString("CI_PIPELINE_URL", "CI_PIPELINE_URL");
        object.buildUrl = EnvParameters.asString("CI_JOB_URL", "CI_JOB_URL");
        object.date = EnvParameters.asString("date", new Date().toString());
        object.date = EnvParameters.asString("date", new Date().toString());
        object.commit = EnvParameters.asString("CI_COMMIT_SHA", "CI_COMMIT_SHA ");
        String html = EnvParameters.asString("BNHP_PIPELINE_REPORT_HTML_URL", "https://raw.githubusercontent.com/aquasecurity/microscanner/master/README.md");
        String json = EnvParameters.asString("BNHP_PIPELINE_REPORT_JSON_URL", "https://raw.githubusercontent.com/snyk/snyk-to-html/master/sample-data/test-report.json");
        if (html != null) {
            object.reports.add(EngineHelper.get(html));
        }
//
        if (json != null) {
            object.reports.add(EngineHelper.get(json));
        }
        object.setEnginePolicy(policy(object));

    }

    /**
     * incase you wanna override some parameters in the policy
     * @param object
     * @return
     */
    public abstract T policy(ScanObject object);
}
