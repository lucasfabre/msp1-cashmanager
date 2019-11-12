package fr.cashmanager.command;

import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.JsonNode;

import fr.cashmanager.accounts.Account;
import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.rpc.IJsonRpcCommand;

/**
 * GetAccount
 */
public class CommandDescribeAccount implements IJsonRpcCommand {

    private ServicesContainer services;
    private String accountId = null;

    /**
     * default constructor
     */
    public CommandDescribeAccount(ServicesContainer serviceContainer) {
        this.services = serviceContainer;
    }

    /**
     * Get the command keyword
     */
    @Override
    public String getMethodName() {
        return "DescribeAccount";
    }

    /**
     * parse the command parameters
     */
    @Override
    public void parseParams(JsonNode params) throws Exception {
        this.accountId = params.path("accountId").asText();
    }

    /**
     * Execute the command
     */
    @Override
    public JsonNode execute() throws Exception {
        BankAccountManagementService bankAccountManagementService = services.get(BankAccountManagementService.class);
        Account account;
        try {
            account = bankAccountManagementService.getAccountForId(this.accountId);
        } catch (NoSuchElementException e) {
            throw new Exception(e.getMessage());
        }
		return JsonMapperFactory.getObjectMapper().valueToTree(account);
	}

    /**
     * Create a new Instance of the command
     */
    @Override
    public IJsonRpcCommand newInstance() {
        return new CommandDescribeAccount(services);
    }

    
}