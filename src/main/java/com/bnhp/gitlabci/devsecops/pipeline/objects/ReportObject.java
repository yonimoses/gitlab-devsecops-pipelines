package com.bnhp.gitlabci.devsecops.pipeline.objects;

import lombok.Data;

@Data
public class ReportObject {

    public String url;

    @Override
    public String toString() {
        return "ReportObject{" +
                "url='" + url + '\'' +
                ", statusCode=" + statusCode +
                ", contentType='" + contentType + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }

    public String content;
    public Integer statusCode;
    public String contentType;
    public String filename;

    public boolean isJson() {
        return contentType.contains("json") || filename.endsWith("json") || url.endsWith("json");
    }

}
  