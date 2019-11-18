package fr.cashmanager.integration;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Before;

import fr.cashmanager.CashManager;
import fr.cashmanager.config.IConfig;
import fr.cashmanager.config.Preference;
import fr.cashmanager.testutils.TestHelper;

/**
 * IntegrationTests
 */
public abstract class ScenarioTestBase extends IntegrationTestBase {

    protected MockedLocalFileConfig mockedLocalFileConfig;

    @Before
    @Override
    public void setUpAppContext() throws Exception {
        // copy the test file
        String testfilepath = File.createTempFile("cashmanagerTest", null).getAbsolutePath();
        System.setProperty("cashmanager.config.localfile", testfilepath);
        TestHelper.copyResource("/fr/cashmanager/integration/CashManagerConfig.json", testfilepath, this.getClass());
        // Init the app like in prod
        services = CashManager.initContainer();
        // Overide the IConfig
        mockedLocalFileConfig = new MockedLocalFileConfig(services);
        services.register(IConfig.class, mockedLocalFileConfig);
        // init the services
        CashManager.initServices(services);
        CashManager.initCommandsAndMiddlewares(services);
        // change the server port
        mockedLocalFileConfig.setPreference(Preference.SERVER_PORT.getName(), Integer.valueOf(getServerPort()).toString());
    }

    protected void runScenario(String scenarioResource) throws Exception {
        waitForServerStarted();
        initClientConection(getServerPort());
        BufferedReader scenarioReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(scenarioResource), StandardCharsets.UTF_8));
        String scenarioline = scenarioReader.readLine();
        String lastReceivedLine = "";
        while (scenarioline != null && false == "".equals(scenarioline)) {
            if (scenarioline.startsWith("> ")) {
                lastReceivedLine = writeMessageAndWaitResponse(scenarioline.substring(2));
            } else {
                assertEquals(scenarioline.substring(2), lastReceivedLine);
            }
            scenarioline = scenarioReader.readLine();
        }
        closeConectionAndStopServer();
    }

    protected abstract int getServerPort();

}