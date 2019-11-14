package fr.cashmanager.integration;

import static org.junit.Assert.assertEquals;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.Test;

import fr.cashmanager.CashManager;
import fr.cashmanager.config.IConfig;
import fr.cashmanager.config.Preference;
import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.testutils.TestHelper;

/**
 * IntegrationTests
 */
public class IntegrationTests extends IntegrationTestBase {

    protected int SERVER_PORT = 3814;

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
        MockedLocalFileConfig mockedLocalFileConfig = new MockedLocalFileConfig(services);
        services.register(IConfig.class, mockedLocalFileConfig);
        // init the services
        CashManager.initServices(services);
        CashManager.initCommandsAndMiddlewares(services);
        // change the server port
        mockedLocalFileConfig.setPreference(Preference.SERVER_PORT.getName(), Integer.valueOf(SERVER_PORT).toString());
    }

    @Test
    public void testSocket() throws Exception {
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
        waitForServerStarted();
        // Client
        initClientConection(SERVER_PORT);
        String response = writeMessageAndWaitResponse("{\"jsonrpc\": \"2.0\", \"method\": \"DescribeAccount\", \"params\": { \"accountId\": \"acc1\" }, \"id\": 2}");
        JsonNode res = mapper.readTree(response);
        assertEquals(Double.valueOf(12.04), Double.valueOf(res.path("result").path("balance").asDouble()));
        closeConectionAndStopServer();
    }

}