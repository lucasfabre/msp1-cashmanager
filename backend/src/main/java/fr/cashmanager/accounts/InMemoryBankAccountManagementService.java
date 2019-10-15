package fr.cashmanager.accounts;

import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * InMemoryBankAccountManagementService
 */
@Service("BankAccountManagementService")
@Scope("singleton")
public class InMemoryBankAccountManagementService extends BankAccountManagementService {

    /**
     * The in memory account list
     */
    private Map<String, Account> accountByAccountId = new HashMap<String, Account>();

    /**
     * return the assciated account for the id
     * @param accountId the account id
     * @return the account with the same Id
     */
    protected Account getAccountForId(String accountId) throws NoSuchElementException {
        if (false == this.accountByAccountId.containsKey(accountId)) {
            throw new NoSuchElementException("No account match the account id: " + accountId);
        }
        return this.accountByAccountId.get(accountId);
    }

    /**
     * Save the account and his state
     * @param account the account to save
     */
    protected void saveAccount(Account account) {
        this.accountByAccountId.put(account.getId(), account);
    }
   
}