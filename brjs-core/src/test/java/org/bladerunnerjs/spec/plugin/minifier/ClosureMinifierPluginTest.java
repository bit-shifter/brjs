package org.bladerunnerjs.spec.plugin.minifier;

import org.bladerunnerjs.model.App;
import org.bladerunnerjs.model.Aspect;
import org.bladerunnerjs.model.Blade;
import org.bladerunnerjs.testing.specutility.engine.SpecTest;
import org.junit.Before;
import org.junit.Test;


public class ClosureMinifierPluginTest extends SpecTest
{
	private App app;
	private Aspect aspect;
	private Blade blade;
	
	private StringBuffer response = new StringBuffer();
	private String unminifiedContent;
	private String minifyWhitespaceContent;
	private String minifySimpleContent;
	private String minifyAdvancedContent;
	
	@Before
	public void initTestObjects() throws Exception
	{
		given(brjs).automaticallyFindsBundlerPlugins()
			.and(brjs).automaticallyFindsMinifierPlugins()
			.and(brjs).hasBeenCreated();
			app = brjs.app("app1");
			aspect = app.aspect("default");
			blade = app.bladeset("bs").blade("b1");
			
		/* only closure compiler service used to calculate responses - http://closure-compiler.appspot.com/home */
		unminifiedContent = "function hello(name) {\n"+
				"  alert('Hello, ' + name);\n"+
				"}\n"+
				"hello('New user');\n"+
				"\n";
		minifyWhitespaceContent = "function hello(name){alert(\"Hello, \"+name)}hello(\"New user\")";
		minifySimpleContent		= "function(a,b,c){alert(\"Hello, New user\")";
		minifyAdvancedContent	= "alert(\"Hello, New user\")";
	}
	
	@Test
	public void closureMinifierRunsForRequestsWithClosureWhitespaceOption() throws Exception
	{
		given(aspect).hasClass("appns/Class1")
			.and(aspect).indexPageRefersTo("appns.Class1")
			.and(aspect).classFileHasContent("appns.Class1", unminifiedContent);
		when(aspect).requestReceivedInDev("js/prod/closure-whitespace/bundle.js", response);
		then(response).containsText(minifyWhitespaceContent);
	}
	
	@Test
	public void closureMinifierRunsForRequestsWithClosureSimpleOption() throws Exception
	{
		given(aspect).hasClass("appns/Class1")
			.and(aspect).indexPageRefersTo("appns.Class1")
			.and(aspect).classFileHasContent("appns.Class1", unminifiedContent);
		when(aspect).requestReceivedInDev("js/prod/closure-simple/bundle.js", response);
		then(response).containsText(minifySimpleContent);
	}
	
	@Test
	public void closureMinifierRunsForRequestsWithClosureAdvancedOption() throws Exception
	{
		given(aspect).hasClass("appns/Class1")
			.and(aspect).indexPageRefersTo("appns.Class1")
			.and(aspect).classFileHasContent("appns.Class1", unminifiedContent);
		when(aspect).requestReceivedInDev("js/prod/closure-advanced/bundle.js", response);
		then(response).containsText(minifyAdvancedContent);
	}
	
	@Test
	public void closureMinifierHandlesRequestsWithMultipleFiles() throws Exception
	{
		given(blade).hasNamespacedJsPackageStyle("src/appns/bs/b1")
			.and(blade).hasClasses("appns.bs.b1.Class1", "appns.bs.b1.Class2")
			.and(aspect).indexPageRefersTo("appns.bs.b1.Class1")
			.and(blade).classDependsOn("appns.bs.b1.Class1", "appns.bs.b1.Class2");
		when(aspect).requestReceivedInDev("js/prod/closure-whitespace/bundle.js", response);
		then(response).containsMinifiedClasses("appns.bs.b1.Class1", "appns.bs.b1.Class2");
	}
	
	@Test
	public void closureMinifierHandlesAMixOfSourceFileTypes() throws Exception
	{
		given(aspect).hasNamespacedJsPackageStyle("src/appns/cjs")
			.and(aspect).hasCommonJsPackageStyle("appns/cjs")
			.and(aspect).hasClasses("appns.cjs.Class", "appns.cjs.CommonJsClass")
			.and(aspect).indexPageRefersTo("appns.cjs.Class")
			.and(aspect).classDependsOn("appns.cjs.Class",  "appns.cjs.CommonJsClass");
		when(aspect).requestReceivedInDev("js/prod/closure-whitespace/bundle.js", response);
		then(response).containsMinifiedClasses("appns.cjs.Class", "CommonJsClass"); //TODO: have better CommonJs minified class handling
	}
	
	@Test
	public void closureMinifierStillAddsPackageDefinitionsBlock() throws Exception
	{
		given(aspect).hasNamespacedJsPackageStyle("src/appns/cjs")
			.and(aspect).hasClasses("appns.cjs.Class", "appns.cjs.Class")
			.and(aspect).indexPageRefersTo("appns.cjs.Class");
		when(aspect).requestReceivedInDev("js/prod/closure-whitespace/bundle.js", response);
		then(response).containsMinifiedClasses("appns.cjs.Class")
			.and(response).containsText("mergePackageBlock(window,{\"appns\":{\"cjs\":{}}});");
	}
	
}
