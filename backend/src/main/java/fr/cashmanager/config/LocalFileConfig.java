package fr.cashmanager.config;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cashmanager.accounts.Account;
import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.user.User;
import fr.cashmanager.user.UserManagementService;

/**
 * LocalFileConfig
 */
public class LocalFileConfig implements IConfig {

    // The property is set by spring
    private String filePathAString = null;

    private ServicesContainer services;

    // Loaded Preferences
    protected Map<String, String>  preferences;

    /**
     * Local File DTO used for deserializing the JSON config file;
     */
    static private class LocalFileDTO {
        public List<Account>        accounts;
        public List<User>           users;
        public Map<String, String>  preferences;
    }

    /**
     * default constructor
     * require: BankAccountManagementService, UserManagementService
     * @param ServicesContainer the ioc container
     */
    public LocalFileConfig(ServicesContainer services) {
        this.services = services;
        String localfilePropertyValue = System.getProperty("cashmanager.config.localfile");
        if (localfilePropertyValue != null) {
            this.filePathAString = localfilePropertyValue;
        }
    }

    /**
     * This method load the configuration in the local file
     * and inject the accounts in the accountManagementService
     */
    @Override
    public void configure() throws Exception {
        BankAccountManagementService bankAccountManagementService = services.get(BankAccountManagementService.class);
        UserManagementService userManagementService = services.get(UserManagementService.class);
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
        if (this.filePathAString == null) {
            throw new Exception("No config file provided, please set the cashmanager.config.localfile property");
        }
        LocalFileDTO fileConfig = mapper.readValue(new File(filePathAString), LocalFileDTO.class);
        this.preferences = new HashMap<String, String>(fileConfig.preferences);
        fileConfig.accounts.forEach((account) ->
            bankAccountManagementService.registerNewAcount(account.getId(), account.getBalance())
        );
        fileConfig.users.forEach((user) ->
            userManagementService.registerUser(user.getId(), user.getPassword())
        );
    }
    
    /**
     * return the preference value
     * @param preference the preference
     * @return the value or the default value
     */
    public String getPreference(Preference preference) {
        String value = this.preferences.get(preference.getName());
        return (value != null) ? value : preference.getDefaultValue();
    }

}