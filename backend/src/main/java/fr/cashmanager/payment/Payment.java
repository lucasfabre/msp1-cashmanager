package fr.cashmanager.payment;

/**
 * IPayment
 * Interface abstracting a transaction
 */
public class Payment {

    private String creditor;
    private String debtor;
    public Double amount;

    public Payment() {
    }
    
    /**
     * Get the creditor account ID
     * @return the creditor account ID
     */
    public String getCreditor() {
        return creditor;
    }
     
    /**
     * Get the debtor account ID
     * @return the debtor account ID
     */
    public String getDebtor() {
        return debtor;
    }

    /**
     * Get the amount of the transaction
     * @return the amount of the transaction
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Set the creditor and return the object
     * @param creditor the creditor
     * @return this
     */
    public Payment withCreditor(String creditor) {
        this.creditor = creditor;
        return this;
    }

    /**
     * Set the debtor and return the object
     * @param debtor the debtor
     * @return this
     */
    public Payment withDebtor(String debtor) {
        this.debtor = debtor;
        return this;
    }

    /**
     * Set the amount and return the object
     * @param amount the amount
     * @return this
     */
    public Payment withAmount(Double amount) {
        this.amount = amount;
        return this;
    }
}