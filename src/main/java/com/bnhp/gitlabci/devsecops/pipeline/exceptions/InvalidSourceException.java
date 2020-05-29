package com.bnhp.gitlabci.devsecops.pipeline.exceptions;

public class InvalidSourceException extends GateException {
    public InvalidSourceException(String message) {
        super(message);
    }

}
