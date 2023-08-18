import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {

        // Create an instance of EmployeeDbService
        EmployeeDbService employeeDbService = new EmployeeDbService();

        // First connection
        try (Connection connection = DbConnectionFactory.createConnection()) {
            List<Employee> employees;

            // Get employees whose department is in Washington
            employees = employeeDbService.getEmployeesFromState(connection, "Washington");
            employees.forEach(System.out::println);

            // Get employees whose department is in Canada
            employees = employeeDbService.getEmployeesFromCountry(connection, "CA");
            FileWriterHelper.writeToFile("employees.txt", employees, Employee::toString);

        }

        // Second connection
        try (Connection connection = DbConnectionFactory.createConnection()) {

            // Raise salary for all employees in IT department
            if (employeeDbService.raiseSalaryInDepartment(connection, BigDecimal.valueOf(500), "IT")) {
                System.out.println("Salary raised successfully!");
            } else {
                System.out.println("Salary raise failed!");
            }

        }
    }
}
