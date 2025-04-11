package main;
import model.Employee;
import model.Project;
import model.Task;
import service.IProjectService;
import service.ProjectServiceImpl;
import util.DBConnUtil;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import myexceptions.EmployeeNotFoundException;
import myexceptions.ProjectNotFoundException;

public class MainModule 
{
    public static void main(String[] args) 
    {
        Connection con = DBConnUtil.getConnection("db.properties");
        IProjectService service = new ProjectServiceImpl();
        Scanner scanner = new Scanner(System.in);

        if (con != null) 
        {
            System.out.println("Database connected successfully!");
        }
        else 
        {
            System.out.println("Failed to connect to the database.");
        }

        int choice;
        do 
        {
            System.out.println(" Project Management System ");
            System.out.println("1. Add Employee");
            System.out.println("2. Add Project");
            System.out.println("3. Add Task");
            System.out.println("4. Assign project to employee");
            System.out.println("5. Assign task to employee within project");
            System.out.println("6. Delete Employee");
            System.out.println("7. Delete Project");
            System.out.println("8. List all tasks in a project assigned to an employee");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();

            switch (choice) 
            {
                case 1:
                    scanner.nextLine();
                    System.out.print("Enter employee name: ");
                    String empName = scanner.nextLine();
                    System.out.print("Enter designation: ");
                    String designation = scanner.nextLine();
                    System.out.print("Enter gender: ");
                    String gender = scanner.nextLine();
                    System.out.print("Enter salary: ");
                    double salary = scanner.nextDouble();
                    System.out.print("Enter project ID (default 0 if not assigned): ");
                    int projId = scanner.nextInt();

                    Employee emp = new Employee();
                    emp.setName(empName);
                    emp.setDesignation(designation);
                    emp.setGender(gender);
                    emp.setSalary(salary);
                    emp.setProjectId(projId);

                    boolean empResult = service.createEmployee(emp);
                    System.out.println(empResult ? "Employee added successfully." : "Failed to add employee.");
                    break;

                case 2:
                    scanner.nextLine();
                    System.out.print("Enter project name: ");
                    String projectName = scanner.nextLine();
                    System.out.print("Enter description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter start date (YYYY-MM-DD): ");
                    String startDateStr = scanner.nextLine();
                    Date startDate = null;
                    try 
                    {
                        startDate = Date.valueOf(startDateStr);
                    } 
                    catch (IllegalArgumentException e)
                    {
                        System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                        break;
                    }
                    System.out.print("Enter status (started/dev/build/test/deployed): ");
                    String status = scanner.nextLine();

                    Project project = new Project();
                    project.setProjectName(projectName);
                    project.setDescription(description);
                    project.setStartDate(startDate);
                    project.setStatus(status);

                    boolean projResult = service.createProject(project);
                    System.out.println(projResult ? "Project added successfully." : "Failed to add project.");
                    break;

                case 3:
                    scanner.nextLine(); 
                    System.out.print("Enter task name: ");
                    String taskName = scanner.nextLine(); 
                    System.out.print("Enter project ID: ");
                    int projectId = scanner.nextInt(); 
                    System.out.print("Enter employee ID: ");
                    int employeeId = scanner.nextInt(); 
                    scanner.nextLine(); 
                    System.out.print("Enter task status (Assigned/Started/Completed): ");
                    String taskStatus = scanner.nextLine(); 

                    Task task = new Task();
                    task.setTaskName(taskName); 
                    task.setProjectId(projectId); 
                    task.setEmployeeId(employeeId); 
                    task.setStatus(taskStatus); 

                    boolean taskResult = service.createTask(task); 
                    System.out.println(taskResult ? "Task added successfully." : "Failed to add task.");
                    break;
                case 4:
                    System.out.print("Enter employee ID: ");
                    int empIdToAssign = scanner.nextInt();
                    System.out.print("Enter project ID: ");
                    int projectIdToAssign = scanner.nextInt();
                    try
                    {
                        boolean assignProjectResult = service.assignProjectToEmployee(projectIdToAssign, empIdToAssign);
                        System.out.println(assignProjectResult ? "Project assigned to employee." : "Failed to assign project.");
                    } 
                    catch (EmployeeNotFoundException | ProjectNotFoundException e) 
                    {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    System.out.print("Enter task ID: ");
                    int taskId = scanner.nextInt();
                    System.out.print("Enter project ID: ");
                    int projIdForTask = scanner.nextInt();
                    System.out.print("Enter employee ID: ");
                    int empIdForTask = scanner.nextInt();
                    try
                    {
                        boolean assignTaskResult = service.assignTaskToEmployeeInProject(taskId, projIdForTask, empIdForTask);
                        System.out.println(assignTaskResult ? "Task assigned successfully." : "Failed to assign task.");
                    } 
                    catch (EmployeeNotFoundException | ProjectNotFoundException e) 
                    {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 6:
                    System.out.print("Enter employee ID to delete: ");
                    int delEmpId = scanner.nextInt();
                    try
                    {
                        boolean delEmpResult = service.deleteEmployee(delEmpId);
                        System.out.println(delEmpResult ? "Employee deleted." : "Failed to delete employee.");
                    } 
                    catch (EmployeeNotFoundException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 7:
                    System.out.print("Enter project ID to delete: ");
                    int delProjId = scanner.nextInt();
                    try {
                        boolean delProjResult = service.deleteProject(delProjId);
                        System.out.println(delProjResult ? "Project deleted." : "Failed to delete project.");
                    } catch (ProjectNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 8:
                    System.out.print("Enter employee ID: ");
                    int eid = scanner.nextInt();
                    System.out.print("Enter project ID: ");
                    int pid = scanner.nextInt();
                    try {
                        List<Task> tasks = service.getAllTasks(eid, pid);
                        if (tasks != null && !tasks.isEmpty()) {
                            for (Task t : tasks) {
                                System.out.println("Task ID: " + t.getTaskId() + ", Name: " + t.getTaskName() + ", Status: " + t.getStatus());
                            }
                        } else {
                            System.out.println("No tasks found.");
                        }
                    } catch (EmployeeNotFoundException | ProjectNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 9:
                    System.out.println("Exiting--->Goodbye!!!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println();
        } while (choice != 9);

        scanner.close();
    }
}