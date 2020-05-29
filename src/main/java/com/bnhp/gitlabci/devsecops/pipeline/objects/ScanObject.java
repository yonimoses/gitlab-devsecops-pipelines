package com.bnhp.gitlabci.devsecops.pipeline.objects;

import com.bnhp.gitlabci.devsecops.pipeline.EnvParameters;
import com.bnhp.gitlabci.devsecops.pipeline.templates.TemplateCustomization;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Policy;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.bnhp.gitlabci.devsecops.pipeline.Utils.getFilename;

@Data
public class ScanObject {


    public static Logger log = LoggerFactory.getLogger(ScanObject.class);

    public ScanSource source;
    public String[] recipients;
    public String projectUrl;
    public String buildUrl;
    public String date;
    public String commit;
    public String branch;
    public List<ReportObject> reports = new ArrayList<>();
    public ScanResults results;
    public Policy policy;
    public TemplateCustomization templateCustomization;

    public ScanObject() {


    }
    public static ScanObject newScanObject(){
        ScanObject object = new ScanObject();
        object.source = ScanSource.from(EnvParameters.asString("source","snyk"));
        object.branch = EnvParameters.asString("branch","master");
        String re[] = StringUtils.split(EnvParameters.asString("recipients","yoni.moses@gmail.com"),",");
        object.recipients = (re !=null ? re : new String[]{EnvParameters.asString("recipients","yoni.moses@gmail.com")});
        object.projectUrl = EnvParameters.asString("projectUrl","https://www.project.com");
        object.buildUrl = EnvParameters.asString("buildUrl","https://www.buildUrl.com");
        object.date = EnvParameters.asString("date", new Date().toString());
        object.commit = EnvParameters.asString("commit", UUID.randomUUID().toString());
        object.branch = System.getenv().get("branch");
        String html = EnvParameters.asString("reportHtmlUrl","https://raw.githubusercontent.com/aquasecurity/microscanner/master/README.md");
        String json = EnvParameters.asString("reportJsonUrl","https://raw.githubusercontent.com/snyk/snyk-to-html/master/sample-data/test-report.json");
        if(html !=null){
            object.reports.add(get(html));
        }
//
        if(json !=null){
            object.reports.add(get(json));
        }
        return object;
    }


    private static ReportObject get(String url){
        log.info("Fetching {} ",url);
        ReportObject object = new ReportObject();
        object.url = url;
        object.filename = getFilename(url);
        HttpResponse<String>  response = null;
        try {
            response = Unirest.get(url).asString();
            object.content = response.getBody();
            object.statusCode = response.getStatus();
            object.contentType = response.getHeaders().getFirst("Content-Type");
        } catch (Exception e) {
            log.error("Exception while fetching url " + url,e);
            if(response !=null){
                object.content = "";
                object.statusCode = response.getStatus();
                object.contentType =  "";
            }
        }

        return object;
    }



}
