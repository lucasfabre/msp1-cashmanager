package fr.cashmanager.command;

import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.JsonNode;

import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.payment.Payment;
import fr.cashmanager.rpc.commands.IJsonRpcCommand;
import fr.cashmanager.rpc.exception.JsonRpcErrorCode;
import fr.cashmanager.rpc.exception.JsonRpcException;

/**
 * CommandStartTransaction
 */
public class CommandStartTransaction implements IJsonRpcCommand {

    public static String TRANSACTION_OBJECT_SESSION_KEY = "Transaction_object";
    public static String TRANSACTION_STARTDATE_SESSION_KEY = "Transaction_startDate";

    private ServicesContainer services;
    private String creditorAccountId = null;
    private Double amount = null;

    /**
     * default constructor
     * require: BankAccountManagementService
     */
    public CommandStartTransaction(ServicesContainer serviceContainer) {
        this.services = serviceContainer;
    }

    /**
     * Get the command keyword
     */
    @Override
    public String getMethodName() {
        return "StartTransaction";
    }

    /**
     * parse the command parameters
     */
    @Override
    public void parseParams(JsonNode params) throws JsonRpcException {
        this.creditorAccountId = params.path("CreditorAccountId").asText();
        if (creditorAccountId == null || "".equals(creditorAccountId)) {
            throw new JsonRpcException(JsonRpcErrorCode.INVALID_PARAMS.getCode(), "creditorAccountId param is null or empty");
        }
        this.amount = params.path("Amount").asDouble();
        if (this.amount <= 0) {
            throw new JsonRpcException(JsonRpcErrorCode.INVALID_PARAMS.getCode(), "Amount param is <= 0");
        }
    }

    /**
     * Execute the command
     */
    @Override
    public JsonNode execute(Map<String, Object> session) throws JsonRpcException {
        BankAccountManagementService bankAccountManagementService = services.get(BankAccountManagementService.class);
        Payment transaction;
        // check if account exist
        try {
            bankAccountManagementService.getAccountForId(this.creditorAccountId);
        } catch (NoSuchElementException e) {
            throw new JsonRpcException(JsonRpcErrorCode.INVALID_PARAMS.getCode(), e.getMessage());
        }
        transaction = new Payment()
            .withCreditor(creditorAccountId)
            .withAmount(amount);
        Date transactionStart = new Date();
        // store the informations in the session
        session.put(TRANSACTION_STARTDATE_SESSION_KEY, transactionStart);
        session.put(TRANSACTION_OBJECT_SESSION_KEY, transaction);
		return JsonMapperFactory.getObjectMapper().valueToTree(transaction);
	}

    /**
     * Create a new Instance of the command
     */
    @Override
    public IJsonRpcCommand newInstance() {
        return new CommandStartTransaction(services);
    }

}