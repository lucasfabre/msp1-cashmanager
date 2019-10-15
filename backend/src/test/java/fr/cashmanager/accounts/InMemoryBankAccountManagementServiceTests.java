package fr.cashmanager.accounts;

/**
 * InMemoryBankAccountManagementServiceTests
 */
public class InMemoryBankAccountManagementServiceTests extends BankAccountManagementServiceTests {

    /**
     * We run the exacts same test as for the abstract service
     * because we don't need to implement getAccountForId and saveAccount
     * and the parrent Test test it
     */
    @Override
    protected void setUp() throws Exception {
        this.service = new InMemoryBankAccountManagementService();
    }
}