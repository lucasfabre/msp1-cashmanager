package fr.cashmanager.config;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cashmanager.accounts.Account;
import fr.cashmanager.accounts.BankAccountManagementService;

/**
 * LocalFileConfig
 */
public class LocalFileConfig implements IConfig {

    // The property is set by spring
    private String filePathAString = null;
    
    // We use a static unique object mapper because the object mapper is slow to instanciate
    private static ObjectMapper mapper = new ObjectMapper();
    private BankAccountManagementService bankAccountManagementService;

    /**
     * Local File DTO used for deserializing the JSON config file;
     */
    static private class LocalFileDTO {
        public List<Account> accounts;
    }

    public LocalFileConfig(BankAccountManagementService bankAccountManagementService) {
        String localfilePropertyValue = System.getProperty("cashmanager.config.localfile");
        if (localfilePropertyValue != null) {
            this.filePathAString = localfilePropertyValue;
        }
        this.bankAccountManagementService = bankAccountManagementService;
    }

    /**
     * This method load the configuration in the local file
     * and inject the accounts in the accountManagementService
     */
    @Override
    public void configure() throws Exception {
        if (this.filePathAString == null) {
            throw new Exception("No config file provided, please set the cashmanager.config.localfile property");
        }
        LocalFileDTO fileConfig = mapper.readValue(new File(filePathAString), LocalFileDTO.class);
        for (Account account : fileConfig.accounts) {
            bankAccountManagementService.registerNewAcount(account.getId(), account.getBalance());
        }
    }
    
}