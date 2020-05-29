package com.bnhp.gitlabci.devsecops.pipeline;

import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InfoUtils {
    public static Logger log = LoggerFactory.getLogger(InfoUtils.class);

    public static final String LIST_FORMAT = "   %1$-70s| %2$s%n";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");

    public final static List<String> discardRequestInformation = new ArrayList<>();




    public static void writeMapInfo(String name, Map parameters) {

        if (!log.isDebugEnabled()) {
            return;
        }
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%n%n MAP DUMP%n"));
        sb.append(String.format(" =======================%n"));

        sb.append(String.format(LIST_FORMAT, "Name", name));

        if (!parameters.isEmpty()) {
            for (Object key : parameters.keySet()) {
                if(key.equals(""))
                sb.append(String.format(LIST_FORMAT, key, parameters.get(key)));
            }
            sb.append("");
        } else {
            sb.append(String.format("%n ")).
                    append("Map has no data").append(String.format("%n"));

        }
        String wholeDump = sb.toString();
        log.debug(wholeDump);
    }

    public static void writeListInfo(String name, List<String> parameters) {

        if (!log.isDebugEnabled()) {
            return;
        }
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%n%n LIST DUMP%n"));
        sb.append(String.format(" =======================%n"));
        sb.append(String.format(LIST_FORMAT, "Name", name));


        if (!parameters.isEmpty()) {
            parameters.forEach(img -> {
                sb.append(String.format(LIST_FORMAT, img));
            });
        } else {
            sb.append(String.format("%n ")).
                    append("Map has no data").append(String.format("%n"));

        }
        String wholeDump = sb.toString();
        log.debug(wholeDump);
    }


    public static void writeArgs(ApplicationArguments args) {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%n%n ARGS %n"));
        sb.append(String.format(" =======================%n"));
        sb.append(String.format(LIST_FORMAT, "Option Names", ""));

        args.getOptionNames().forEach(a -> {
            sb.append(String.format(LIST_FORMAT, a,args.getOptionValues(a)));
        });

        sb.append(String.format(LIST_FORMAT, "Non Options Args", ""));
        args.getNonOptionArgs().forEach(a -> {
            sb.append(String.format(LIST_FORMAT, a,""));
        });
        String wholeDump = sb.toString();
        log.debug(wholeDump);
    }

    public static void prettyFormat(ScanObject object) {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%n%n ScanObject %n"));
        sb.append(String.format(" =======================%n"));
        sb.append(String.format(LIST_FORMAT, "Option Names", ""));
        Utils.beanProperties(object).forEach((k, v) -> sb.append(String.format(LIST_FORMAT, k,v)));
        String wholeDump = sb.toString();
        log.debug(wholeDump);
    }

    public static void writePolicy(Policy policy) {

        StringBuilder sb = new StringBuilder();

        sb.append(String.format("%n%n EnginePolicy %n"));
        sb.append(String.format(" =======================%n"));
        sb.append(String.format(LIST_FORMAT, "Policy", ""));
        Utils.beanProperties(policy).forEach((k, v) -> sb.append(String.format(LIST_FORMAT, k,v)));
        String wholeDump = sb.toString();
        log.debug(wholeDump);
    }
}