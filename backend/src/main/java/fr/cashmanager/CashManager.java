package fr.cashmanager;

import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.accounts.InMemoryBankAccountManagementService;
import fr.cashmanager.command.CommandDescribeAccount;
import fr.cashmanager.config.IConfig;
import fr.cashmanager.config.LocalFileConfig;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.payment.PaymentProcessingService;
import fr.cashmanager.rpc.ClientHandlerFactory;
import fr.cashmanager.rpc.IServer;
import fr.cashmanager.rpc.JsonRpcClientHandlerFactory;
import fr.cashmanager.rpc.JsonRpcCommandManager;
import fr.cashmanager.rpc.SocketServer;
import fr.cashmanager.user.InMemoryUserManagementService;
import fr.cashmanager.user.UserManagementService;

/**
 * Main
 */
public class CashManager {

    /**
     * Main method for the cashManager
     */
    public static void main(String[] args) {
        ServicesContainer services = initContainer();
        try {
            initServices(services);
            initCommands(services);
            IServer server = services.get(IServer.class);
            server.listen();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * init the services who need to be initialized at the start of the app
     * @param container the service container
     * @throws Exception
     */
    public static void initServices(ServicesContainer container) throws Exception {
        IConfig config = container.get(IConfig.class);
        config.configure();
    }

    /**
     * init commands
     * @param container the service container
     * @throws Exception
     */
    public static void initCommands(ServicesContainer services) throws Exception {
        JsonRpcCommandManager commandManager = services.get(JsonRpcCommandManager.class);
        commandManager.registerCommand(new CommandDescribeAccount(services));
    }

    /**
     * initialize the Ioc Container
     * @return the ioc container
     */
    public static ServicesContainer initContainer() {
        ServicesContainer container = new ServicesContainer();
        
        UserManagementService userManagementService = new InMemoryUserManagementService();
        container.register(UserManagementService.class, userManagementService);

        BankAccountManagementService bankAccountManagementService = new InMemoryBankAccountManagementService();
        container.register(BankAccountManagementService.class, bankAccountManagementService);

        IConfig config = new LocalFileConfig(container);
        container.register(IConfig.class, config);

        PaymentProcessingService paymentProcessingService = new PaymentProcessingService(container);
        container.register(PaymentProcessingService.class, paymentProcessingService);

        JsonRpcCommandManager jsonRpcCommandManager = new JsonRpcCommandManager();
        container.register(JsonRpcCommandManager.class, jsonRpcCommandManager);

        ClientHandlerFactory clientHandlerFactory = new JsonRpcClientHandlerFactory(container);
        container.register(ClientHandlerFactory.class, clientHandlerFactory);

        IServer server = new SocketServer(container);
        container.register(IServer.class, server);

        return container;
    }
}