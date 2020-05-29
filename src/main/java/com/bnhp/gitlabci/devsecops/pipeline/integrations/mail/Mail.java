package com.bnhp.gitlabci.devsecops.pipeline.integrations.mail;

import lombok.Data;

import java.util.List;

@Data
public class Mail {

    private String from;

    private List<String> to;

    private List<String> cc;

    private List<String> attachments;


    private String subject;

    private String content;

    private String contentType;
}
 