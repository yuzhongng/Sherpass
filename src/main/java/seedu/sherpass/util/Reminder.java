package seedu.sherpass.util;

import seedu.sherpass.task.Task;
import seedu.sherpass.task.TaskList;

import java.util.ArrayList;
import java.util.Comparator;
import java.time.LocalDate;

import static java.util.stream.Collectors.toList;


public class Reminder {
    private Ui ui;
    private ArrayList<Task> tasks;
    private LocalDate todayDate = LocalDate.now();

    /**
     * Create a constructor for the class Reminder.
     *
     * @param taskList Representation of an array of tasks.
     */
    public Reminder(TaskList taskList, Ui ui) {
        tasks = taskList.getTasks();
        this.ui = ui;
    }

    /**
     * Prints reminders to the user that
     * the task is due soon or
     * tasks that need to be done on the day.
     */
    public void showReminders() {
        showDailyTask();
        showWeeklyTask();
    }

    /**
     * Prints tasks that need to be done today
     * with reference to user local machine date.
     */
    public ArrayList<Task> showDailyTask() {
        ArrayList<Task> filteredDailyTasks = getFilteredDailyTasks();

        if (filteredDailyTasks.isEmpty()) {
            ui.showToUser("Your schedule is empty today.");
        } else {
            ui.showToUser("Schedule for today:");
            for (Task task : filteredDailyTasks) {
                System.out.println(task.toString());
            }
        }
        ui.showLine();
        return filteredDailyTasks;
    }

    /**
     * Prints tasks that is due in the current week
     * with reference to user local machine date.
     */
    public ArrayList<Task> showWeeklyTask() {
        LocalDate nextWeekDate = todayDate.plusDays(7);

        ArrayList<Task> filteredThisWeekTasks = (ArrayList<Task>) tasks.stream()
                .filter((t) -> t.getByDate().isBefore(nextWeekDate))
                .filter((t) -> !t.isDone())
                .sorted(Comparator.comparing(Task::getByDate))
                .collect(toList());

        if (filteredThisWeekTasks.isEmpty()) {
            ui.showToUser("You do not have any pending task for the week.");
        } else {
            ui.showToUser("Tasks to be done within the week:");
            for (Task task : filteredThisWeekTasks) {
                System.out.println(task.toString());
            }
        }
        ui.showLine();
        return filteredThisWeekTasks;
    }

    private void addDailyTaskToFilteredDailyTasks(ArrayList<Task> filteredDailyTasks, Task task) {
        if (hasNoDeadline(task)) {
            return;
        } else if (task.getByDate().isEqual(todayDate)) {
            filteredDailyTasks.add(task);
        } else if (hasNoReminderDate(task)) {
            return;
        } else if (task.getDoOnDate().isEqual(todayDate)) {
            filteredDailyTasks.add(task);
        } else {
            return;
        }
    }

    private ArrayList<Task> getFilteredDailyTasks() {
        ArrayList<Task> filteredDailyTasks = new ArrayList<>();
        for (Task task : tasks) {
            addDailyTaskToFilteredDailyTasks(filteredDailyTasks, task);
        }
        return (ArrayList<Task>) filteredDailyTasks.stream()
                .filter((t) -> !t.isDone())
                .collect(toList());
    }

    private boolean hasNoDeadline(Task task) {
        if (task.getByDate() == null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean hasNoReminderDate(Task task) {
        if (task.getDoOnDate() == null) {
            return true;
        } else {
            return false;
        }
    }
}
