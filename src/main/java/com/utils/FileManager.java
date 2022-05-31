package com.utils;

import aquality.selenium.core.logging.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

/**
 * Class for working with files and streams
 */
public class FileManager {
    private static FileManager instance;
    private final Properties properties = new Properties();
    private final Logger logger = Logger.getInstance();

    private FileManager() {}

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    /**
     * Convert content of some file to String format
     * @param file File, to convert to String
     * @return String of file
     */
    public String printFile(String file) {
        StringBuilder st = new StringBuilder();
        try (Scanner in = new Scanner(new File(file))) {
            while (in.hasNext()) {
                st.append(in.nextLine()).append("\n");
            }
        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage());
        }
        return st.toString();
    }

    /**
     * Creates a new print stream
     * @param name Name of the file to use as the destination of this print stream
     * @return New print stream
     */
    public PrintStream createPrintStream(String name) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(name);
        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage());
        }
        return ps;
    }

    public String getSQLQuery(String src) {
        StringBuilder query = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(getResourcePath(src)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                query.append(line);
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
        return query.toString();
    }

    /**
     * Get path in String format to resources files or directories
     * @param file The file or directory to find path to
     * @return path to file or directory
     */
    public String getResourcePath(String file) {
        String path = null;
        try {
            path = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(file)).toURI()).getPath();
        } catch (URISyntaxException ex) {
            logger.error(ex.getMessage());
        }
        return path;
    }

    /**
     * Get Root path of project
     * @return Path in String format
     */
    public String getRootPath() {
        String path = "";
        try {
            path = Paths.get(Objects.requireNonNull(getClass().getResource("/")).toURI()).getParent().toString();
        } catch (URISyntaxException ex) {
            logger.error(ex.getMessage());
        }
        return path;
    }

    /**
     * Get value from .properties file
     * @param key The key to get value
     * @return The value of .properties file in String format
     */
    public String getProperties(String key) {
        loadFile("config.properties");
        return properties.getProperty(key);
    }

    /**
     * Load properties from custom file to Properties object
     * @param src File, contains properties to load
     */
    private void loadFile(String src) {
        try (FileInputStream fileInputStream = new FileInputStream(getResourcePath(src))) {
            properties.load(fileInputStream);
        } catch (IOException ex) {
            logger.error(String.format("File %s is not found%n%s", src, ex.getMessage()));
        }
    }
}
