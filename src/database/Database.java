package database;

import main.InputsLogs;
import main.Main;

import java.sql.Connection;
import java.sql.*;
import java.sql.SQLException;

/**
 * Database class creates connection
 * with SQL database task_manager_db.
 */
public class Database {

    /**
     * Database class attributes.
     */
    Connection connection;
    Statement statement;

    /**
     * Database class constructor.
     * @param connection: SQL connection.
     * @param statement: SQL statement object.
     */
    public Database(
            Connection connection,
            Statement statement
    ) {

        this.connection = connection;
        this.statement = statement;
    }

    /**
     * Database class getter methods.
     */
    public ResultSet getResultSet(String query) throws SQLException {

        return statement.executeQuery(query);
    }

    /**
     * Creates Database class object with valid SQL connection
     * and Statement object to run queries.
     * Object is listed Global variable in Main class.
     * Sets connect timeout counter to 5.
     * @return SQL url path String.
     */
    public static Database initializeDatabase(String url) {

        try {
            Connection connection = Database.connectServer(url, 5);
            Statement statement = Database.createStatement(connection);
            return new Database(
                    connection,
                    statement
            );
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return null;
    }

    /**
     * Return Statement object.
     * @param connection: Valid mysql connection.
     * @return Statement object.
     */
    public static Statement createStatement(Connection connection) throws SQLException {

        return connection.createStatement();
    }

    /**
     * Attempts to connect with SQL server located at
     * given url and entered credentials.
     * System exits in error status due to exception
     * after given timeout attempt counter = zero.
     * @return SQL server connection object.
     */
    public static Connection connectServer(String url, int attempt) {
        try {
            InputsLogs.print("*** SQL LOGIN ***");
            String sqlUser = InputsLogs.stringInput("\nUsername >_ ");
            String password = InputsLogs.stringInput("Password >_ ");
            return DriverManager.getConnection(
                    url,
                    sqlUser,
                    password
            );
        } catch (SQLException error) {
            InputsLogs.addLog(Main.logPath, String.valueOf(error));
            InputsLogs.print("!!! FATAL SERVER ERROR SHUTTING DOWN !!!");
            if (attempt == 0) {
                System.exit(-1);
            }
            InputsLogs.print("!!! " + attempt + " ATTEMPTS REMAINING !!!");
        }
        return connectServer(url, attempt - 1);
    }

    /**
     * Sets up the default database scaffolding including
     * > task_manager_db database
     * > users table
     * > tasks table
     * > default username: admin password: adm1n
     * Both tables auto increment unique keys.
     * Default 'admin' user has admin rights enabled.
     */
    public static void databaseScaffolding() {
        String db = """
                CREATE DATABASE IF NOT EXISTS task_manager_db;
                """;
        String useDb = """
                USE task_manager_db;
                """;
        String userTable = """
                CREATE TABLE IF NOT EXISTS users (
                id int AUTO_INCREMENT,
                username varchar(25),
                password varchar(25),
                admin tinyint(1),
                PRIMARY KEY (id)
                );
                """;
        String userIdStart = """
                ALTER TABLE users AUTO_INCREMENT = 100
                """;
        String taskIdStart = """
                ALTER TABLE tasks AUTO_INCREMENT = 500
                """;
        String tasksTable = """
                CREATE TABLE IF NOT EXISTS tasks (
                id int AUTO_INCREMENT,
                assigned varchar(15),
                task varchar(15),
                description varchar(100),
                start_date date,
                due_date date,
                status varchar(10),
                PRIMARY KEY (id)
                );
                """;
        String adminUser = """
                INSERT IGNORE INTO users (username, password, admin)
                VALUES (
                'admin',
                'adm1n',
                1
                );
                """;
        DatabaseQueries.executeQuery(
                db,
                "*** DATABASE INITIALIZED ***"
        );
        DatabaseQueries.executeQuery(
                useDb,
                "*** DATABASE INITIALIZED ***"
        );
        DatabaseQueries.executeQuery(
                userTable,
                "***  ***"
                );
        DatabaseQueries.executeQuery(userIdStart);
        DatabaseQueries.executeQuery(tasksTable);
        DatabaseQueries.executeQuery(taskIdStart);
        DatabaseQueries.executeQuery(adminUser);
    }
}
