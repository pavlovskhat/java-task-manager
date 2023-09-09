package task;

import java.time.LocalDate;

import main.InputsLogs;

/**
 * Task class deals with the records
 * from the tasks table.
 */
public class Task {

    /**
     * Task class attributes.
     */
    int taskId;
    String assigned;
    String taskName;
    String taskDescription;
    LocalDate startDate;
    LocalDate dueDate;
    Boolean status;

    /**
     * Task class object constructor.
     * @param taskId tasks table key.
     * @param assigned String user assigned.
     * @param taskName String task name.
     * @param taskDescription String task description.
     * @param startDate LocalDate date task is assigned.
     * @param dueDate LocalDate date task is due.
     * @param status Boolean task completed or not.
     */
    public Task(
            int taskId,
            String assigned,
            String taskName,
            String taskDescription,
            LocalDate startDate,
            LocalDate dueDate,
            boolean status
    ) {

        this.taskId = taskId;
        this.assigned = assigned;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    /**
     * Task class getter methods.
     * @return relevant class attribute.
     */
    public int getTaskId() {

        return taskId;
    }

    public String getAssigned() {
        return assigned;
    }

    public boolean getStatus() {
        return status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Task class setter methods.
     */
    public void setAssigned(String newAssigned) {

        this.assigned = newAssigned;
    }

    public void setTaskName(String newTaskName) {

        this.taskName = newTaskName;
    }

    public void setTaskDescription(String newDescription) {

        this.taskDescription = newDescription;
    }

    public void setDueDate(LocalDate newDueDate) {

        this.dueDate = newDueDate;
    }

    public void setTaskStatus() {

        this.status = true;
    }

    /**
     * Displays Task class object information
     * to console.
     */
    public void taskToString() {
        String banner = "=".repeat(40);
        String stringStatus;
        if (Boolean.TRUE.equals(this.status)) {
            stringStatus = "finalized";
        } else stringStatus = "pending";
        String displayString = banner;
        displayString += "\n~ TASK ID > " + this.taskId;
        displayString += "\n~ USER ASSIGNED > " + this.assigned;
        displayString += "\n~ TASK NAME > " + this.taskName;
        displayString += "\n~ TASK DESCRIPTION > " + this.taskDescription;
        displayString += "\n~ START DATE > " + this.startDate;
        displayString += "\n~ DUE DATE > " + this.dueDate;
        displayString += "\n~ STATUS > " + stringStatus;
        displayString += "\n" + banner;
        InputsLogs.print(displayString);
    }
}
