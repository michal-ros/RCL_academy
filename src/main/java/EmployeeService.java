import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {

    private static final String GET_EMPLOYEES_FROM_WASHINGTON =
            "SELECT employee_id, first_name, last_name, state_province FROM employees e " +
                    "JOIN departments d ON e.department_id=d.department_id " +
                    "JOIN locations l ON d.location_id=l.location_id " +
                    "WHERE state_province='Washington'";

    private static final String GET_EMPLOYEES_FROM_CANADA =
            "SELECT employee_id, first_name , last_name, country_name FROM employees e " +
                    "JOIN departments d ON e.department_id=d.department_id " +
                    "JOIN locations l ON d.location_id=l.location_id " +
                    "JOIN countries c ON l.country_id=c.country_id " +
                    "WHERE c.country_name='Canada'";

    private static final String RISE_SALARY_IN_IT_BY_500 =
            "UPDATE employees SET salary=salary+500 WHERE department_id IN " +
                    "(SELECT department_id FROM departments WHERE department_name='IT')";

    public EmployeeService() {
    }

    public List<Employee> getEmployeesFromWashington() {
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = DbConnectionFactory.createConnection()) {
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(GET_EMPLOYEES_FROM_WASHINGTON);
            while (rs.next()) {
                Employee employee = Employee.builder()
                        .employeeId(rs.getInt("employee_id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .stateProvince(rs.getString("state_province"))
                        .build();
                employees.add(employee);
                System.out.println(employee);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return employees;
    }

    public List<Employee> getEmployeesFromCanada() {
        List<Employee> employees = new ArrayList<>();
        CachedRowSet crs;
        try (Connection conn = DbConnectionFactory.createConnection()){
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(GET_EMPLOYEES_FROM_CANADA);
            RowSetFactory factory = RowSetProvider.newFactory();
            crs = factory.createCachedRowSet();
            crs.populate(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try{
            while (crs.next()) {
                Employee employee = Employee.builder()
                        .employeeId(crs.getInt("employee_id"))
                        .firstName(crs.getString("first_name"))
                        .lastName(crs.getString("last_name"))
                        .countryName(crs.getString("country_name"))
                        .build();
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return employees;
    }

    public int riseSalaryInITBy500() {
        int rowsAffected = 0;
        try (Connection conn = DbConnectionFactory.createConnection()) {
            conn.setAutoCommit(false);
            try {
                Statement stat = conn.createStatement();
                rowsAffected = stat.executeUpdate(RISE_SALARY_IN_IT_BY_500);
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("TRANSACTION ROLLED BACK!");
            }
            conn.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("CONNECTION ERROR!");

        }
        return rowsAffected;
    }
}
