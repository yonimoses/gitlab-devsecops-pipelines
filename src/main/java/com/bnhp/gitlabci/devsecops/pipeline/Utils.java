package com.bnhp.gitlabci.devsecops.pipeline;

import org.apache.commons.io.FilenameUtils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Utils {

    public static Map<String, Object> beanProperties(Object bean) {
        try {
            Map<String, Object> map = new HashMap<>();
            Arrays.stream(Introspector.getBeanInfo(bean.getClass(), Object.class)
                    .getPropertyDescriptors())
                    // filter out properties with setters only
                    .filter(pd -> Objects.nonNull(pd.getReadMethod()))
                    .forEach(pd -> { // invoke method to get value
                        try {
                            Object value = pd.getReadMethod().invoke(bean);
                            if (value != null) {
                                map.put(pd.getName(), value);
                            }
                        } catch (Exception e) {
                            // add proper error handling here
                        }
                    });
            return map;
        } catch (IntrospectionException e) {
            // and here, too
            return Collections.emptyMap();
        }
    }

    public static String getFilename(String url) {
        try {
            URL _url = new URL(url);
            return FilenameUtils.getName(_url.getPath());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "cannot parse " + url;
        }
//        System.out.println(FilenameUtils.getBaseName(_url.getPath())); // -> file
//        System.out.println(FilenameUtils.getExtension(_url.getPath())); // -> xml
//        System.out.println(FilenameUtils.getName(_url.getPath())); // -> file.xml
    }
}
