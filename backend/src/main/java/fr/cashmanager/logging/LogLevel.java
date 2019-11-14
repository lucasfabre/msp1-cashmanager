package fr.cashmanager.logging;

/**
 * Preferences
 */
public enum LogLevel {

    DEBUG(0),
    INFO(1),
    WARN(2),
    ERROR(3);

    private int level;
 
    /**
     * default constructor
     * @param level the level
     */
    LogLevel(int level) {
        this.level = level;
    }
 
    /**
     * get the level
     * @return the level
     */
    public int getLevel() {
        return this.level;
    }

}