import java.util.Date;

public class RecurringTask extends Task {
    private String recurrence;

    public RecurringTask(String title, String description, Date dueDate, Priority priority, String recurrence) {
        super(title, description, dueDate, priority);
        this.recurrence = recurrence;
    }

    public String getRecurrence() { return recurrence; }
}
