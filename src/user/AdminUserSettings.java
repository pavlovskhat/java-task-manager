package user;

import main.InputsLogs;
import database.DatabaseQueries;
import main.Main;
import task.Task;
import task.TaskHelpers;

/**
 * Utility class for user settings restricted
 * to admin users.
 */
public class AdminUserSettings {

    private AdminUserSettings() {

        throw new IllegalStateException("Utility class");
    }

    /**
     * User settings main menu & switch case thread.
     */
    public static void userSettingsMenu() {

        String menu = """
                *** USER SETTINGS ***
                1) Register new user
                2) User directory
                3) Return to main menu
                =====================
                Select choice >_
                """;
        while (true) {
            String choice = InputsLogs.stringInput(menu);
            switch (choice) {
                case "1" -> registerUser();
                case "2" -> userOptions(userDirectory());
                case "3" -> {
                    return;
                }
                default -> Loggers.printWarning("*** INVALID CHOICE ***");
            }
        }
    }

    /**
     * Creates new user record in users table.
     * Verifies username & password.
     * Re-imports users table data as User class objects to update
     * global userArray variable.
     */
    public static void registerUser() {

        String newUsername = UserHelpers.verifyUniqueUsername();
        String newPassword = UserHelpers.verifyPassword();
        boolean admin = false;
        String query = String.format(
                "INSERT INTO users (username, password, admin) VALUES ('%s', '%s', %b)",
                newUsername,
                newPassword,
                admin
        );
        DatabaseQueries.executeQuery(query);
        Main.setUserArray();
    }

    /**
     * Displays user directory.
     * Instructs user to enter user id.
     * Verifies user id.
     * Returns User object associated with id.
     * @return User class object.
     */
    public static User userDirectory() {

        while (true) {
            User[] userArray = Main.getUserArray();
            for (User user : userArray) {
                user.userToString();
            }
            int userId = InputsLogs.intInput("User ID >_ ");
            if (UserHelpers.verifyUserId(userId)) {
                return UserHelpers.getUser(userId);
            }
        }
    }

    /**
     * Menu with user record update options & switch case
     * thread.
     * Compiles array of user related tasks.
     * @param user: User class object.
     */
    public static void userOptions(User user) {

        String menu = """
                *** OPTIONS ***
                1) Set admin rights
                2) View assigned tasks
                3) Delete user
                4) Return to user settings
                =========================
                Select Option >_
                """;
        Task[] userTaskArray = TaskHelpers.getUserTasks(user);
        while (true) {
            user.userToString();
            String choice = InputsLogs.stringInput(menu);
            switch (choice) {
                case "1" -> setAdminRights(
                        user.getAdmin(),
                        user.getUserId()
                );
                case "2" -> TaskHelpers.viewTasks(userTaskArray);
                case "3" -> {
                    if (UserHelpers.checkPendingTasks(userTaskArray)) {
                        Loggers.printWarning(
                                "!!! ERROR: REASSIGN PENDING TASKS !!!"
                        );
                    } else {
                        deleteUser(user);
                    }
                }
                case "4" -> {
                    return;
                }
                default -> Loggers.printWarning("!!! ERROR: INVALID INPUT !!!");
            }
        }
    }

    /**
     * Sets or revokes user admin rights.
     * Re-imports users table data as User class objects to update
     * global userArray variable.
     * @param adminRights: boolean indicating admin status.
     * @param userId: unique user id integer.
     */
    public static void setAdminRights(boolean adminRights, int userId) {

        String confirmation;
        String setQuery = String.format("UPDATE users SET admin = true WHERE id = %d", userId);
        String revokeQuery = String.format("UPDATE users SET admin = false WHERE id = %d", userId);
        if (adminRights) {
            confirmation = InputsLogs.stringInput(
                    "User set as admin, remove admin rights? (Y/N) >_ ").toLowerCase();
            if (confirmation.equals("y")) {
                DatabaseQueries.executeQuery(revokeQuery);
                Main.setUserArray();
            }
        } else {
            confirmation = InputsLogs.stringInput(
                    "Set this user as admin user? (Y/N) >_ ").toLowerCase();
            if (confirmation.equals("y")) {
                DatabaseQueries.executeQuery(setQuery);
                Main.setUserArray();
            }
        }
    }

    /**
     * Deletes user record from users table.
     * @param user: User class object.
     */
    public static void deleteUser(User user) {

        String confirmation;
        String query = String.format("DELETE FROM users WHERE id = %d", user.getUserId());
        confirmation = InputsLogs.stringInput(
                String.format("Delete user %s? (Y/N) >_", user.getUserName()));
        if (confirmation.equals("y")) {
            DatabaseQueries.executeQuery(query);
            Main.setUserArray();
            userSettingsMenu();
        }
    }
}
