import java.util.ArrayList;
import java.util.List;

public class Manager extends TeamMember {
    private List<Project> projectsOverseen;

    public Manager(String name, String email) {
        super(name, email);
        this.projectsOverseen = new ArrayList<>();
    }

    // Oversee the project, add it to the manager's list of overseen projects
    public void overseeProject(Project project) {
        projectsOverseen.add(project);
        System.out.println(getName() + " is overseeing project: " + project.getName());
    }
}
