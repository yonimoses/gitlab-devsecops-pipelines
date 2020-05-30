package com.bnhp.gitlabci.devsecops.pipeline.objects;

public enum ScanSource {

    SNYK("snyk"),
    AQUA("aqua"),
    UNKNOWN("unknown"),
    CHECKMARX("checkmarx"),
    SONARQUBE("sonarqube");

    private String source;

    ScanSource(String source) {
        this.source = source;
    }

    public String asString() {
        return source.toLowerCase();
    }

    public static ScanSource from(String source) {
        switch (source) {
            case "aqua":
                return AQUA;
            case "snyk":
                return SNYK;
            case "checkmarx":
                return CHECKMARX;
            case "sonarqube":
                return SONARQUBE;
            default:
                return UNKNOWN;
        }
    }
}
