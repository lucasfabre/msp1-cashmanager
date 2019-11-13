package fr.cashmanager;

import fr.cashmanager.accounts.BankAccountManagementService;
import fr.cashmanager.accounts.InMemoryBankAccountManagementService;
import fr.cashmanager.command.CommandDescribeAccount;
import fr.cashmanager.config.IConfig;
import fr.cashmanager.config.LocalFileConfig;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.logging.LoggerFactory;
import fr.cashmanager.logging.LoggerService;
import fr.cashmanager.payment.PaymentProcessingService;
import fr.cashmanager.rpc.clienthandler.ClientHandlerFactory;
import fr.cashmanager.rpc.clienthandler.JsonRpcClientHandlerFactory;
import fr.cashmanager.rpc.commands.JsonRpcCommandManager;
import fr.cashmanager.rpc.middlewares.CommandMiddleware;
import fr.cashmanager.rpc.middlewares.ErrorMiddleware;
import fr.cashmanager.rpc.server.IServer;
import fr.cashmanager.rpc.server.SocketServer;
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
            initCommandsAndMiddlewares(services);
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
    public static void initCommandsAndMiddlewares(ServicesContainer services) throws Exception {
        JsonRpcCommandManager commandManager = services.get(JsonRpcCommandManager.class);
        commandManager.registerCommand(new CommandDescribeAccount(services));

        commandManager.registerMiddleware(new ErrorMiddleware());
        commandManager.registerMiddleware(new CommandMiddleware(services));
    }

    /**
     * initialize the Ioc Container
     * @return the ioc container
     */
    public static ServicesContainer initContainer() {
        ServicesContainer services = new ServicesContainer();
        
        IConfig config = new LocalFileConfig(services);
        services.register(IConfig.class, config);

        LoggerService loggerService = new LoggerService(services);
        services.register(LoggerService.class, loggerService);

        LoggerFactory loggerFactory = new LoggerFactory(services);
        services.register(LoggerFactory.class, loggerFactory);

        UserManagementService userManagementService = new InMemoryUserManagementService();
        services.register(UserManagementService.class, userManagementService);

        BankAccountManagementService bankAccountManagementService = new InMemoryBankAccountManagementService();
        services.register(BankAccountManagementService.class, bankAccountManagementService);

        PaymentProcessingService paymentProcessingService = new PaymentProcessingService(services);
        services.register(PaymentProcessingService.class, paymentProcessingService);

        JsonRpcCommandManager jsonRpcCommandManager = new JsonRpcCommandManager();
        services.register(JsonRpcCommandManager.class, jsonRpcCommandManager);

        ClientHandlerFactory clientHandlerFactory = new JsonRpcClientHandlerFactory(services);
        services.register(ClientHandlerFactory.class, clientHandlerFactory);

        IServer server = new SocketServer(services);
        services.register(IServer.class, server);

        return services;
    }
}