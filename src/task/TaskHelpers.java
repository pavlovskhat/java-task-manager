package task;

import main.Main;
import user.User;

/**
 * Task settings package helper class.
 */
public class TaskHelpers {

    private TaskHelpers() {

        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns an array of Task objects assigned to
     * the user.
     * @param user: User class object.
     * @return Array of Task class objects.
     */
    public static Task[] getUserTasks(User user) {

        int taskCount = user.getTaskCount();
        int index = 0;
        Task[] userTaskArray = new Task[taskCount];
        Task[] taskArray = Main.getTaskArray();
        for (Task task : taskArray) {
            if (task.getAssigned().equals(user.getUserName()) &&
            !task.getStatus()) {
                userTaskArray[index] = task;
                index ++;
            }
        }
        return userTaskArray;
    }

    /**
     * Verifies if given task id integer is found in
     * Task object array.
     * @param taskId: unique task id integer.
     * @return boolean indicating if id is found.
     */
    public static boolean verifyTaskId(int taskId) {

        Task[] taskArray = Main.getTaskArray();
        for (Task task : taskArray) {
            if (taskId == task.getTaskId()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Uses the Task object toString method to display
     * all the tasks in the given Task array.
     * @param taskArray: Array of Task class objects.
     */
    public static void viewTasks(Task[] taskArray) {

        for (Task task : taskArray) {
            task.taskToString();
        }
    }

    /**
     * Returns User object from User object array
     * associated with given user id.
     * @param taskId: unique user id integer.
     * @return User class object.
     */
    public static Task getTask(int taskId) {

        Task[] taskArray = Main.getTaskArray();
        for (Task task : taskArray) {
            if (task.getTaskId() == taskId) {
                return task;
            }
        }
        return null;
    }
}
