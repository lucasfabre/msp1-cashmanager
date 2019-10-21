package fr.cashmanager.testutils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * TestHelper
 */
public class TestHelper {

    /**
     * Copy resource inside the classpath to a desired path
     * @param res the resource URI
     * @param dest the destination path
     * @param c a class (usualy this.class)
     * @throws IOException
     */
    public static void copyResource(String res, String dest, Class<?> c) throws IOException {
        InputStream src = c.getResourceAsStream(res);
        Files.copy(src, Paths.get(dest), StandardCopyOption.REPLACE_EXISTING);
    }
}