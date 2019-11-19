package fr.cashmanager.command;

import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cashmanager.accounts.Account;
import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.accounts.InMemoryBankAccountManagementService;
import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.rpc.commands.IJsonRpcCommand;
import junit.framework.TestCase;

/**
 * BankAccountManagementServiceTests
 */
public class CommandDescribeAccountTest extends TestCase {

    private ServicesContainer services = new ServicesContainer();
    private CommandDescribeAccount describeAccountCommand;

    public void initAppContext() {
        BankAccountManagementService bankAccountManagementService = new InMemoryBankAccountManagementService();
        bankAccountManagementService.registerNewAcount("acc1", Double.parseDouble("12"));
        services.register(BankAccountManagementService.class, bankAccountManagementService);
        this.describeAccountCommand = new CommandDescribeAccount(services);
    }

    /**
     * Create the service with a in memory mock
     */
    @Override
    protected void setUp() throws Exception {
        initAppContext();
    }

    /**
     * Test the creditAccount method
     */
    public void testMethodName() {
        assertEquals("DescribeAccount", describeAccountCommand.getMethodName());
    }

    /**
     * Test the command
     */
    public void testExec() throws Exception {
        ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
        JsonNode params = mapper.readTree("{\"accountId\": \"acc1\"}");
        IJsonRpcCommand cmd = describeAccountCommand.newInstance();
        cmd.parseParams(params);
        JsonNode res = cmd.execute(new HashMap<String, Object>());
        Account acc = mapper.treeToValue(res, Account.class);
        assertEquals(Double.valueOf(12), Double.valueOf(acc.getBalance()));
        assertEquals("acc1", acc.getId());
    }

}