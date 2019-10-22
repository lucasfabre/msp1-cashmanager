
package fr.cashmanager.config;

import java.io.File;
import java.io.IOException;

import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.accounts.InMemoryBankAccountManagementService;
import fr.cashmanager.testutils.TestHelper;
import fr.cashmanager.user.InMemoryUserManagementService;
import fr.cashmanager.user.User;
import fr.cashmanager.user.UserManagementService;
import junit.framework.TestCase;

/**
 * PaymentProcessingServiceTests
 */
public class LocalFileConfigTest extends TestCase {

    private String testfilepath = null;
    private BankAccountManagementService bankAccountManagementService;
    private UserManagementService userManagementService;
    private IConfig configManager;

    public void setUp() throws IOException {
        // copy the test file
        testfilepath = File.createTempFile("cashmanagerTest", null).getAbsolutePath();
        System.setProperty("cashmanager.config.localfile", testfilepath);
        TestHelper.copyResource("/fr/cashmanager/config/localConfigTestFile.json", testfilepath, this.getClass());
        // init the service
        bankAccountManagementService = new InMemoryBankAccountManagementService();
        userManagementService = new InMemoryUserManagementService();
        configManager = new LocalFileConfig(bankAccountManagementService, userManagementService);
    }

    /**
     * test if the config initialize the services
     * @throws Exception
     */
    public void testConfig() throws Exception {
        configManager.configure();
        assertEquals(Double.valueOf(12.04), bankAccountManagementService.getAccountBalance("acc1"));
        User user = userManagementService.authenticateUser("user1", "p1");
        assertEquals("user1", user.getId());
        assertEquals("p1", user.getPassword());
        assertEquals("server port", configManager.getPreference(Preference.SERVER_PORT));
    }

    public void testNoConfigFileProvided() throws Exception {
        System.clearProperty("cashmanager.config.localfile");
        try {
            configManager.configure();
        } catch (Exception e) {
            assertEquals("No config file provided, please set the cashmanager.config.localfile property", e.getMessage());
        }
    }
 
    public void tearDown() {
        File tempFile = new File(testfilepath);
        tempFile.delete();
    }

}