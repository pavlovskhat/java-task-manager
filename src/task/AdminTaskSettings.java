package task;

import main.InputsLogs;
import database.DatabaseQueries;
import main.Main;
import user.UserHelpers;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Utility class allowing admin users to edit
 * task details.
 */
public class AdminTaskSettings {

    private AdminTaskSettings() {

        throw new IllegalStateException("Utility class");
    }

    /**
     * Displays task settings menu and requests
     * choice from user to parse into switch case
     * thread.
     */
    public static void taskSettingsMenu() {

        String menu = """
                *** TASK SETTINGS ***
                1) Add new task
                2) Task directory
                3) Return to main menu
                =====================
                Select choice >_
                """;
        while (true) {
            String choice = InputsLogs.stringInput(menu);
            switch (choice) {
                case "1" -> addTask();
                case "2" -> taskOption(taskDirectory());
                case "3" -> {
                    return;
                }
                default -> Loggers.printWarning("!!! INVALID CHOICE !!!");
            }
        }
    }

    /**
     * Creates new task entry.
     * Verifies that the assigned user is valid.
     * Sends insert query to users table.
     * Re-imports 'tasks' table data as Task class objects to update
     * global userArray variable.
     */
    public static void addTask() {

        String assigned = UserHelpers.verifyExistingUsername();
        String taskName = InputsLogs.stringInput("Task heading >_ ");
        String taskDescription = InputsLogs.stringInput("Task Description >_ ");
        LocalDate startDate = LocalDate.now();
        LocalDate dueDate = InputsLogs.getDate("Due Date (FORMAT: yyyy-mm-dd) >_ ");
        boolean status = false;
        String query = String.format("""
                INSERT INTO tasks (assigned, task, description, start_date, due_date, status)
                VALUES ('%s', '%s', '%s', '%s', '%s', '%b')
                """,
                assigned,
                taskName,
                taskDescription,
                Date.valueOf(startDate),
                Date.valueOf(dueDate),
                status
        );
        DatabaseQueries.executeQuery(query);
        Main.setTaskArray();
    }

    /**
     * Displays task directory.
     * Instructs user to enter task id.
     * Verifies task id.
     * Returns Task object associated with id.
     * @return Task class object.
     */
    public static Task taskDirectory() {

        while (true) {
            Task[] taskArray = Main.getTaskArray();
            TaskHelpers.viewTasks(taskArray);
            int taskId = InputsLogs.intInput("Task ID >_");
            if (TaskHelpers.verifyTaskId(taskId)) {
                return TaskHelpers.getTask(taskId);
            }
        }
    }

    /**
     * Displays task update options.
     * Requests choice from user to parse into
     * switch case thread.
     * @param task: Task class object.
     */
    public static void taskOption(Task task) {

        if (task.getStatus()) {
            Loggers.printWarning("!!! ERROR: TASK FINALIZED !!!");
            return;
        }
        String menu = """
                *** OPTIONS ***
                1) Reassign task
                2) Edit task name
                3) Edit task description
                4) Edit due date
                5) Finalize task
                6) Return to task settings
                =========================
                Select Option >_
                """;
        while (true) {
            task.taskToString();
            String choice = InputsLogs.stringInput(menu);
            switch (choice) {
                case "1" -> updateAssigned(task);
                case "2" -> updateTaskName(task);
                case "3" -> updateTaskDescription(task);
                case "4" -> updateTaskDueDate(task);
                case "5" -> finalizeTask(task);
                case "6" -> {
                    return;
                }
                default -> Loggers.printWarning("!!! ERROR: INVALID CHOICE !!!");
            }
        }
    }

    /**
     * Sends query to update tasks table assigned field.
     * Updates Task object assigned attribute.
     * @param task: Task class object.
     */
    public static void updateAssigned(Task task) {

        String newAssigned = InputsLogs.stringInput("New assigned user >_ ");
        String query = String.format(
                "UPDATE tasks SET assigned='%s' WHERE id=%d",
                newAssigned,
                task.getTaskId()
        );
        DatabaseQueries.executeQuery(query);
        task.setAssigned(newAssigned);
    }

    /**
     * Sends query to update tasks table task field.
     * Updates Task object task name attribute.
     * @param task: Task class object.
     */
    public static void updateTaskName(Task task) {

        String newTaskName = InputsLogs.stringInput("New task name >_");
        String query = String.format(
                "UPDATE tasks SET task='%s' WHERE id=%d",
                newTaskName,
                task.getTaskId()
        );
        DatabaseQueries.executeQuery(query);
        task.setTaskName(newTaskName);
    }

    /**
     * Sends query to update tasks table description field.
     * Updates Task object description attribute.
     * @param task: Task class object.
     */
    public static void updateTaskDescription(Task task) {

        String newDescription = InputsLogs.stringInput("New description >_");
        String query = String.format(
                "UPDATE tasks SET description='%s' WHERE id=%d",
                newDescription,
                task.getTaskId()
        );
        DatabaseQueries.executeQuery(query);
        task.setTaskDescription(newDescription);
    }

    /**
     * Sends query to update tasks table due date field.
     * Updates Task object due date attribute.
     * @param task: Task class object.
     */
    public static void updateTaskDueDate(Task task) {

        LocalDate newDueDate = InputsLogs.getDate("New due date (FORMAT: yyyy-mm-dd) >_ ");
        String query = String.format(
                "UPDATE tasks SET due_date='%s' WHERE id=%d",
                newDueDate,
                task.getTaskId()
        );
        DatabaseQueries.executeQuery(query);
        task.setDueDate(newDueDate);
    }

    /**
     * Sends query to update tasks table status field.
     * Updates Task object status attribute.
     * @param task: Task class object.
     */
    public static void finalizeTask(Task task) {

        String query = String.format(
                "UPDATE tasks SET status='finalized' WHERE id=%d",
                task.getTaskId()
        );
        DatabaseQueries.executeQuery(query);
        task.setTaskStatus();
    }
}
