package database;

import main.InputsLogs;
import main.Main;
import java.sql.*;

/**
 * DatabaseQueries class deals with
 * SQL queries.
 */
public class DatabaseQueries {

    private DatabaseQueries() {

        throw new IllegalStateException("Utility class");
    }

    /**
     * Attempts to execute String query to SQL
     * database.
     * Displays confirmation given String confirmation
     * message if query is successful.
     * @param query: String SQL query.
     * @param confirmation: String confirmation message.
     */
    public static void executeQuery(String query, String confirmation) {

        try {
            Main.getDatabase().statement.executeUpdate(query);
        } catch (SQLException error) {
            error.printStackTrace();
        }
        InputsLogs.print(confirmation);
    }

    /**
     * Returns the number of rows found in table to calculate
     * array length. Returns 0 if table not found.
     * @param table: String name of SQL table.
     * @return: int value indicating no of table rows.
     */
    public static int tableLength(String table) {

        int numberOfRows = 0;
        Database database = Main.getDatabase();
        try (ResultSet resultSet = database.getResultSet(
                "SELECT COUNT(*) FROM " + table)
        ) {
            resultSet.next();
            numberOfRows = resultSet.getInt(1);
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return numberOfRows;
    }
}
