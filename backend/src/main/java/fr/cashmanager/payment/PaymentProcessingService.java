package fr.cashmanager.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cashmanager.accounts.BankAccountManagementService;

/**
 * PaymentProcessingService
 */
@Service
public class PaymentProcessingService {

    private BankAccountManagementService bankAccountManagementService;

    @Autowired(required = true)
    public PaymentProcessingService(BankAccountManagementService bankAccountManagementService) {
        this.bankAccountManagementService = bankAccountManagementService;
    }

    /**
     * Process a Transaction
     * @param payment the transaction to process
     */
    public void processTransaction(IPayment payment) {
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