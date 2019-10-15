package fr.cashmanager.accounts;

import java.util.NoSuchElementException;

/**
 * BankAccountManagementService
 * This class manage the accounts operations
 */
public abstract class BankAccountManagementService {

    /**
     * return the assciated account for the id
     * @param accountId the account id
     * @return the account with the same Id
     */
    protected abstract Account getAccountForId(String accountId) throws NoSuchElementException;

    /**
     * Save the account and his state
     * @param account the account to save
     */
    protected abstract void saveAccount(Account account);

    /**
     * Credit an accont by the given amount
     * @param accoundId the account id
     * @param amount the amount to credit
     */
    public void creditAccount(String accoundId, Double amount) throws NoSuchElementException {
        Account account = this.getAccountForId(accoundId);
        Double balance = account.getBalance();
        balance = balance + amount;
        account.setBalance(balance);
        this.saveAccount(account);
    }

    /**
     * Debit an accont by the given amount
     * @param accoundId the account id
     * @param amount the amount to debit
     */
    public void debitAccount(String accoundId, Double amount) throws NoSuchElementException {
        Account account = this.getAccountForId(accoundId);
        Double balance = account.getBalance();
        balance = balance - amount;
        account.setBalance(balance);
        this.saveAccount(account);
    }

    /**
     * register a new account with the given id and balance
     * @param accountId the account id
     * @param balance the account balance
     */
    public void registerNewAcount(String accountId, Double balance) {
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(balance);
        this.saveAccount(account);
    }

    /**
     * Get the balance of an account
     * @return the balance
     */
    public Double getAccountBalance(String accountId) throws NoSuchElementException {
        return this.getAccountForId(accountId).getBalance();
    }

}