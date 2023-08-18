import employee.EmployeeDAO;

public class Main {

    public static void main(String[] args) {

        EmployeeDAO employeeDAO = new EmployeeDAO();

        System.out.println(employeeDAO.getEmployeesFromWashington());

        employeeDAO.writeToFileEmployeesFromCanada();

        employeeDAO.riseITEmployeesSalaryBy500();

    }
}
