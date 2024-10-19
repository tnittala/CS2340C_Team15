import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project {
    private String name;
    @SuppressWarnings("unused")
    private String description;
    @SuppressWarnings("unused")
    private Date startDate;
    @SuppressWarnings("unused")
    private Date endDate;
    private List<ITask> tasks;
    private List<TeamMember> members;

    public Project(String name, String description, Date startDate, Date endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tasks = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addTask(ITask task) {
        tasks.add(task);
        System.out.println("Task added: " + task.getTitle());
    }

    public void removeTask(ITask task) {
        tasks.remove(task);
        System.out.println("Task removed: " + task.getTitle());
    }

    public void addMember(TeamMember member) {
        members.add(member);
        System.out.println(member.getName() + " added to the project.");
    }

    public void removeMember(TeamMember member) {
        members.remove(member);
        System.out.println(member.getName() + " removed from the project.");
    }
}
