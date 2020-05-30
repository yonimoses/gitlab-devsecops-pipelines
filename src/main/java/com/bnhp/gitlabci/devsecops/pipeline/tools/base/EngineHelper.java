package com.bnhp.gitlabci.devsecops.pipeline.tools.base;

import com.bnhp.gitlabci.devsecops.pipeline.EnvParameters;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ReportObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import static com.bnhp.gitlabci.devsecops.pipeline.Utils.getFilename;

public class EngineHelper {
    public static Logger log = LoggerFactory.getLogger(EngineHelper.class);

    static ReportObject get(String url) {
        return url.startsWith("http") ? getRemote(url) : getLocal(url);
    }
    static String[] parseRecipients() {
        String re[] = StringUtils.split(EnvParameters.asString("recipients", "yoni.moses@gmail.com"), ",");
        return (re != null ? re : new String[]{EnvParameters.asString("recipients", "yoni.moses@gmail.com")});
    }
    private static ReportObject getRemote(String url) {
        log.info("Fetching {} ", url);
        ReportObject object = new ReportObject();
        object.url = url;
        object.filename = getFilename(url);
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(url).asString();
            object.content = response.getBody();
            object.statusCode = response.getStatus();
            object.contentType = response.getHeaders().getFirst("Content-Type");
        } catch (Exception e) {
            log.error("Exception while fetching url " + url, e);
            if (response != null) {
                object.content = "";
                object.statusCode = response.getStatus();
                object.contentType = "";
            }
        }

        return object;
    }

    private static ReportObject getLocal(String url) {
        log.info("Fetching {} ", url);
        ReportObject object = new ReportObject();
        object.url = url;
        object.filename = url;
        try {
            object.content = IOUtils.toString(new ClassPathResource(url).getInputStream(), "UTF-8");
            object.statusCode = 200;
            object.contentType = url.endsWith("json") ? "application/json" : "text/plain";
        } catch (Exception e) {
            log.error("Exception while fetching url " + url, e);
        }

        return object;
    }
}
