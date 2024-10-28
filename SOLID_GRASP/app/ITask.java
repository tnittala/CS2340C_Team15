import java.util.Date;

public interface ITask {
    String getTitle();
    Date getDueDate();
    Status getStatus();
    void assignMember(TeamMember member);
}
