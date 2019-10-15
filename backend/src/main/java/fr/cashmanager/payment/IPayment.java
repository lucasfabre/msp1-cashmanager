package fr.cashmanager.payment;

/**
 * IPayment
 * Interface abstracting a transaction
 */
public interface IPayment {

    /**
     * Get the creditor account ID
     * @return the creditor account ID
     */
    public String getCreditor();
     
    /**
     * Get the debtor account ID
     * @return the debtor account ID
     */
    public String getDebtor();

    /**
     * Get the amount of the transaction
     * @return the amount of the transaction
     */
    public Double getAmount();
}