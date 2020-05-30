package com.bnhp.gitlabci.devsecops.pipeline.exceptions;

public class GateException extends Exception {
    public GateException(String message) {
        super(message);
    }

    public GateException(String s, Exception e) {

        super(s, e);
    }
}
