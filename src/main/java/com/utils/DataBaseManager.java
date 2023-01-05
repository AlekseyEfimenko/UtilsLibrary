package com.utils;

import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for managing queries to DataBase
 */
public class DataBaseManager {
    private static final Logger LOGGER = Logger.getLogger(DataBaseManager.class.getName());
    private static final FileManager fileManager = FileManager.getInstance();
    private static final String ERROR_MSG = "Can't get access to database or incorrect SQL query";
    private static final String ERROR_FORMAT = "%1$s%n%2$s";
    private static DataBaseManager instance;
    private static Connection connection;
    private Statement statement;
    private ResultSet rs = null;

    private DataBaseManager() {
        setConnection();
        try {
            statement = getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }

    /**
     * Close the connection
     */
    public static void closeConnection() {
        LOGGER.info("Close connection");
        try {
            connection.close();
            LOGGER.info("Connection is closed successfully");
        } catch (SQLException ex) {
            LOGGER.error(String.format("Can't close connection%n%1$s",  ex.getMessage()));
        }
    }

    /**
     * Select data from all columns of the table
     * @param table The table to get data
     * @return ResultSet, contains of all the data in this table
     */
    public ResultSet selectQuery(String table) {
        try {
            LOGGER.info(String.format("Getting data from database with next query: \"SELECT * FROM %1$s\"", table));
            rs = statement.executeQuery(String.format(fileManager.getSQLQuery("sql_query/select_all.sql"), table));
        } catch (SQLException ex) {
            LOGGER.error(String.format("%1$s: \"SELECT * FROM %2$s\"%n%3$s", ERROR_MSG, table, ex.getMessage()));
        }
        return rs;
    }

    /**
     * Select data from given column of the table
     * @param columnName The column to get data
     * @param table The table to get data
     */
    public void selectQuery(String columnName, String table) {
        try {
            LOGGER.info(String.format("Getting data from database with next query: \"SELECT %1$s FROM %2$s\"", columnName, table));
            rs = statement.executeQuery(String.format(fileManager.getSQLQuery("sql_query/select_without_conditions.sql"), columnName, table));
        } catch (SQLException ex) {
            LOGGER.error(String.format("%1$s: \"SELECT %2$s FROM %3$s\"%n%4$s", ERROR_MSG, columnName, table, ex.getMessage()));
        }
    }

    /**
     * Select specific data from given table ang given column
     * @param columnName The column to get data
     * @param table The table to get data
     * @param conditions Conditions to be applied for query
     * @return ResultSet, contains of all the data due to the specified request
     */
    public ResultSet selectQuery(String columnName, String table, String conditions) {
        try {
            LOGGER.info(String.format("Getting data from database with next query: \"SELECT %1$s FROM %2$s WHERE %3$s\"", columnName, table, conditions));
            rs = statement.executeQuery(String.format(fileManager.getSQLQuery("sql_query/select_with_conditions.sql"), columnName, table, conditions));
        } catch (SQLException ex) {
            LOGGER.error(String.format("%1$s: \"SELECT %2$s FROM %3$s WHERE %4$s\"%n%5$s", ERROR_MSG, columnName, table, conditions, ex.getMessage()));
        }
        return rs;
    }

    /**
     * Putting data into database
     * @param sqlQuery The query to be executed
     */
    public void insertQuery(String sqlQuery) {
        try {
            LOGGER.info(String.format("Putting data into database with next query: %1$s", sqlQuery));
            statement.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
    }

    /**
     * Update data in database
     * @param sqlQuery The query to be executed
     */
    public void updateRecord(String sqlQuery) {
        try {
            LOGGER.info(String.format("Updating data in database with next query: %1$s", sqlQuery));
            statement.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
    }

    /**
     * Delete data from database
     * @param sqlQuery The query to be executed
     */
    public void deleteRecord(String sqlQuery) {
        try {
            LOGGER.info(String.format("Deleting data from database with next query: %1$s", sqlQuery));
            statement.executeUpdate(sqlQuery);
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
    }

    /**
     * Get max value in the given column and given table of database
     * @param columnName The column to get max value
     * @param table The table to get data
     * @return Max value
     */
    public Object getMax(String columnName, String table) {
        Object max = null;
        selectQuery(String.format("max(%1$s)", columnName), table);
        try {
            if (rs.first()) {
                max = rs.getObject(String.format("max(%1$s)", columnName));
            }
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
        return max;
    }

    /**
     * Get first value of the sql query
     * @param columnName The column to get data
     * @param table The table to get data
     * @param conditions Conditions to be applied for query
     * @return First value
     */
    public Object getFirst(String columnName, String table, String conditions) {
        Object first = null;
        selectQuery(columnName, table, conditions);
        try {
            if (rs.first()) {
                first = rs.getObject(columnName);
            }
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
        return first;
    }

    /**
     * Check if the table is empty
     * @param table The table to check
     * @return True - if the table is empty, False - if not
     */
    public boolean isEmpty(String table) {
        boolean cond = false;
        try {
            cond = !selectQuery(table).next();
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
        return cond;
    }

    /**
     * Check if the specified query return empty ResultSet
     * @param columnName The column to get data
     * @param table The table to get data
     * @param conditions Conditions to be applied for query
     * @return True - if the query is empty, False - if not
     */
    public boolean isEmpty(String  columnName, String table, String conditions) {
        boolean cond = false;
        try {
            cond = !selectQuery(columnName, table, conditions).next();
        } catch (SQLException ex) {
            LOGGER.error(String.format(ERROR_FORMAT, ERROR_MSG, ex.getMessage()));
        }
        return cond;
    }

    /**
     * Select top n rows from query
     * @param columnName The column to get data
     * @param table The table to get data
     * @param numRows number of top rows to select
     */
    public void selectTopRows(String columnName, String table, int numRows) {
        selectQuery(columnName, String.format("%1$s LIMIT %2$s", table, numRows));
    }

    /**
     * Set connection to database
     */
    private static void setConnection() {
        if (connection == null) {
            String connectionString = fileManager.getProperties("connectionPath");
            String login = fileManager.getProperties("user");
            String password = fileManager.getProperties("password");
            try {
                LOGGER.info(String.format("Set connection to %1$s", connectionString));
                connection = DriverManager.getConnection(connectionString, login, password);

            } catch (SQLException ex) {
                LOGGER.error(String.format("Can't get connection. Incorrect URL%n%1$s", ex.getMessage()));
            }
        }
        LOGGER.info("Connection is created successfully");
    }
}
