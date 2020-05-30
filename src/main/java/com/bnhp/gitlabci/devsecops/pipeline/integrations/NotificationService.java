package com.bnhp.gitlabci.devsecops.pipeline.integrations;

import com.bnhp.gitlabci.devsecops.pipeline.InfoUtils;
import com.bnhp.gitlabci.devsecops.pipeline.exceptions.TemplateRenderException;
import com.bnhp.gitlabci.devsecops.pipeline.integrations.mail.MailIntegrationService;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.templates.TemplateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public static Logger log = LoggerFactory.getLogger(NotificationService.class);

    final MailIntegrationService service;
    final TemplateHandler handler;

    public NotificationService(MailIntegrationService service, TemplateHandler handler) {
        this.service = service;
        this.handler = handler;
    }

    public void doNotify(ScanObject object) throws TemplateRenderException {
        if (object.getNotify()) {
            InfoUtils.prettyFormat(object);
            String templateContent = handler.compile(object);
            service.send(object, templateContent);
        } else {
            log.warn("BNHP_PIPELINE_NOTIFY was set to false, emails will not be sent");
        }
    }
}
