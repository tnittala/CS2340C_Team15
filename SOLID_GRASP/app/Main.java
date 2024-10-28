import java.util.Date;

public class Main {
    public static void main(String[] args) {
        // Create a project
        Project project = new Project("Task Management System", "Design and implement a task management system", 
                                      new Date(), new Date());

        // Create team members
        TeamMember archita = new TeamMember("Archita", "archita@example.com");
        Manager trisha = new Manager("Trisha", "trisha@example.com");

        // Add members to the project
        project.addMember(archita);
        project.addMember(trisha);

        // Assign Bob to oversee the project
        // This should pass the 'project' object (correct type)
        trisha.overseeProject(project);

        // Create tasks
        Task task1 = new Task("Design Database", "Design the database schema", new Date(), Priority.HIGH);
        RecurringTask task2 = new RecurringTask("Standup Meeting", "Daily team meeting", new Date(), Priority.MEDIUM, "Daily");

        // Add tasks to the project
        project.addTask(task1);
        project.addTask(task2);

        // Assign Alice to a task
        task1.assignMember(archita);

        // Change task status
        task1.setStatus(Status.IN_PROGRESS);
        System.out.println("Task 1 status: " + task1.getStatus());
    }
}
