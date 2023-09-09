package user;

import main.InputsLogs;
import database.DatabaseQueries;
import main.Main;

/**
 * Utility class for user profile settings.
 */
public class UserSettings {

    private UserSettings() {

        throw new IllegalStateException("Utility class");
    }

    /**
     * Sub menu to edit user's profile details & switch
     * case thread.
     */
    public static void profileSettingsMenu() {

        User currentUser = Main.getCurrentUser();
        String menu = """
                *** PROFILE SETTINGS ***
                1) Change password
                2) Return to main menu
                ========================
                Select option >_
                """;
        while (true) {
            currentUser.userToString();
            String choice = InputsLogs.stringInput(menu);
            switch (choice) {
                case "1" -> changePassword();
                case "2" -> {
                    return;
                }
                default -> InputsLogs.print(
                        "!!! ERROR: INVALID CHOICE !!!"
                );
            }
        }
    }

    /**
     * Updates the user's password.
     * Verifies that it is unique from current password.
     * Verifies by asking user to enter password twice.
     * Sends update query to users table.
     * Updates the User object password attribute.
     */
    public static void changePassword() {

        User currentUser = Main.getCurrentUser();
        String newPassword = UserHelpers.getNewPassword(currentUser);
        int userId = currentUser.getUserId();
        String query = String.format(
                "UPDATE users SET password = '%s' WHERE id = %d",
                newPassword,
                userId
        );
        DatabaseQueries.executeQuery(query);
        currentUser.setPassWord(newPassword);
    }
}
