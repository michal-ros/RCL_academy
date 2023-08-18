import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDbService {

    /**
     * Get employees whose department is in some State (e.g. Washington)
     *
     * @param connection connection to the database.
     * @param state state to search for.
     * @return list of employees.
     * @throws SQLException if the query is invalid.
     */
    public List<Employee> getEmployeesFromState(Connection connection, String state) throws SQLException {
        List<Employee> employees = new ArrayList<>();

        String query = """
                SELECT
                	employee.first_name,
                	employee.last_name,
                	location.state_province
                FROM employees employee
                JOIN departments department
                	ON employee.department_id = department.department_id
                JOIN locations location
                	ON department.location_id = location.location_id
                WHERE location.state_province = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, state);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                String stateProvince = result.getString("state_province");
                employees.add(Employee.createWithState(firstName, lastName, stateProvince));
            }
        }
        return employees;
    }

    /**
     * Get employees whose department is in some Country (e.g. Canada)
     *
     * @param connection connection to the database.
     * @param country country to search for.
     * @return list of employees.
     * @throws SQLException if the query is invalid.
     */
    public List<Employee> getEmployeesFromCountry(Connection connection, String country) throws SQLException {
        List<Employee> employees = new ArrayList<>();

        String query = """
                SELECT
                	employee.first_name,
                	employee.last_name,
                	location.country_id
                FROM
                	employees employee
                JOIN departments department
                	ON employee.department_id = department.department_id
                JOIN locations location
                	ON department.location_id = location.location_id
                WHERE location.country_id = ?
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, country);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                String firstName = result.getString("first_name");
                String lastName = result.getString("last_name");
                String countryId = result.getString("country_id");
                employees.add(Employee.createWithCountry(firstName, lastName, countryId));
            }
        }
        return employees;
    }

    /**
     * Get employees whose salary is greater than some amount.
     * @param connection connection to the database.
     * @param salaryIncrease salary increase to apply.
     * @param department department to search for.
     * @return true if the salary was raised successfully, false otherwise.
     * @throws SQLException if the query is invalid.
     */
    public boolean raiseSalaryInDepartment(Connection connection, BigDecimal salaryIncrease, String department) throws SQLException {

        String query = """
            UPDATE employees
            SET salary = salary + ?
            WHERE department_id
                IN (
                SELECT department_id
                FROM departments
                WHERE department_name = ?
                )
            """;

        boolean result;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);

            // Set the parameters for the PreparedStatement
            preparedStatement.setBigDecimal(1, salaryIncrease);
            preparedStatement.setString(2, department);

            // Check result of execution
            result = preparedStatement.executeUpdate() > 0;

            // Commit transaction if there was no error
            connection.commit();

        } catch (SQLException e) {
            // Rollback transaction if there was an error
            connection.rollback();
            throw e;

        } finally {
            connection.setAutoCommit(true);
        }
        return result;
    }
}
