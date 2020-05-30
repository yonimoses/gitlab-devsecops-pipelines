package com.bnhp.gitlabci.devsecops.pipeline;

import com.bnhp.gitlabci.devsecops.pipeline.exceptions.InvalidSourceException;
import com.bnhp.gitlabci.devsecops.pipeline.integrations.NotificationService;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanSource;
import com.bnhp.gitlabci.devsecops.pipeline.tools.EngineLocator;
import com.bnhp.gitlabci.devsecops.pipeline.tools.aqua.AquaEngine;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Engine;
import com.bnhp.gitlabci.devsecops.pipeline.tools.checkmarx.CheckmarxEngine;
import com.bnhp.gitlabci.devsecops.pipeline.tools.snyk.SnykEngine;
import com.bnhp.gitlabci.devsecops.pipeline.tools.sonarqube.SonarQubeEngine;
import com.samskivert.mustache.Mustache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mustache.MustacheEnvironmentCollector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class DevsecOpsApplication implements ApplicationRunner {

    public static Logger log = LoggerFactory.getLogger(EngineLocator.class);
    public final EngineLocator engine;
    @Autowired
    public ApplicationContext context;
    @Autowired
    public NotificationService notificationService;


    public DevsecOpsApplication(EngineLocator engine) {
        this.engine = engine;

    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DevsecOpsApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Bean
    public Mustache.Compiler mustacheCompiler(
            Mustache.TemplateLoader templateLoader,
            Environment environment) {

        MustacheEnvironmentCollector collector
                = new MustacheEnvironmentCollector();
        collector.setEnvironment(environment);

        return Mustache.compiler()
                //.defaultValue("Some Default Value")
                .withLoader(templateLoader)
                .withCollector(collector);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        EnvParameters.arguments = args;
        //debug a little, most of the variables will be available from the env.
        InfoUtils.writeMapInfo("getenv", System.getenv());

        ScanSource source = ScanSource.from(EnvParameters.asString("source", "unknown"));

        if (source.equals(ScanSource.UNKNOWN)) {
            throw new InvalidSourceException("Sources are aqua/snyk/sonar/checkmarx");
        }
        Engine engine = null;

        switch (source) {
            case AQUA:
                engine = new AquaEngine();
                break;
            case SNYK:
                engine = new SnykEngine();
                break;
            case SONARQUBE:
                engine = new SonarQubeEngine();
                break;
            case CHECKMARX:
                engine = new CheckmarxEngine();
                break;
        }
        assert engine != null;
        engine.init(source);
        engine.doRun();

        notificationService.doNotify(engine.getObject());
//        engine.doNotify();
        InfoUtils.prettyFormat(engine.getObject());
        PipelineFinalizer.exit(engine.getObject());

    }
}
