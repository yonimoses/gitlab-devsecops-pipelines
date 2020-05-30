package com.bnhp.gitlabci.devsecops.pipeline;

import org.springframework.boot.ApplicationArguments;

public class EnvParameters {

    static ApplicationArguments arguments;

    public static String asString(String key, String def) {
        return get(key, def);
    }

    public static Boolean asBoolean(String key, boolean def) {
        return Boolean.valueOf(get(key, def + ""));
    }

    private static String get(String key, String def) {
        // or from env
        String value = System.getenv(key);
        if (value == null) {
            // or from arguments
            if (arguments.containsOption(key)) {
                value = arguments.getOptionValues(key).get(0);
            }
        }
        return value == null ? System.getProperty(key, def) : value;
    }
}
