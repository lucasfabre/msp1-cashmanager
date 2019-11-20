package fr.cashmanager.command;

import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.cashmanager.accounts.Account;
import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.config.IConfig;
import fr.cashmanager.config.Preference;
import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.payment.Payment;
import fr.cashmanager.payment.PaymentProcessingService;
import fr.cashmanager.rpc.commands.IJsonRpcCommand;
import fr.cashmanager.rpc.exception.JsonRpcErrorCode;
import fr.cashmanager.rpc.exception.JsonRpcException;

/**
 * CommandValidateAndProcessTransaction
 */
public class CommandValidateAndProcessTransaction implements IJsonRpcCommand {

    public static String TRANSACTION_RETRY_SESSION_KEY = "Transaction_retry";

    private ServicesContainer services;
    private Account debtorAccount = null;

    /**
     * default constructor
     * require: BankAccountManagementService
     */
    public CommandValidateAndProcessTransaction(ServicesContainer serviceContainer) {
        this.services = serviceContainer;
    }

    /**
     * Get the command keyword
     */
    @Override
    public String getMethodName() {
        return "ValidateAndProcessTransaction";
    }

    /**
     * parse the command parameters
     */
    @Override
    public void parseParams(JsonNode params) throws JsonRpcException {
        String debtorAccountId = params.path("DebtorAccountId").asText();
        if (debtorAccountId == null || "".equals(debtorAccountId)) {
            throw new JsonRpcException(JsonRpcErrorCode.INVALID_PARAMS.getCode(), "DebtorAccountId param is null or empty");
        }
        try {
            this.debtorAccount = services.get(BankAccountManagementService.class).getAccountForId(debtorAccountId);
        } catch (NoSuchElementException e) {
            throw new JsonRpcException(JsonRpcErrorCode.INVALID_PARAMS.getCode(), e.getMessage());
        }
    }

    /**
     * Entrypoint of the command and handle the max retry
     */
    @Override
    public JsonNode execute(Map<String, Object> session) throws JsonRpcException {
        Integer retryNb = (Integer) session.getOrDefault(TRANSACTION_RETRY_SESSION_KEY, 0);
        if (retryNb >= this.getMaxTransactionRetry()) {
            this.cleanTransactionSession(session);
            throw new JsonRpcException(JsonRpcErrorCode.SERVICE_ERROR.getCode(), "Max retry exceeded for transaction");
        } else {
            try {
                return processTransaction(session);       
            } catch (JsonRpcException e) {
                retryNb++;
                session.put(TRANSACTION_RETRY_SESSION_KEY, retryNb);
                throw e;
            }
        }
    }

    /**
     * actual processing of the command
     * @param session the session
     * @return the ClientRpc result
     * @throws JsonRpcException
     */
    private JsonNode processTransaction(Map<String, Object> session) throws JsonRpcException {
        Payment transaction = (Payment) session.get(CommandStartTransaction.TRANSACTION_OBJECT_SESSION_KEY);
        Date transactionStartDate = (Date) session.get(CommandStartTransaction.TRANSACTION_STARTDATE_SESSION_KEY);
        if (transaction == null || transactionStartDate == null) {
            throw new JsonRpcException(JsonRpcErrorCode.INVALID_REQUEST.getCode(), "No transaction has been started");
        }
        Date transactionMaxValidityDate = new Date(transactionStartDate.getTime() + (this.getMaxTransactionDelayInSeconds() * 1000));
        transaction.withDebtor(debtorAccount.getId());
        if (new Date().compareTo(transactionMaxValidityDate) > 0) {
            throw new JsonRpcException(JsonRpcErrorCode.INVALID_REQUEST.getCode(), "The transaction max delay has been exceeded");
        }
        try {
            services.get(PaymentProcessingService.class).processTransaction(transaction);
        } catch (Exception e) {
            throw new JsonRpcException(JsonRpcErrorCode.SERVICE_ERROR.getCode(), e.getMessage());
        }
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("success", true);
        return result;
    }

    /**
     * clean the transaction in the session
     * @param session the session
     */
    private void cleanTransactionSession(Map<String, Object> session) {
        session.remove(CommandStartTransaction.TRANSACTION_OBJECT_SESSION_KEY);
        session.remove(CommandStartTransaction.TRANSACTION_STARTDATE_SESSION_KEY);
        session.remove(TRANSACTION_RETRY_SESSION_KEY);
    }

    /**
     * Create a new Instance of the command
     */
    @Override
    public IJsonRpcCommand newInstance() {
        return new CommandValidateAndProcessTransaction(services);
    }

    /**
     * get MaxTransactionDelayInSeconds from the config service
     */
    private Long getMaxTransactionDelayInSeconds() {
        IConfig config = services.get(IConfig.class);
        try {
            return Long.parseLong(config.getPreference(Preference.MAX_TRANSACTION_DELAY));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error while parsing " + Preference.MAX_TRANSACTION_DELAY.getName() + " in the configuration", e);
        }
    }

    /**
     * get MaxTransactionRetry from the config service
     */
    private Long getMaxTransactionRetry() {
        IConfig config = services.get(IConfig.class);
        try {
            return Long.parseLong(config.getPreference(Preference.MAX_TRANSACTION_RETRY));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error while parsing " + Preference.MAX_TRANSACTION_RETRY.getName() + " in the configuration", e);
        }
    }

}