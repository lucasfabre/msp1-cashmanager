package fr.cashmanager.payment;

import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.impl.ioc.ServicesContainer;

/**
 * PaymentProcessingService
 */
public class PaymentProcessingService {

    private ServicesContainer services;

    /**
     * default constructor
     * require: BankAccountManagementService
     * @param container the ioc container
     */
    public PaymentProcessingService(ServicesContainer services) {
        this.services = services;
    }

    /**
     * Process a Transaction
     * @param payment the transaction to process
     */
    public void processTransaction(IPayment payment) throws Exception {
        BankAccountManagementService bankAccountManagementService = services.get(BankAccountManagementService.class);
        final Double amount = payment.getAmount();
        bankAccountManagementService.debitAccount(payment.getDebtor(), amount);
        try {
            bankAccountManagementService.creditAccount(payment.getCreditor(), amount);
        } catch (Exception e) {
            // In case of any exception at this level we want to refund the debter
            bankAccountManagementService.creditAccount(payment.getDebtor(), amount);
            throw e;
        }
    }
}