package fr.cashmanager.accounts;

/**
 * Account
 * Object representing an account
 */
public class Account {

    /**
     * the id of the account
     */
    private String id;

    /**
     * the balance of the account
     */
    private Double balance;

    /**
     * access to the balance of the account
     * @return account balance
     */
    public Double getBalance() {
        return this.balance;
    }
    /**
     * set the balance of the account
     * @param balance the new balance
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    /**
     * access the id of the account
     * @return id of the account
     */
    public String getId() {
        return id;
    }

    /**
     * set the id of the account
     * @param id new id of the account
     */
    public void setId(String id) {
        this.id = id;
    }
}
