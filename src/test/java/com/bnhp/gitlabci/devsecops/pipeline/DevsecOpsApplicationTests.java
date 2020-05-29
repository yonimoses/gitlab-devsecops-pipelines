package com.bnhp.gitlabci.devsecops.pipeline;

import com.bnhp.gitlabci.devsecops.pipeline.objects.ReportObject;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanObject;
import com.bnhp.gitlabci.devsecops.pipeline.objects.ScanResults;
import com.bnhp.gitlabci.devsecops.pipeline.templates.TemplateHandler;
import com.bnhp.gitlabci.devsecops.pipeline.tools.EngineLocator;
import com.bnhp.gitlabci.devsecops.pipeline.tools.checkmarx.CheckmarxPolicy;
import com.samskivert.mustache.Mustache;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mustache.MustacheResourceTemplateLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.forName;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevsecOpsApplicationTests {

	String name = "/home/yoni/Documents/gitlabci-devsecops-pipeline/src/test/java/com/bnhp/gitlabci/devsecops/pipeline/test.html";
	@Autowired
	TemplateHandler handler;

	@Autowired
	EngineLocator locator;
	@Autowired
	private  Configuration configuration;

	static EasyRandom easyRandom ;
	@BeforeClass
	public static void beforeClass() {
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


	@Test
	public void testRenderTemplate() throws Exception {

		ScanObject scanObject = easyRandom.nextObject(ScanObject.class);
		ScanResults results = easyRandom.nextObject(ScanResults.class);
		scanObject.setResults(results);
		scanObject.setReports(easyRandom.objects(ReportObject.class,4).collect(Collectors.toList()));
		scanObject.setPolicy(new CheckmarxPolicy());
		Template t = configuration.getTemplate("scan-report.html");

		System.out.println("t.getName() = " + t.getName());

		String readyParsedTemplate = FreeMarkerTemplateUtils
				.processTemplateIntoString(t, scanObject);

		// System.out.println("readyParsedTemplate = " + readyParsedTemplate);
//		Map<String, Object> map = JsonUtils.toMap(scanObject);
//		map.putAll(JsonUtils.toMap(results));
//		String template = handler.compile(scanObject);
		IOUtils.write(readyParsedTemplate,new FileOutputStream(name));
	}

	@Test
	public void snykSuccess() throws Exception {

		System.setProperty("source","snyk");
		locator.doRun();
//		IOUtils.write(tmpl.execute(data),new FileOutputStream(name));
		//	System.out.println("mustacheTemplateLoader = " + loader.getTemplate("scan-report"));
	}

}
