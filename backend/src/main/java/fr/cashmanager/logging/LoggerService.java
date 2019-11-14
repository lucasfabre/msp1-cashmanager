package fr.cashmanager.logging;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.cashmanager.impl.ioc.ServicesContainer;

/**
 * LoggerServce
 */
public class LoggerService {

    // we store this to use the IConfig service in the future
    @SuppressWarnings("unused")
    private ServicesContainer services;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Map<LogLevel, PrintStream> printStreamByLvl = new HashMap<LogLevel, PrintStream>();

    /**
     * default constructor
     * @param services
     */
    public LoggerService(ServicesContainer services) {
        this.services = services;
        printStreamByLvl.put(LogLevel.DEBUG, System.out);
        printStreamByLvl.put(LogLevel.INFO, System.out);
        printStreamByLvl.put(LogLevel.WARN, System.out);
        printStreamByLvl.put(LogLevel.ERROR, System.err);
    }

    /**
     * write something on the matching stream
     * @param lvl the level
     * @param name the name of the logger
     * @param msg the message
     * @param e an exception (can be null)
     */
    public synchronized void write(LogLevel lvl, String name, String msg, Exception e) {
        printStreamByLvl.get(lvl).println(formatMessage(lvl, new Date(), name, msg));
        if (e != null) {
            e.printStackTrace(printStreamByLvl.get(lvl));
        }
    }

    /**
     * format a message for printing
     */
    private String formatMessage(LogLevel lvl, Date date, String name, String msg) {
        return sdf.format(date) + " [" +  lvl.name() + "] - " + name + ": " + msg;
    }
}