import javax.sql.rowset.CachedRowSet;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {

        // Create an instance of EmployeeDbService
        EmployeeDbService employeeDbService = new EmployeeDbService();
        List<Employee> employees;
        CachedRowSet employeesCachedRowSet;

        // First connection
        try (Connection connection = DbConnectionFactory.createConnection()) {


            // Get employees whose department is in Washington
            employees = employeeDbService.getEmployeesFromState(connection, "Washington");
            employees.forEach(System.out::println);

            // Raise salary for all employees in IT department
            if (employeeDbService.raiseSalaryInDepartment(connection, BigDecimal.valueOf(500), "IT")) {
                System.out.println("Salary raised successfully!");
            } else {
                System.out.println("Salary raise failed!");
            }

            // Fetch employees whose department is in Canada
            employeesCachedRowSet = employeeDbService.fetchEmployeesFromCountry(connection, "Canada");

        }

        // Get employees whose department is in Canada and store them in a file
        employees = employeeDbService.getEmployeesFromCountry(employeesCachedRowSet);
        FileWriterHelper.writeToFile("employees.txt", employees, Employee::toString);

    }
}
