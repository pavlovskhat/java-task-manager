package task;

import main.InputsLogs;
import database.DatabaseQueries;
import main.Main;
import user.User;

import java.util.Objects;

/**
 * Utility class that allow the user to view and
 * edit their tasks.
 */
public class TaskSettings {

    private TaskSettings() {

        throw new IllegalStateException("Utility class");
    }

    /**
     * Displays all tasks assigned to user and requests
     * user to enter task id to view specific task.
     */
    public static void userTaskSettingsMenu() {

        int taskId;
        User currentUser = Main.getCurrentUser();
        Task[] userTaskArray = TaskHelpers.getUserTasks(currentUser);
        if (userTaskArray.length == 0) {
            Loggers.printInfo("*** NO TASKS ASSIGNED ***");
            return;
        }
        while (true) {
            TaskHelpers.viewTasks(userTaskArray);
            taskId = InputsLogs.intInput("""
                    *** TASK SETTINGS ***
                    Enter task ID to mark as complete >_
                    """);
            if (TaskHelpers.verifyTaskId(taskId)) {
                break;
            }
            Loggers.printWarning("!!! ERROR: INVALID TASK ID !!!");
        }
        taskOptions(
                Objects.requireNonNull(
                        TaskHelpers.getTask(taskId)), taskId);
    }

    /**
     * Displays specific task and request confirmation to
     * set task status to 'finalized'.
     * Sends update query to tasks table.
     * Updates the Task object status attribute.
     *
     * @param task:   Task class object.
     * @param taskId: Task object unique ID integer.
     */
    public static void taskOptions(Task task, int taskId) {

        User currentUser = Main.getCurrentUser();
        String query = String.format(
                "UPDATE tasks SET status = 'finalized' WHERE id = %d",
                taskId
        );
        task.taskToString();
        String confirmation = InputsLogs.stringInput(
                "Mark this task as completed? (Y/N) >_ "
        ).toLowerCase();
        if (confirmation.equals("y")) {
            currentUser.setTaskCount(currentUser.getTaskCount() - 1);
            DatabaseQueries.executeQuery(query);
            task.setTaskStatus();
        }
    }
}

