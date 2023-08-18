package employee;

import utils.DbConnectionFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class EmployeeDAO {

    private static final String WASHINGTON_EMPL_QUERY =
            "SELECT e.employee_id, e.first_name, e.last_name FROM employees e " +
                    "JOIN departments d ON e.department_id = d.department_id " +
                    "JOIN locations l ON d.location_id = l.location_id " +
                    "WHERE l.state_province = 'Washington'";

    private static final String CANADA_EMPL_QUERY =
            "SELECT e.employee_id, e.first_name, e.last_name, c.country_name FROM employees e " +
                    "JOIN departments d ON e.department_id = d.department_id " +
                    "JOIN locations l ON d.location_id = l.location_id " +
                    "JOIN countries c ON l.country_id = c.country_id " +
                    "WHERE country_name = 'Canada'";

    private static final String RAISE_IT_SALARY_BY_500_QUERY =
            "UPDATE employees  SET salary = salary+500 " +
                    "WHERE department_id = 6";

    public List<Employee> getEmployeesFromWashington() {

        List<Employee> result = new ArrayList<>();
        try (Connection connection = DbConnectionFactory.createConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(WASHINGTON_EMPL_QUERY);

            while (resultSet.next()) {

                long id = resultSet.getLong("employee_id");
                String name = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                Employee employee = Employee.builder()
                        .id(id)
                        .firstName(name)
                        .lastName(lastName)
                        .countryName("United States of America")
                        .build();
                result.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void writeToFileEmployeesFromCanada() {
        List<Employee> result = new ArrayList<>();
        try (Connection connection = DbConnectionFactory.createConnection()) {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(CANADA_EMPL_QUERY);

            while (resultSet.next()) {

                long id = resultSet.getLong("employee_id");
                String name = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String countryName = resultSet.getString("country_name");

                Employee employee = Employee.builder()
                        .id(id)
                        .firstName(name)
                        .lastName(lastName)
                        .countryName(countryName)
                        .build();
                result.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("CanadaEmployees")) {

            result.forEach(employee -> {
                try {
                    writer.write(employee.toString() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void riseITEmployeesSalaryBy500() {

        try (Connection connection = DbConnectionFactory.createConnection()) {

            Statement statement = connection.createStatement();
            statement.executeUpdate(RAISE_IT_SALARY_BY_500_QUERY);
            System.out.println("Salary for all IT employees has been raised by 500!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
