package user;

import main.InputsLogs;
import main.Main;
import task.Task;

/**
 * User settings package helper class.
 */
public class UserHelpers {

    private UserHelpers() {

        throw new IllegalStateException("Utility class");
    }

    /**
     * Requests new user username.
     * Checks if username is unique.
     * @return new username as string value.
     */
    public static String verifyUniqueUsername() {

        while (true) {
            String newUsername = InputsLogs.stringInput("New username >_ ");
            if (checkForUsername(newUsername)) {
                Loggers.printWarning(String.format(
                        "!!! ERROR: USERNAME %s ALREADY EXISTS !!!",
                        newUsername
                ));
                continue;
            }
            return newUsername;
        }
    }

    /**
     * Returns exiting user username.
     * Validates if user is registered.
     * @return existing username as string value.
     */
    public static String verifyExistingUsername() {

        while (true) {
            String username = InputsLogs.stringInput("Username >_ ");
            if (!checkForUsername(username)) {
                Loggers.printWarning(String.format(
                        "!!! ERROR: USERNAME %s NOT FOUND !!!",
                        username
                ));
                continue;
            }
            return username;
        }
    }

    /**
     * Checks if given username is unique.
     * Checks the given username against User class
     * object array.
     * @param userName: Given username String.
     * @return boolean indicating if username is unique.
     */
    public static boolean checkForUsername(String userName) {

        User[] userArray = Main.getUserArray();
        for (User user : userArray) {
            if (user.getUserName().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Request user to enter new password.
     * Verifies the password by requesting the user
     * to repeat the password.
     * @return new password as string value.
     */
    public static String verifyPassword() {

        while (true) {
            String newPassWord = InputsLogs.stringInput("Password >_ ");
            String passWordVerify = InputsLogs.stringInput("Re-enter password >_ ");
            if (newPassWord.equals(passWordVerify)) {
                return newPassWord;
            }
            Loggers.printWarning("!!! ERROR: PASSWORDS DID NOT MATCH !!!");
        }
    }

    /**
     * Check if given user is assigned to any pending tasks.
     * @param userTaskArray: Array of users Task objects.
     * @return boolean indicating if pending tasks are found.
     */
    public static boolean checkPendingTasks(Task[] userTaskArray) {

        for (Task task : userTaskArray) {
            if (!task.getStatus()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns User object from User object array
     * associated with given user id.
     * @param userId: unique user id integer.
     * @return User class object.
     */
    public static User getUser(int userId) {

        User[] userArray = Main.getUserArray();
        for (User user : userArray) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    /**
     * Verifies if given user id integer is found in
     * User object array.
     * @param userId: unique user id integer.
     * @return boolean indicating if id is found.
     */
    public static boolean verifyUserId(int userId) {

        User[] userArray = Main.getUserArray();
        for (User user : userArray) {
            if (userId == user.getUserId()) {
                return true;
            }
        }
        Loggers.printWarning("*** INVALID USER ID ***");
        return false;
    }

    /**
     * Returns updated password that does not match the
     * current password.
     * @param user: User class object.
     * @return new password as string value.
     */
    public static String getNewPassword(User user) {

        String currentPassword = user.getPassWord();
        while (true) {
            String newPassword = verifyPassword();
            if (newPassword.equals(currentPassword)) {
                Loggers.printWarning(
                        "*** ERROR: CANNOT USE CURRENT PASSWORD ***"
                );
                continue;
            }
            return newPassword;
        }
    }
}
