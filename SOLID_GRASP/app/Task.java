import java.util.Date;

public class Task implements ITask {
    private String title;
    private String description;
    private Date dueDate;
    private Status status;
    private Priority priority;
    private TeamMember assignedMember;

    public Task(String title, String description, Date dueDate, Priority priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = Status.TODO;
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public Date getDueDate() { return dueDate; }

    @Override
    public Status getStatus() { return status; }

    @Override
    public void assignMember(TeamMember member) {
        this.assignedMember = member;
        System.out.println("Task assigned to: " + member.getName());
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
