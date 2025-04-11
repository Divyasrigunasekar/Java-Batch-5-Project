package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Employee;
import model.Project;
import model.Task;
import myexceptions.EmployeeNotFoundException;
import myexceptions.ProjectNotFoundException;
import util.DBConnUtil;

public class ProjectRepositoryImpl implements IProjectRepository 
{

    private Connection conn;

    public ProjectRepositoryImpl() 
    {
        conn = DBConnUtil.getConnection("db.properties");
    }

    @Override
    public boolean createEmployee(Employee emp)
    {
        String query = "insert into employee (name, designation, gender, salary, project_id) values (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setString(1, emp.getName());
            pstmt.setString(2, emp.getDesignation());
            pstmt.setString(3, emp.getGender());
            pstmt.setDouble(4, emp.getSalary());
            pstmt.setInt(5, emp.getProjectId());
            return pstmt.executeUpdate() > 0;
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createProject(Project pj)
    {
        String query = "insert into project (project_name, description, startDate, status) values (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) 
        {
            pstmt.setString(1, pj.getProjectName());
            pstmt.setString(2, pj.getDescription());
            pstmt.setDate(3, pj.getStartDate());
            pstmt.setString(4, pj.getStatus());
            
            return pstmt.executeUpdate() > 0;
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createTask(Task task) {
        String sql = "insert into task (task_name, projectId, employeeId, status) values (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, task.getTaskName());
            stmt.setInt(2, task.getProjectId());
            stmt.setInt(3, task.getEmployeeId());
            stmt.setString(4, task.getStatus());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean assignProjectToEmployee(int projectId, int employeeId) throws ProjectNotFoundException, EmployeeNotFoundException {
        if (!projectExists(projectId)) {
            throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
        }
        if (!employeeExists(employeeId)) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found.");
        }

        int taskId = getDefaultTaskIdForProject(projectId);

        String query = "insert into employee_project_task (employee_id, project_id, task_id) values (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);
            pstmt.setInt(2, projectId);
            pstmt.setInt(3, taskId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean assignTaskToEmployeeInProject(int taskId, int projectId, int employeeId) throws ProjectNotFoundException, EmployeeNotFoundException {
        if (!projectExists(projectId)) {
            throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
        }
        if (!employeeExists(employeeId)) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found.");
        }

        if (isTaskAssignedToEmployeeInProject(taskId, projectId, employeeId)) {
            System.out.println("This task is already assigned to the employee in the project.");
            return false;
        }

        String query = "insert into employee_project_task (employee_id, project_id, task_id) values (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);
            pstmt.setInt(2, projectId);
            pstmt.setInt(3, taskId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isTaskAssignedToEmployeeInProject(int taskId, int projectId, int employeeId) {
        String query = "select 1 from employee_project_task where task_id = ? and project_id = ? and employee_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, taskId);
            pstmt.setInt(2, projectId);
            pstmt.setInt(3, employeeId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteEmployee(int userId) throws EmployeeNotFoundException {
        if (!employeeExists(userId)) {
            throw new EmployeeNotFoundException("Employee with ID " + userId + " not found.");
        }
        String query = "delete from employee where id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteProject(int projectId) throws ProjectNotFoundException {
        if (!projectExists(projectId)) {
            throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
        }
        String query = "delete from project where id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, projectId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Task> getAllTasks(int empId, int projectId) throws EmployeeNotFoundException, ProjectNotFoundException {
        if (!projectExists(projectId)) {
            throw new ProjectNotFoundException("Project with ID " + projectId + " not found.");
        }
        if (!employeeExists(empId)) {
            throw new EmployeeNotFoundException("Employee with ID " + empId + " not found.");
        }
        List<Task> taskList = new ArrayList<>();
        String query = "SELECT t.taskId, t.task_name, t.status, t.projectId, t.employeeId FROM task t " +
                       "JOIN employee_project_task ept ON t.taskId = ept.task_id " +
                       "WHERE ept.employee_id = ? AND ept.project_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, empId);
            pstmt.setInt(2, projectId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("taskId"));
                task.setTaskName(rs.getString("task_name"));
                task.setStatus(rs.getString("status"));
                task.setProjectId(rs.getInt("projectId"));
                task.setEmployeeId(rs.getInt("employeeId"));
                taskList.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskList;
    }
    @Override
    public boolean employeeExists(int empId) {
        String query = "select id from employee where id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, empId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean projectExists(int projectId) {
        String query = "select id from project where id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, projectId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int getDefaultTaskIdForProject(int projectId) {
        return 1;  
    }
}