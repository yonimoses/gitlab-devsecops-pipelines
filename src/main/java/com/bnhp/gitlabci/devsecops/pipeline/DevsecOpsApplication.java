package com.bnhp.gitlabci.devsecops.pipeline;

import com.bnhp.gitlabci.devsecops.pipeline.tools.EngineLocator;
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
public class DevsecOpsApplication implements ApplicationRunner{

	public static Logger log = LoggerFactory.getLogger(EngineLocator.class);
	public final EngineLocator locator;
	@Autowired
	public ApplicationContext context;


	public DevsecOpsApplication(EngineLocator locator) {
		this.locator = locator;

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
		//debug a little
		InfoUtils.writeMapInfo("getenv", System.getenv());
		InfoUtils.writeArgs(args);

		//debug a little
//		ScanObject object = ScanObject.newScanObject();
//		InfoUtils.prettyFormat(object);
		locator.doRun();

		//Template tmpl = mustacheCompiler.compile(IOUtils.toString(loader.getTemplate("scan-report")));


//		System.out.println("# NonOptionArgs: " + args.getNonOptionArgs().size());
//
//		System.out.println("NonOptionArgs:");
//		args.getNonOptionArgs().forEach(System.out::println);
//
//		System.out.println("# OptionArgs: " + args.getOptionNames().size());
//		System.out.println("OptionArgs:");
//
//		args.getOptionNames().forEach(optionName -> {
//			System.out.println(optionName + "=" + args.getOptionValues(optionName));
//		});
	}
}
