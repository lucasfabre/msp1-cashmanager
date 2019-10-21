
package fr.cashmanager.config;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.accounts.InMemoryBankAccountManagementService;
import fr.cashmanager.testutils.TestHelper;

/**
 * PaymentProcessingServiceTests
 */
public class LocalFileConfigTest {

    private static String testfilepath = null;
    private static BankAccountManagementService bankAccountManagementService;

    @Before
    public void setUp() throws IOException {
        bankAccountManagementService = new InMemoryBankAccountManagementService();
        testfilepath = File.createTempFile("temp", null).getAbsolutePath();
        System.setProperty("cashmanager.config.localfile", testfilepath);
        TestHelper.copyResource("/fr/cashmanager/config/localConfigTestFile.json", testfilepath, this.getClass());
    }

    @Test
    public void testConfig() throws Exception {
        System.setProperty("cashmanager.config.localfile", testfilepath);
        IConfig configManager = new LocalFileConfig(bankAccountManagementService);
        configManager.configure();
        assertEquals(Double.valueOf(12.04), bankAccountManagementService.getAccountBalance("1"));
    }

    @Test
    public void testNoConfigFileProvided() throws Exception {
        IConfig configManager = new LocalFileConfig(bankAccountManagementService);
        try {
            configManager.configure();
        } catch (Exception e) {
            assertEquals("No config file provided, please set the cashmanager.config.localfile property", e.getMessage());
        }
    }
 
    @After
    public void tearDown() {
        File tempFile = new File(testfilepath);
        tempFile.delete();
    }

}