package org.bladerunnerjs.spec.app;

import static org.bladerunnerjs.model.App.Messages.APP_DEPLOYED_LOG_MSG;
import static org.bladerunnerjs.model.App.Messages.APP_DEPLOYMENT_FAILED_LOG_MSG;

import org.bladerunnerjs.model.App;
import org.bladerunnerjs.model.Aspect;
import org.bladerunnerjs.model.DirNode;
import org.bladerunnerjs.model.JsLib;
import org.bladerunnerjs.model.NamedDirNode;
import org.bladerunnerjs.model.events.AppDeployedEvent;
import org.bladerunnerjs.model.events.NodeReadyEvent;
import org.bladerunnerjs.model.exception.name.InvalidRootPackageNameException;
import org.bladerunnerjs.testing.specutility.engine.SpecTest;
import org.junit.Before;
import org.junit.Test;


public class AppTest extends SpecTest {
	
	private JsLib sdkLib;
	private JsLib globalNonBladeRunnerLib;
	private JsLib appNonBladeRunnerLib;
	private JsLib globalOverriddenNonBRLib;
	private JsLib appOverriddenNonBRLib;
	private App app;
	private JsLib appLib;
	private NamedDirNode appTemplate;
	private DirNode appJars;
	private Aspect aspect;
	
	@Before
	public void initTestObjects() throws Exception
	{
		given(brjs).hasBeenCreated();
			sdkLib = brjs.sdkLib("br");
			app = brjs.app("app1");
			appLib = app.jsLib("lib1");
			appTemplate = brjs.template("app");
			appJars = brjs.appJars();
			aspect = app.aspect("default");
			globalNonBladeRunnerLib = brjs.sdkLib("legacy-thirdparty");
			appNonBladeRunnerLib = app.appJsLib("app-legacy-thirdparty");
			globalOverriddenNonBRLib = brjs.sdkLib("overridden-lib");
			appOverriddenNonBRLib = app.appJsLib("overridden-lib");
	}
	
	// TODO: does this add anything over the baselining test?
	@Test
	public void weCanCreateAnAppUsingATemplate() throws Exception {
		given(appTemplate).containsFile("some-file.blah")
			.and(logging).enabled();
		when(app).populate("appx");
		then(app).dirExists()
			.and(app).hasFile("some-file.blah")
			.and(logging).noMessagesLogged();
	}
	
	@Test
	public void populatingAnAppCausesRootObserversToBeNotified() throws Exception {
		given(observer).observing(brjs);
		when(app).populate();
		then(observer).notified(NodeReadyEvent.class, app)
			.and(observer).notified(NodeReadyEvent.class, aspect);
	}
	
	@Test
	public void theAppConfIsWrittenOnPopulate() throws Exception {
		given(appTemplate).hasBeenCreated();
		when(app).populate("appx");
		then(app).fileHasContents("app.conf", "localeCookieName: BRJS.LOCALE\nlocales: en\nrequirePrefix: appx");
	}
	
	@Test
	public void theAppConfIsNotWrittenOnZeroArgPopulate() throws Exception {
		given(appTemplate).hasBeenCreated();
		when(app).populate();
		then(app).doesNotHaveFile("app.conf");
	}
	
	@Test
	public void theAppConfCanBeManuallyWrittenOnZeroArgPopulate() throws Exception {
		given(appTemplate).hasBeenCreated()
			.and(app).hasBeenPopulated();
		when(app).appConf().write();
		then(app).fileHasContents("app.conf", "localeCookieName: BRJS.LOCALE\nlocales: en\nrequirePrefix: appns");
	}
	
	@Test
	public void invalidAppNameSpaceThrowsException() throws Exception {
		given(appTemplate).containsFile("some-file.blah");
		when(app).populate("appX");
		then(exceptions).verifyException(InvalidRootPackageNameException.class, app.dir(), unquoted("'appX'"));
	}
	
	@Test
	public void usingJSKeywordAsAppNameSpaceThrowsException() throws Exception {
		given(appTemplate).containsFile("some-file.blah");
		when(app).populate("transient");
		then(exceptions).verifyException(InvalidRootPackageNameException.class, app.dir(), unquoted("'transient'"));
	}
	
	@Test
	public void usingReservedKeywordAsAppNameSpaceThrowsException() throws Exception {
		given(appTemplate).containsFile("some-file.blah");
		when(app).populate("caplinx");
		then(exceptions).verifyException(InvalidRootPackageNameException.class, app.dir(), unquoted("'caplinx'"));
	}
	
	@Test
	public void appIsBaselinedDuringPopulation() throws Exception {
		given(appTemplate).containsFolder("@appns");
		when(app).populate("appx");
		then(app).dirExists()
			.and(app).hasDir("appx")
			.and(app).doesNotHaveDir("appns");
	}
	
	@Test
	public void appLibsAreInstalledWhenAppIsDeployed() throws Exception {
		given(app).hasBeenCreated()
			.and(appJars).containsFile("some-lib.jar")
			.and(logging).enabled();
		when(app).deployApp();
		then(app).hasFile("WEB-INF/lib/some-lib.jar")
			.and(logging).infoMessageReceived(APP_DEPLOYED_LOG_MSG, "app1", app.dir().getPath());
	}
	
	@Test
	public void deployedEventIsFiredWhenAppDeployed() throws Exception {
		given(app).hasBeenCreated()
			.and(appJars).containsFile("some-lib.jar")
			.and(observer).observing(brjs);
		when(app).deployApp();
		then(observer).notified(AppDeployedEvent.class, app);
	}
	
	// TODO: use this test (or a test like it) to verify that we log about population before logging about app deployment, so if it fails you
	// have a better idea what's going on
	@Test
	public void exceptionIsThrownIfThereAreNoAppLibs() throws Exception {
		given(app).hasBeenCreated()
			.and(logging).enabled();
		when(app).deployApp();
		then(logging).errorMessageReceived(APP_DEPLOYMENT_FAILED_LOG_MSG, app.getName(), app.dir().getPath())
			.and(exceptions).verifyException(IllegalStateException.class, appJars.dir().getPath());
	}
	
	@Test
	public void globalLibsAreWrappedSoTheCorrectAppIsReturned() throws Exception {
		given(app).hasBeenCreated()
			.and(appLib).hasBeenCreated()
			.and(sdkLib).hasBeenCreated();
		given(app).hasLibs(sdkLib, appLib);
		then(app).libsReturnCorrectApp();
	}
	
	@Test
	public void nonBladerunnerLibsAreWrappedSoTheCorrectAppIsReturned() throws Exception {
		given(app).hasBeenCreated()
			.and(appLib).hasBeenCreated()
			.and(sdkLib).hasBeenCreated()
			.and(globalNonBladeRunnerLib).hasBeenCreated()
			.and(appNonBladeRunnerLib).hasBeenCreated();
		given(app).hasLibs(sdkLib, globalNonBladeRunnerLib, appNonBladeRunnerLib, appLib);
		then(app).libsReturnCorrectApp();
	}
	
	@Test
	public void appLibsContainBothAppLibsAndSdkLibs() throws Exception {
		given(app).hasBeenCreated()
			.and(appLib).hasBeenCreated()
			.and(sdkLib).hasBeenCreated();
		then(app).hasLibs(sdkLib, appLib);
	}
	
	@Test
	public void appLibsContainBothAppLibsAndNonBladerunnerLibs() throws Exception {
		given(app).hasBeenCreated()
    		.and(appLib).hasBeenCreated()
    		.and(sdkLib).hasBeenCreated()
    		.and(globalNonBladeRunnerLib).hasBeenCreated()
    		.and(appNonBladeRunnerLib).hasBeenCreated();
		then(app).hasLibs(sdkLib, globalNonBladeRunnerLib, appNonBladeRunnerLib, appLib);
	}
	
	@Test
	public void overriddenLibsDontAppearTwiceInLibsList() throws Exception {
		given(app).hasBeenCreated()
    		.and(appLib).hasBeenCreated()
    		.and(sdkLib).hasBeenCreated()
    		.and(globalOverriddenNonBRLib).hasBeenCreated()
    		.and(appOverriddenNonBRLib).hasBeenCreated();
		then(app).hasLibs(sdkLib, appOverriddenNonBRLib, appLib);
	}
	
	@Test
	public void appNonBRLibsCanOverrideGlobalLibs() throws Exception {
		given(app).hasBeenCreated()
			.and(appLib).hasBeenCreated()
			.and(globalOverriddenNonBRLib).hasBeenCreated()
			.and(appOverriddenNonBRLib).hasBeenCreated();
		then(app).libWithNameIs("overridden-lib", appOverriddenNonBRLib);
	}
	
	@Test
	public void appIsAvailableImmediatelyAfterCreationSinceFileModificationServiceListensForReadyEvent() throws Exception {
		given(brjs).hasBeenAuthenticallyCreated();
		when(brjs.app("app1")).populate();
		then(brjs).hasApps("app1");
	}
}
