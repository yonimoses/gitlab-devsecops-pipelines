package com.bnhp.gitlabci.devsecops.pipeline;

import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanSource;
import com.bnhp.gitlabci.devsecops.pipeline.templates.TemplateHandler;
import com.bnhp.gitlabci.devsecops.pipeline.tools.EngineLocator;
import com.bnhp.gitlabci.devsecops.pipeline.tools.aqua.AquaEngine;
import com.bnhp.gitlabci.devsecops.pipeline.tools.aqua.AquaEnginePolicy;
import com.bnhp.gitlabci.devsecops.pipeline.tools.base.Engine;
import com.bnhp.gitlabci.devsecops.pipeline.tools.snyk.SnykEngine;
import com.bnhp.gitlabci.devsecops.pipeline.tools.snyk.SnykEnginePolicy;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.text.StringRandomizer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;

import static java.nio.charset.Charset.forName;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevsecOpsApplicationTests {

	String name = "/home/yoni/Documents/gitlabci-devsecops-pipeline/src/test/java/com/bnhp/gitlabci/devsecops/pipeline/test.html";
	@Autowired
	TemplateHandler handler;
	@Autowired
	ApplicationContext context;

	@Autowired
	EngineLocator locator;
	@Autowired
	private  Configuration configuration;

	static EasyRandom easyRandom ;
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("source","snyk");
		System.setProperty("CI_COMMIT_BRANCH","snyk-v1");
		System.setProperty("BNHP_PIPELINE_NOTIFY","false");
		System.setProperty("CI_PIPELINE_URL","https://CI_PIPELINE_URL.com");
		System.setProperty("CI_JOB_URL","https://CI_JOB_URL.com");
		System.setProperty("CI_COMMIT_SHA", StringRandomizer.aNewStringRandomizer().getRandomValue());


		EasyRandomParameters parameters = new EasyRandomParameters()
				.seed(123L)
				.objectPoolSize(100)
				.randomizationDepth(3)
				.charset(forName("UTF-8"))
				.timeRange(LocalTime.MIN, LocalTime.MAX)
				.dateRange(LocalDate.now().minusDays(2), LocalDate.now().plusDays(2))
				.stringLengthRange(5, 50)
				.collectionSizeRange(1, 10)
				.scanClasspathForConcreteTypes(true)
				.overrideDefaultInitialization(false)
				.ignoreRandomizationErrors(true);
		easyRandom = new EasyRandom(parameters);


	}
	@Test
	public void contextLoads() {
	}


//	@Test
//	public void testRenderTemplate() throws Exception {
//
//		ScanObject scanObject = easyRandom.nextObject(ScanObject.class);
//		ScanResults results = easyRandom.nextObject(ScanResults.class);
//		scanObject.setResults(results);
//		scanObject.setReports(easyRandom.objects(ReportObject.class,4).collect(Collectors.toList()));
//		scanObject.setPolicy(new CheckmarxPolicy());
//		Template t = configuration.getTemplate("scan-report.html");
//
//		System.out.println("t.getName() = " + t.getName());
//
//		String readyParsedTemplate = FreeMarkerTemplateUtils
//				.processTemplateIntoString(t, scanObject);
//
//		// System.out.println("readyParsedTemplate = " + readyParsedTemplate);
////		Map<String, Object> map = JsonUtils.toMap(scanObject);
////		map.putAll(JsonUtils.toMap(results));
////		String template = handler.compile(scanObject);
//		IOUtils.write(readyParsedTemplate,new FileOutputStream(name));
//	}
//	public ScanObject test() throws Exception {
//		ScanSource source = ScanSource.from(EnvParameters.asString("source", "unknown"));
//		Engine engine = (Engine) context.getBean(source.asString());
//		engine.init(source);
//		engine.doRun();
//		return engine.getObject();
//
//	}

	@Test
	public void test_snyk_success() throws Exception {
		System.setProperty("BNHP_PIPELINE_REPORT_JSON_URL","snyk-report.json");
		System.setProperty("source","snyk");
		SnykEngine engine = new SnykEngine();
		engine.init(ScanSource.SNYK);
		((SnykEnginePolicy)engine.getObject().getEnginePolicy()).maxHighAllowed = 100;
		engine.doRun();
		String templateContent = handler.compile(engine.getObject());
		IOUtils.write(templateContent,new FileOutputStream(name));
		Assert.assertTrue(engine.getObject().getResults().getSuccess());
	}

	@Test
	public void test_snyk_fail() throws Exception {
		System.setProperty("BNHP_PIPELINE_REPORT_JSON_URL","snyk-report.json");
		System.setProperty("source","snyk");
		SnykEngine engine = new SnykEngine();
		engine.init(ScanSource.SNYK);
		((SnykEnginePolicy)engine.getObject().getEnginePolicy()).maxHighAllowed = 1;
		engine.doRun();
		String templateContent = handler.compile(engine.getObject());
		IOUtils.write(templateContent,new FileOutputStream(name));
		Assert.assertFalse(engine.getObject().getResults().getSuccess());
	}


	@Test
	public void test_aqua_fail() throws Exception {
		System.setProperty("BNHP_PIPELINE_REPORT_JSON_URL","aqua-report.json");
		System.setProperty("source","aqua");

		AquaEngine engine = new AquaEngine();
		engine.init(ScanSource.AQUA);
		((AquaEnginePolicy)engine.getObject().getEnginePolicy()).maxHighAllowed = 1;
		engine.doRun();
		String templateContent = handler.compile(engine.getObject());
		IOUtils.write(templateContent,new FileOutputStream(name));
		Assert.assertFalse(engine.getObject().getResults().getSuccess());
	}
	@Test
	public void test_aqua_pass() throws Exception {
		System.setProperty("BNHP_PIPELINE_REPORT_JSON_URL","aqua-report.json");
		System.setProperty("source","aqua");

		AquaEngine engine = new AquaEngine();
		engine.init(ScanSource.AQUA);
		((AquaEnginePolicy)engine.getObject().getEnginePolicy()).maxHighAllowed = 100;
		engine.doRun();
		String templateContent = handler.compile(engine.getObject());
		IOUtils.write(templateContent,new FileOutputStream(name));
		Assert.assertTrue(engine.getObject().getResults().getSuccess());
	}

}
