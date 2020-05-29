package com.bnhp.gitlabci.devsecops.pipeline.tools.base;

import com.bnhp.gitlabci.devsecops.pipeline.exceptions.EngineParseException;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanResults;
import com.bnhp.gitlabci.devsecops.pipeline.tools.snyk.SnykPolicy;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.Data;

import javax.annotation.PostConstruct;

@Data
public abstract class Engine<T extends Policy> {

    public abstract ScanResults parse(ScanObject object) throws EngineParseException;

    /**
     * I'm
     * @param object
     * @return
     */
    public abstract T policy(ScanObject object);
    public T _defaultPolicy;

//    public abstract String getReport(String url){
//        HttpResponse<String> response = null;
//        try {
//            response = Unirest.get(url).asString();
//            object.content = response.getBody();
//            object.statusCode = response.getStatus();
//            object.contentType = response.getHeaders().getFirst("Content-Type");
//        } catch (Exception e) {
//            log.error("Exception while fetching url " + url,e);
//            if(response !=null){
//                object.content = "";
//                object.statusCode = response.getStatus();
//                object.contentType =  "";
//            }
//        }
//    }




}
