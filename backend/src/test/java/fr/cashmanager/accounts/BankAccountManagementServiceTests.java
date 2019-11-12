package fr.cashmanager.accounts;

import java.util.HashMap;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

/**
 * BankAccountManagementServiceTests
 */
public class BankAccountManagementServiceTests extends TestCase {

    protected BankAccountManagementService service = null;

    /**
     * Create the service with a in memory mock
     */
    @Override
    protected void setUp() throws Exception {
        this.service = new BankAccountManagementService(){
            private HashMap<String, Account> testStorage = new HashMap<String, Account>();

            @Override
            protected void saveAccount(Account account) {
                this.testStorage.put(account.getId(), account);
            }
        
            @Override
            public Account getAccountForId(String accountId) throws NoSuchElementException {
                return testStorage.get(accountId);
            }
        };
        super.setUp();
    }

    /**
     * Test the creditAccount method
     */
    public void testCreditAccount() {
        service.registerNewAcount("1", 12.0);
        service.creditAccount("1", 3.0);
        assertEquals(15.0, service.getAccountBalance("1"));
    }

    /**
     * Test the debitAccount method
     */
    public void testDebitAccount() {
        service.registerNewAcount("1", 12.0);
        service.debitAccount("1", 2.0);
        assertEquals(10.0, service.getAccountBalance("1"));
    }

    /**
     * Test the registerNewAccountMethod method
     */
    public void testRegisterNewAcount() {
        service.registerNewAcount("1", 12.0);
        assertEquals(12.0, service.getAccountBalance("1"));
        service.registerNewAcount("1", 20.0);
        assertEquals(20.0, service.getAccountBalance("1"));
    }

}