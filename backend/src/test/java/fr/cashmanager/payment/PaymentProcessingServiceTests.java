package fr.cashmanager.payment;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.accounts.InMemoryBankAccountManagementService;
import fr.cashmanager.config.IConfig;
import fr.cashmanager.config.LocalFileConfig;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.logging.LoggerFactory;
import fr.cashmanager.logging.LoggerService;

/**
 * PaymentProcessingServiceTests
 */
public class PaymentProcessingServiceTests {

    private BankAccountManagementService bankAccountManagementService;
    private ServicesContainer services = new ServicesContainer();

    @Before
    public void setUp() {
        IConfig config = new LocalFileConfig(services);
        services.register(IConfig.class, config);

        LoggerService loggerService = new LoggerService(services);
        services.register(LoggerService.class, loggerService);

        LoggerFactory loggerFactory = new LoggerFactory(services);
        services.register(LoggerFactory.class, loggerFactory);

        bankAccountManagementService = new InMemoryBankAccountManagementService();
        services.register(BankAccountManagementService.class, bankAccountManagementService);
    }

    /**
     * Test the transacton Processing
     */
    @Test
    public void testProcessTransaction() throws Exception {
        // Init the banks accounts
        bankAccountManagementService.registerNewAcount("1", 120.0);
        bankAccountManagementService.registerNewAcount("2", 90.0);

        // create the transaction
        Payment payment = new Payment()
            .withDebtor("1")
            .withCreditor("2")
            .withAmount(Double.valueOf(20.0));

        // process the transaction
        PaymentProcessingService service = new PaymentProcessingService(services);
        service.processTransaction(payment);

        // check the result
        assertEquals(Double.valueOf(100.0), bankAccountManagementService.getAccountBalance("1"));
        assertEquals(Double.valueOf(110.0), bankAccountManagementService.getAccountBalance("2"));
    }

    /**
     * Test the transacton Processing
     * If the second account doesn't exist the first is not withdrawed
     */
    @Test
    public void testOnError() {
        // Init the banks accounts
        bankAccountManagementService.registerNewAcount("1", 120.0);

        // create the transaction
        Payment payment = new Payment()
            .withDebtor("1")
            .withCreditor("2")
            .withAmount(Double.valueOf(20.0));


        // process the transaction
        PaymentProcessingService service = new PaymentProcessingService(services);
        services.get(LoggerService.class).disableLogs();
        try {
           service.processTransaction(payment);
        } catch (Exception e) {
            // fail silently
        }
        services.get(LoggerService.class).enableLogs();

        // check the result
        assertEquals(Double.valueOf(120.0), bankAccountManagementService.getAccountBalance("1"));
    }

}