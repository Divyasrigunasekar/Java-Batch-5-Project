package service;
import java.util.List;
import dao.IProjectRepository;
import dao.ProjectRepositoryImpl;
import model.Employee;
import model.Project;
import model.Task;
import myexceptions.EmployeeNotFoundException;
import myexceptions.ProjectNotFoundException;

public class ProjectServiceImpl implements IProjectService
{

    private IProjectRepository repo = new ProjectRepositoryImpl();

    @Override
    public boolean createEmployee(Employee emp)
    {
        if (emp != null && emp.getName() != null) 
        {
            return repo.createEmployee(emp);
        }
        return false;
    }

    @Override
    public boolean createProject(Project pj)
    {
        if (pj != null && pj.getProjectName() != null)
        {
            return repo.createProject(pj);
        }
        return false;
    }

    @Override
    public boolean createTask(Task task) 
    {
        if (task != null && task.getTaskName() != null) 
        {
            return repo.createTask(task);
        }
        return false;
    }

    @Override
    public boolean assignProjectToEmployee(int projectId, int employeeId)
            throws ProjectNotFoundException, EmployeeNotFoundException
    {
        if (!repo.projectExists(projectId))
        {
            throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
        }
        if (!repo.employeeExists(employeeId)) 
        {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found.");
        }
        return repo.assignProjectToEmployee(projectId, employeeId);
    }

    @Override
    public boolean assignTaskToEmployeeInProject(int taskId, int projectId, int employeeId)
            throws ProjectNotFoundException, EmployeeNotFoundException 
    {
        if (!repo.projectExists(projectId)) 
        {
            throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
        }
        if (!repo.employeeExists(employeeId))
        {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found.");
        }
        return repo.assignTaskToEmployeeInProject(taskId, projectId, employeeId);
    }

    @Override
    public boolean deleteEmployee(int userId) throws EmployeeNotFoundException
    {
        if (!repo.employeeExists(userId))
        {
            throw new EmployeeNotFoundException("Employee with ID " + userId + " not found.");
        }
        return repo.deleteEmployee(userId);
    }

    @Override
    public boolean deleteProject(int projectId) throws ProjectNotFoundException 
    {
        if (!repo.projectExists(projectId)) 
        {
            throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
        }
        return repo.deleteProject(projectId);
    }

    @Override
    public List<Task> getAllTasks(int empId, int projectId)
            throws EmployeeNotFoundException, ProjectNotFoundException
    {
        if (!repo.projectExists(projectId)) 
        {
            throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
        }
        if (!repo.employeeExists(empId)) 
        {
            throw new EmployeeNotFoundException("Employee with ID " + empId + " not found.");
        }
        return repo.getAllTasks(empId, projectId);
    }
}