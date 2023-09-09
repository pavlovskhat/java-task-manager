package main;

import database.*;
import reports.*;
import task.AdminTaskSettings;
import task.Task;
import task.TaskHelpers;
import task.TaskSettings;
import user.AdminUserSettings;
import user.User;
import user.UserSettings;

public class Main {
    private static Database database;
    private static User currentUser;
    private static User[] userArray;
    private static Task[] taskArray;
    public static String logPath;

    /**
     * Initializes global variables.
     * Creates database object.
     * Verifies valid login.
     */
    public static void main(String[] args) {
        logPath = "..\\logs\\system_log.txt";
        while (database == null) {
            String url = "jdbc:mysql://localhost/";
            database = Database.initializeDatabase(url);
        }
        Database.databaseScaffolding();
        taskArray = DataImports.importTasks();
        userArray = DataImports.importUsers();
        currentUser = login(5);
        mainMenu();
    }

    /**
     * Displays program main menu.
     * Requests choice from user to parse into
     * switch case thread.
     */
    public static void mainMenu() {

        boolean menuLoop = true;
        String menu = """
                *** MAIN MENU ***
                1) Profile settings
                2) Task settings
                3) View all tasks
                4) Admin menu
                5) Exit
                ==================
                Select option >_
                """;
        while (menuLoop) {
            String userInput = InputsLogs.stringInput(menu);
            switch (userInput) {
                case "1" -> UserSettings.profileSettingsMenu();
                case "2" -> TaskSettings.userTaskSettingsMenu();
                case "3" -> TaskHelpers.viewTasks(taskArray);
                case "4" -> {
                    if (Boolean.TRUE.equals(currentUser.getAdmin())) {
                        adminMenu();
                    } else {
                        Loggers.printWarning("!!! ERROR: UNAUTHORIZED !!!");
                    }
                }
                case "5" -> menuLoop = false;
                default -> Loggers.printWarning("!!! ERROR: INVALID CHOICE !!!");
            }
        }
        System.exit(0);
    }

    /**
     * Displays admin sub menu with admin restricted
     * program options.
     */
    public static void adminMenu() {

        String menu = """
                *** ADMIN MENU ***
                1) User settings
                2) Task settings
                3) Report menu
                4) Return to main menu
                ==================
                Enter choice >_
                """;
        while (true) {
            String choice = InputsLogs.stringInput(menu);
            switch (choice) {
                case "1" -> AdminUserSettings.userSettingsMenu();
                case "2" -> AdminTaskSettings.taskSettingsMenu();
                case "3" -> Reporting.reportMenu();
                case "4" -> {
                    return;
                }
                default -> Loggers.printWarning("!!! ERROR: INVALID CHOICE !!!");
            }
        }
    }

    /**
     * Global variable getter methods.
     */
    public static Database getDatabase() {

        return database;
    }

    public static User getCurrentUser() {

        return currentUser;
    }

    public static Task[] getTaskArray() {

        return taskArray;
    }

    /**
     * Global variable setter methods.
     */
    public static User[] getUserArray() {

        return userArray;
    }

    public static void setUserArray() {

        userArray = DataImports.importUsers();
    }

    public static void setTaskArray() {

        taskArray = DataImports.importTasks();
    }

    /**
     * Method to log users into the program, returns a true or false value based
     * on if the credentials are found in the database or not.
     * @return boolean value indicating if login was successful or not and
     *         String username in ArrayList.
     */
    public static User login(int attempt){

        User[] userArray = Main.getUserArray();
        while (attempt > 0) {
            String userName = InputsLogs.stringInput("Username >_ ");
            String passWord = InputsLogs.stringInput("Password >_ ");
            for (User user : userArray) {
                if (user.getUserName().equals(userName) && user.getPassWord().equals(passWord)) {
                    InputsLogs.print("*** LOGIN SUCCESSFUL ***");
                    return user;
                }
            }
            InputsLogs.printWarning("!!! USERNAME OR PASSWORD INCORRECT !!!");
            Loggers.printWarning("!!! " + attempt + " ATTEMPTS LEFT !!!");
            login(attempt - 1);
        }
        System.exit(1);
        return null;
    }
}