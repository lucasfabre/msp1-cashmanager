package fr.cashmanager.user;

/**
 * UserManagementService
 * This class manage the accounts operations
 */
public abstract class UserManagementService {

    /**
     * Get an user with the same password and id or null if no user match
     * @param userId the userId
     * @param password the password of the user
     * @return a matching user
     */
    public abstract User authenticateUser(String userId, String password);

    /**
     * register an user in the service
     * @param userId the userId
     * @param password the password
     */
    public abstract void registerUser(String userId, String password);
 
}
