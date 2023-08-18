import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.*;

public class Main {

    public static void main(String[] args) {
        getEmployeesFromWashington();
        getEmployeesFromCanada();
        riseSalaryInITBy500();
    }

    public static void getEmployeesFromWashington() {

        try (Connection conn = DbConnectionFactory.createConnection()) {
            Statement stat = conn.createStatement();
            String command = "SELECT employee_id, first_name, last_name, state_province FROM employees e " +
                    "JOIN departments d ON e.department_id=d.department_id " +
                    "JOIN locations l ON d.location_id=l.location_id " +
                    "WHERE state_province='Washington'";
            ResultSet rs = stat.executeQuery(command);
            while (rs.next()) {
                System.out.println(
                        rs.getString("employee_id") + " " +
                        rs.getString("first_name") + " " +
                        rs.getString("last_name") + " " +
                        rs.getString("state_province")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getEmployeesFromCanada() {

        try (Connection conn = DbConnectionFactory.createConnection()) {
            Statement stat = conn.createStatement();
            String command = "SELECT employee_id, first_name , last_name, country_name FROM employees e " +
                    "JOIN departments d ON e.department_id=d.department_id " +
                    "JOIN locations l ON d.location_id=l.location_id " +
                    "JOIN countries c ON l.country_id=c.country_id " +
                    "WHERE c.country_name='Canada'";

            ResultSet rs = stat.executeQuery(command);
            try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/Canadians.txt"))) {
                while (rs.next()) {
                    writer.println(
                            rs.getString("employee_id") + " " +
                            rs.getString("first_name") + " " +
                            rs.getString("last_name") + " " +
                            rs.getString("country_name")
                    );
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void riseSalaryInITBy500() {

        try (Connection conn = DbConnectionFactory.createConnection()) {
            Statement stat = conn.createStatement();

            conn.setAutoCommit(false);
            try {
                String command = "SELECT department_id " +
                        "FROM departments " +
                        "WHERE department_name = 'IT'";
                ResultSet rs = stat.executeQuery(command);
                String departmentId = "";
                while (rs.next()) {
                    departmentId = rs.getString("department_id");
                }

                command = "UPDATE employees " +
                        "SET salary = salary + 500 " +
                        "WHERE department_id = ? ";
                PreparedStatement ps = conn.prepareStatement(command);
                ps.setInt(1, Integer.parseInt(departmentId));
                int r = ps.executeUpdate();
                System.out.println("Rows affected: " + r);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
            }
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}



