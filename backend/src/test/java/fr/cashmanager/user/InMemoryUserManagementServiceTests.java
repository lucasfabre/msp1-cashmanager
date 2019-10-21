package fr.cashmanager.user;

import junit.framework.TestCase;

/**
 * InMemoryUserManagementServiceTests
 */
public class InMemoryUserManagementServiceTests extends TestCase {

    UserManagementService service = new InMemoryUserManagementService();

    public void testAuthenticateUser() {
        service.registerUser("id", "password");
        User user = service.authenticateUser("id", "password");
        assertEquals("id", user.getId());
        assertEquals("password", user.getPassword());
        assertNull(service.authenticateUser("i return", "null"));
    }

}