package com.bnhp.gitlabci.devsecops.pipeline.exceptions;

import java.io.IOException;

public class TemplateRenderException extends GateException{
    public TemplateRenderException(String message) {
        super(message);
    }

    public TemplateRenderException(String s, Exception e) {
        super(s, e);
    }
}
