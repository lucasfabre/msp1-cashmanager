package fr.cashmanager.rpc.middlewares;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.cashmanager.impl.helpers.JsonMapperFactory;
import fr.cashmanager.impl.ioc.ServicesContainer;
import fr.cashmanager.rpc.exception.JsonRpcException;
import fr.cashmanager.rpc.exception.JsonRpcErrorCode;
import fr.cashmanager.rpc.helpers.JsonRpcHelper;
import fr.cashmanager.user.User;
import fr.cashmanager.user.UserManagementService;

/**
 * CommandMiddleware
 */
public class AuthenticationMiddleware extends JsonRpcMiddleware {

    public static String CONNECTED_USER_SESSION_KEY = "ConnectedUser";
    private ServicesContainer services;

    /**
     * default constructor
     * @param services the service container
     */
	public AuthenticationMiddleware(ServicesContainer services) {
		this.services = services;
	}

    /**
     * check if the user is connected
     */
	@Override
	public JsonNode impl() throws JsonRpcException {
        if (isUserAuthenticated()) {
            return next();
        } else if (isAuthenticationRequest()) {
            return authenticate();
        } else {
            throw new JsonRpcException(JsonRpcErrorCode.NOT_AUTHENTICATED);
        }
	}

    /**
     * check if the session contains the user
     */
    private boolean isUserAuthenticated() {
        return session.containsKey(CONNECTED_USER_SESSION_KEY);
    }

    /**
     * check if the request is an login request
     */
    private boolean isAuthenticationRequest() {
        return "Login".equals(JsonRpcHelper.getMethod(body));
    }

    /**
     * login request implementation
     * @return a api response
     * @throws JsonRpcException on error
     */
    private JsonNode authenticate() throws JsonRpcException {
        UserManagementService userManagementService = services.get(UserManagementService.class);
        String requestId = JsonRpcHelper.getParams(body).path("id").asText();
        String requestPassword = JsonRpcHelper.getParams(body).path("password").asText();
        User user = userManagementService.authenticateUser(requestId, requestPassword);
        if (user == null) {
            throw new JsonRpcException(JsonRpcErrorCode.SERVICE_ERROR.getCode(), "No user matching");
        } else {
            session.put(CONNECTED_USER_SESSION_KEY, user);
            ObjectMapper mapper = JsonMapperFactory.getObjectMapper();
            ObjectNode result = mapper.createObjectNode();
            result.put("success", true);
            return JsonRpcHelper.formatClientResult(JsonRpcHelper.getIdOrNull(body), result);
        }
    }
}