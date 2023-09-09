package database;

import main.Main;
import task.Task;
import user.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * DataImports class imports to users
 * and tasks table information as class
 * object arrays.
 */
public class DataImports {

    private DataImports() {

        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns an array compiled of objects populated with the user information
     * from the users table: id, username & password.
     * @return array containing user objects from the User class.
     */
    public static User[] importUsers() {

        Database database = Main.getDatabase();
        int rowCount = DatabaseQueries.tableLength("users");
        User[] userArray;
        userArray = new User[rowCount];
        int counter = 0;
        try (
            ResultSet resultSet = database.getResultSet("SELECT * FROM users")) {
            while (resultSet.next()) {
                User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("username"),
                    resultSet.getString("password"),
                    resultSet.getBoolean("admin")
                );
                User.getTaskCount(user);
                userArray[counter] = user;
                counter ++;
            }
            return userArray;
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return new User[0];
    }

    /**
     * Returns an array of Task class objects populated with attributes
     * from the tasks table.
     * @return Array of Task class objects.
     */
    public static Task[] importTasks() {

        Database database = Main.getDatabase();
        int rowCount = DatabaseQueries.tableLength("tasks");
        Task[] taskArray;
        taskArray = new Task[rowCount];
        int counter = 0;
        try (
            ResultSet resultSet = database.getResultSet("SELECT * FROM tasks")) {
            while (resultSet.next()) {
                String stringStatus = resultSet.getString(7);
                boolean status;
                status = stringStatus.equals("finalized");
                LocalDate startDate = resultSet.getDate(5).toLocalDate();
                LocalDate dueDate = resultSet.getDate(6).toLocalDate();
                Task task = new Task(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        startDate,
                        dueDate,
                        status
                );
                taskArray[counter] = task;
                counter += 1;
            }
            return taskArray;
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return new Task[0];
    }
}
