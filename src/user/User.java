package user;

import main.InputsLogs;
import main.Main;
import task.Task;

/**
 * User class deals with the records
 * from the users table.
 */
public class User {

    /**
     * User class attributes.
     */
    int userId;
    String userName;
    String passWord;
    int taskCount = 0;
    Boolean admin;

    /**
     * User class object constructor.
     * @param userId: users table key.
     * @param userName: unique username.
     * @param passWord: user password.
     * @param admin: admin rights.
     */
    public User(
            int userId,
            String userName,
            String passWord,
            Boolean admin
    ) {

        this.userId = userId;
        this.userName = userName;
        this.passWord = passWord;
        this.admin = admin;
    }

    /**
     * Returns the total Task class objects related to the given
     * User class object.
     * @param user User class object.
     */
    public static void getTaskCount(User user) {

        Task[] taskArray = Main.getTaskArray();
        int taskCount = 0;
        for (Task task : taskArray) {
            if (user.getUserName().equals(task.getAssigned()) &&
                    !task.getStatus()) {
                taskCount ++;
            }
        }
        user.setTaskCount(taskCount);
    }

    /**
     * User class getter methods.
     * @return relevant class attribute.
     */
    public int getUserId() {

        return userId;
    }

    public String getUserName() {

        return userName;
    }

    public String getPassWord() {

        return passWord;
    }

    public int getTaskCount() {

        return taskCount;
    }

    public Boolean getAdmin() {

        return admin;
    }

    /**
     * User class setter methods.
     */
    public void setTaskCount(int newTaskCount) {

        this.taskCount = newTaskCount;
    }

    public void setUserName(String newUsername) {

        this.userName = newUsername;
    }

    public void setPassWord(String newPassword) {

        this.passWord = newPassword;
    }

    /**
     * Displays User class object information to
     * console.
     */
    public void userToString() {

        String banner = "=".repeat(40);
        String displayString = banner;
        displayString += "\n~ USER ID > " + this.userId;
        displayString += "\n~ USERNAME > " + this.userName;
        displayString += "\n~ ADMIN USER > " + this.admin;
        displayString += "\n" + banner;
        InputsLogs.print(displayString);
    }
}
