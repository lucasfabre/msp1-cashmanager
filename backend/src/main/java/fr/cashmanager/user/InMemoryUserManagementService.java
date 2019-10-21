package fr.cashmanager.user;

import java.util.ArrayList;
import java.util.List;

/**
 * InMemoryBankAccountManagementService
 */
public class InMemoryUserManagementService extends UserManagementService {

    protected List<User> users = new ArrayList<User>();

    /**
     * Get an user with the same password and id or null if no user match
     * @param userId the userId
     * @param password the password of the user
     * @return a matching user
     */
    @Override
    public User authenticateUser(String userId, String password) {
        return users.stream()
            .filter((User user) -> user.getId() == userId && user.getPassword() == password)
            .findFirst().orElse(null);
    }

    /**
     * register an user in the service
     * @param userId the userId
     * @param password the password
     */
    public void registerUser(String userId, String password) {
        User user = new User()
            .withId(userId)
            .withPassword(password);
        this.users.add(user);
    }
    
}