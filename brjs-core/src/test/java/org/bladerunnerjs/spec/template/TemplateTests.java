package org.bladerunnerjs.spec.template;

import java.util.Arrays;

import org.bladerunnerjs.model.App;
import org.bladerunnerjs.model.Aspect;
import org.bladerunnerjs.model.Blade;
import org.bladerunnerjs.model.Bladeset;
import org.bladerunnerjs.model.DirNode;
import org.bladerunnerjs.model.JsLib;
import org.bladerunnerjs.model.Workbench;
import org.bladerunnerjs.testing.specutility.engine.SpecTest;
import org.junit.Before;
import org.junit.Test;


public class TemplateTests extends SpecTest
{
	
	App app;
	Aspect aspect;
	Bladeset bladeset;
	Blade blade;
	Workbench workbench;
	private JsLib lib;
	
	@Before
	public void initTestObjects() throws Exception {		
		given(brjs).automaticallyFindsAllPlugins()
			.and(brjs).hasBeenCreated()
			.and(brjs).usesProductionTemplates();
		
		DirNode appJars = brjs.appJars();
		given(appJars).containsFile("some-lib.jar");
		
		app = brjs.app("app");
		aspect = app.aspect("default");
		bladeset = app.bladeset("bs");
		blade = bladeset.blade("b1");
		workbench = blade.workbench();
		lib = app.jsLib("lib");
	}
	
	@Test
	public void brjsHasCorrectTemplate() throws Exception {
		when(brjs).populate();
		then(brjs).hasFilesAndDirs(
				Arrays.asList("conf/bundleConfig.xml", "conf/test-runner.conf", "conf/users.properties"),
				Arrays.asList("apps", "conf", "sdk")
		);
	}

	@Test
	public void appHasCorrectTemplate() throws Exception {
		when(brjs).runCommand("create-app", "app");
		then(app).hasFilesAndDirs(
				Arrays.asList("app.conf"),
				Arrays.asList("WEB-INF", "default-aspect", "libs")
		);
	}
	
	@Test
	public void aspectHasCorrectTemplate() throws Exception {
		when(brjs).runCommand("create-app", "app", "appns");
		then(aspect).hasFilesAndDirs(
				Arrays.asList("index.html", "resources/aliases.xml", "src/appns/App.js", "themes/common/style.css"),
				Arrays.asList("resources", "src", "unbundled-resources", "themes")
		);
	}
	
	@Test
	public void bladesetHasCorrectTemplate() throws Exception {
		given(brjs).commandHasBeenRun("create-app", "app", "appns");
		when(brjs).runCommand("create-bladeset", "app", "bs");
		then(bladeset).hasFilesAndDirs(
				Arrays.asList("src/appns/bs/BsClass.js", "themes/common/style.css"),
				Arrays.asList("resources", "resources/html", "src", "tests", "themes")
		);
	}
	
	@Test
	public void bladesetTestsHasCorrectTemplate() throws Exception {
		given(brjs).commandHasBeenRun("create-app", "app", "appns");
		when(brjs).runCommand("create-bladeset", "app", "bs");
		then(bladeset.testType("unit")).hasFilesAndDirs(
				Arrays.asList("js-test-driver/jsTestDriver.conf", "js-test-driver/resources/aliases.xml", "js-test-driver/tests/ExampleClassTest.js"),
				Arrays.asList("js-test-driver", "js-test-driver/tests", "js-test-driver/resources")
		);
	}
	
	@Test	
	public void bladeHasCorrectTemplate() throws Exception {
		given(brjs).commandHasBeenRun("create-app", "app", "appns")
			.and(brjs).commandHasBeenRun("create-bladeset", "app", "bs");
		when(brjs).runCommand("create-blade", "app", "bs", "b1");
		then(blade).hasFilesAndDirs(
				Arrays.asList("src/appns/bs/b1/B1ViewModel.js", "themes/common/style.css"),
				Arrays.asList("resources", "resources/html", "src", "tests", "workbench", "themes")
		);
	}
	
	@Test
	public void bladeTestsHasCorrectTemplate() throws Exception {
		given(brjs).commandHasBeenRun("create-app", "app", "appns")
			.and(brjs).commandHasBeenRun("create-bladeset", "app", "bs");
		when(brjs).runCommand("create-blade", "app", "bs", "b1");
		then(blade.testType("unit")).hasFilesAndDirs(
				Arrays.asList("js-test-driver/jsTestDriver.conf", "js-test-driver/resources/aliases.xml", "js-test-driver/tests/B1ViewModelTest.js"),
				Arrays.asList("js-test-driver", "js-test-driver/tests", "js-test-driver/resources")
		);
	}
	
	@Test
	public void workbenchHasCorrectTemplate() throws Exception {
		given(brjs).commandHasBeenRun("create-app", "app", "appns")
			.and(brjs).commandHasBeenRun("create-bladeset", "app", "bs");
		when(brjs).runCommand("create-blade", "app", "bs", "b1");
		then(workbench).hasFilesAndDirs(
				Arrays.asList("index.html", "resources/aliases.xml", "resources/style/workbench.css"),
				Arrays.asList("resources", "resources/html", "resources/style/", "src")
		);
	}
	
	@Test
	public void brLibHasCorrectTemplate() throws Exception {
		given(brjs).commandHasBeenRun("create-app", "app", "appns");
		when(brjs).runCommand("create-library", "app", "lib");
		then(lib).hasFilesAndDirs(
				Arrays.asList("br-lib.conf", "src/lib/Lib.js", "tests/test-unit/js-test-driver/jsTestDriver.conf", "tests/test-unit/js-test-driver/resources/aliases.xml", 
							"tests/test-unit/js-test-driver/tests/LibTest.js"),
				Arrays.asList("src", "tests", "tests/test-unit/js-test-driver/")
		).and(lib).fileContentsContains("src/lib/Lib.js", "Lib = ");
	}
	
	@Test
	public void thirdpartyLibHasCorrectTemplate() throws Exception {
		given(brjs).commandHasBeenRun("create-app", "app", "appns");
		when(brjs).runCommand("create-library", "app", "lib", "-t", "thirdparty");
		then(lib).hasFilesAndDirs(
				Arrays.asList("thirdparty-lib.manifest"),
				Arrays.asList()
		);
	}
	
}