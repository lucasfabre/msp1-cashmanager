package fr.cashmanager.payment;

import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.logging.Logger;
import fr.cashmanager.logging.LoggerFactory;

/**
 * PaymentProcessingService
 */
public class PaymentProcessingService {

    private ServicesContainer services;
    private Logger log;

    /**
     * default constructor
     * require: BankAccountManagementService
     * @param container the ioc container
     */
    public PaymentProcessingService(ServicesContainer services) {
        this.services = services;
        this.log = services.get(LoggerFactory.class).getLogger("PaymentProcessingService");
    }

    /**
     * Process a Transaction
     * @param payment the transaction to process
     */
    public void processTransaction(Payment payment) throws Exception {
        BankAccountManagementService bankAccountManagementService = services.get(BankAccountManagementService.class);
        final Double amount = payment.getAmount();
        bankAccountManagementService.debitAccount(payment.getDebtor(), amount);
        try {
            bankAccountManagementService.creditAccount(payment.getCreditor(), amount);
        } catch (Exception e) {
            log.error("An error Occured while processing a transaction", e);
            // In case of any exception at this level we want to refund the debter
            bankAccountManagementService.creditAccount(payment.getDebtor(), amount);
            throw e;
        }
    }
}