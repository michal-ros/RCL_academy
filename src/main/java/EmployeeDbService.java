import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.RowSetProvider;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
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
     * @return list of employees.
     * @throws SQLException if the cached row set is invalid.
     */
    public List<Employee> getEmployeesFromCountry(CachedRowSet crs) throws SQLException {
        List<Employee> employees = new ArrayList<>();

        try (crs) {
            while (crs.next()) {
                String firstName = crs.getString("first_name");
                String lastName = crs.getString("last_name");
                String countryId = crs.getString("country_name");
                employees.add(Employee.createWithCountry(firstName, lastName, countryId));
            }
        }

        return employees;
    }

    /**
     * Fetch employees whose department is in some Country (e.g. Canada)
     *
     * @param connection connection to the database.
     * @param country country to search for.
     * @return cached row set of employees to further process.
     * @throws SQLException if the query is invalid.
     */
    public CachedRowSet fetchEmployeesFromCountry(Connection connection, String country) throws SQLException {

        String query = """
                SELECT
                    employee.first_name,
                    employee.last_name,
                    country.country_name
                FROM employees employee
                JOIN departments department
                    ON employee.department_id = department.department_id
                JOIN locations location
                    ON department.location_id = location.location_id
                JOIN countries country
                    ON location.country_id = country.country_id
                WHERE country.country_name = ?
                """;

        // Store results in a CachedRowSet and then pass them to the caller
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, country);
            ResultSet result = preparedStatement.executeQuery();
            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet crs = factory.createCachedRowSet();
            crs.populate(result);
            return crs;
        }
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
                UPDATE employees employee
                SET salary = salary + ?
                FROM departments department
                WHERE employee.department_id = department.department_id
                AND department.department_name = ?
                """;

        boolean result;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set the parameters for the PreparedStatement
            preparedStatement.setBigDecimal(1, salaryIncrease);
            preparedStatement.setString(2, department);

            // Check result of execution
            result = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            // Rollback transaction if there was an error
            connection.rollback();
            throw e;

        }
        return result;
    }
}
