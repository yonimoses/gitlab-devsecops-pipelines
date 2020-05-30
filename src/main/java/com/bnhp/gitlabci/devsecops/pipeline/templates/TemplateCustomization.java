package com.bnhp.gitlabci.devsecops.pipeline.templates;

import lombok.Data;

@Data
public class TemplateCustomization {

    public static final String SUCCESS_COLOR = "#22bc66";
    public static final String ERROR_COLOR = "#ff6136";
    public String color;

    public TemplateCustomization(boolean success) {
        this.color = success ? SUCCESS_COLOR : ERROR_COLOR;
    }


}
