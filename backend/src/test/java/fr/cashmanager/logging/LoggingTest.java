package fr.cashmanager.logging;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.cashmanager.impl.ioc.ServicesContainer;

/**
 * LoggingTest
 */
public class LoggingTest {

    private ServicesContainer services = new ServicesContainer();

    // Output stream overide
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @Test
    public void test() {
        Logger log = services.get(LoggerFactory.class).getLogger("LoggingTest");
        log.info("Test info");
        assertTrue(outContent.toString().endsWith("[INFO] - LoggingTest: Test info\n"));
        log.debug("Test debug");
        assertTrue(outContent.toString().endsWith("[DEBUG] - LoggingTest: Test debug\n"));
        log.warn("Test warn");
        assertTrue(outContent.toString().endsWith("[WARN] - LoggingTest: Test warn\n"));
        log.error("Test error");
        assertTrue(errContent.toString().endsWith("[ERROR] - LoggingTest: Test error\n"));
        log.error("test stacktrace", new Exception("testErrMsg"));
        assertTrue(errContent.toString().contains("[ERROR] - LoggingTest: test stacktrace\n"));
        assertTrue(errContent.toString().contains("java.lang.Exception: testErrMsg\n"));
    }

    @Before
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        LoggerService loggerService = new LoggerService(services);
        services.register(LoggerService.class, loggerService);
        LoggerFactory loggerFactory = new LoggerFactory(services);
        services.register(LoggerFactory.class, loggerFactory);
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
}