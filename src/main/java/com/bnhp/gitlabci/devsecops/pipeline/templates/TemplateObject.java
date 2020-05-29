package com.bnhp.gitlabci.devsecops.pipeline.templates;

import lombok.Data;

@Data
public class TemplateObject {

    private String docker_id;
    private String product_url;
    private String product_name;
    private String commit_ud;
    private String build_url;
    private String branch;
    private String job_id;
    private String project_url;
}
