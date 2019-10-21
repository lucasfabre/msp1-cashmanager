package fr.cashmanager.user;

/**
 * User
 * Object representing an cashmanager service user (the application)
 */
public class User {

    /**
     * the id of the user
     */
    private String id;

    /**
     * the password of the user
     */
    private String password;

    /**
     * access to the password of the user
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * set the password of the user
     * @param password the new password
     * @return the user
     */
    public User withPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * access the id of the user
     * @return id of the user
     */
    public String getId() {
        return id;
    }

    /**
     * set the id of the user
     * @param id new id of the user
     * @return the user
     */
    public User withId(String id) {
        this.id = id;
        return this;
    }
}
