package fr.cashmanager.payment;

import fr.cashmanager.accounts.BankAccountManagementService;

/**
 * PaymentProcessingService
 */
public class PaymentProcessingService {

    private BankAccountManagementService bankAccountManagementService;

    public PaymentProcessingService(BankAccountManagementService bankAccountManagementService) {
        this.bankAccountManagementService = bankAccountManagementService;
    }

    /**
     * Process a Transaction
     * @param payment the transaction to process
     */
    public void processTransaction(IPayment payment) throws Exception {
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