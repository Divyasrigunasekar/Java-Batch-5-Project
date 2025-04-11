package service;
import java.util.List;
import model.Employee;
import model.Project;
import model.Task;
import myexceptions.EmployeeNotFoundException;
import myexceptions.ProjectNotFoundException;

public interface IProjectService 
{
    boolean createEmployee(Employee emp);
    boolean createProject(Project pj);
    boolean createTask(Task task);
    
    boolean assignProjectToEmployee(int projectId, int employeeId)
        throws ProjectNotFoundException, EmployeeNotFoundException;
    
    boolean assignTaskToEmployeeInProject(int taskId, int projectId, int employeeId)
        throws ProjectNotFoundException, EmployeeNotFoundException;
    
    boolean deleteEmployee(int userId)
        throws EmployeeNotFoundException;
    
    boolean deleteProject(int projectId)
        throws ProjectNotFoundException;
    
    List<Task> getAllTasks(int empId, int projectId)
        throws EmployeeNotFoundException, ProjectNotFoundException;
}