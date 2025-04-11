package test;
import dao.ProjectRepositoryImpl;
import model.Employee;
import model.Project;
import model.Task;
import myexceptions.EmployeeNotFoundException;
import myexceptions.ProjectNotFoundException;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectRepositoryImplTest {

    private static ProjectRepositoryImpl repo;

    @BeforeAll
    static void init() {
        repo = new ProjectRepositoryImpl();
    }

    @Test
    void testEmployeeCreation() {
        Employee emp = new Employee(0, "Ajay", "Developer", "Male", 45000, 1);
        boolean result = repo.createEmployee(emp);
        assertTrue(result, "Employee should be created successfully");
    }

    @Test
    void testTaskCreation() {
        Task task = new Task(0, "Fix UI bugs", 1, 1, "Pending");
        boolean result = repo.createTask(task);
        assertTrue(result, "Task should be created successfully");
    }
    @Test
    void testGetTasksAssignedToEmployee() {
        try {
            List<Task> tasks = repo.getAllTasks(1, 1);
            assertNotNull(tasks, "Task list should not be null");
            assertFalse(tasks.isEmpty(), "Task list should not be empty");
        } catch (EmployeeNotFoundException | ProjectNotFoundException e) {
            fail("Exception should not be thrown for valid employee and project IDs");
        }
    }

    @Test
    void testEmployeeNotFoundException() {
        Exception e = assertThrows(EmployeeNotFoundException.class, () -> {
            repo.getAllTasks(999, 1);
        });
        assertEquals("Employee with ID 999 not found.", e.getMessage());
    }

    @Test
    void testProjectNotFoundException() {
        Exception e = assertThrows(ProjectNotFoundException.class, () -> {
            repo.getAllTasks(1, 999);
        });
        assertEquals("Project with ID 999 not found.", e.getMessage());
    }
}