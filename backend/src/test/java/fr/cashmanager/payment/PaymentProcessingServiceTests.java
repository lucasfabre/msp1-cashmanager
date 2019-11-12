package fr.cashmanager.payment;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.accounts.InMemoryBankAccountManagementService;
import fr.cashmanager.impl.ioc.ServicesContainer;

/**
 * PaymentProcessingServiceTests
 */
public class PaymentProcessingServiceTests {

    private BankAccountManagementService bankAccountManagementService;
    private ServicesContainer container = new ServicesContainer();

    @Before
    public void setUp() {
        bankAccountManagementService = new InMemoryBankAccountManagementService();
        container.register(BankAccountManagementService.class, bankAccountManagementService);
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
        IPayment payment = new IPayment(){
            public String getDebtor() {
                return "1";
            }
        
            public String getCreditor() {
                return "2";
            }
        
            public Double getAmount() {
                return 20.0;
            }
        };

        // process the transaction
        PaymentProcessingService service = new PaymentProcessingService(container);
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
        IPayment payment = new IPayment(){
            public String getDebtor() {
                return "1";
            }
        
            public String getCreditor() {
                return "2";
            }
        
            public Double getAmount() {
                return 20.0;
            }
        };

        // process the transaction
        PaymentProcessingService service = new PaymentProcessingService(container);
        try {
           service.processTransaction(payment);
        } catch (Exception e) {
            // fail silently
        }

        // check the result
        assertEquals(Double.valueOf(120.0), bankAccountManagementService.getAccountBalance("1"));
    }

}