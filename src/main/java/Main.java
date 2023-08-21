import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeService();

        var result1 = employeeService.getEmployeesFromWashington();
        printToConsole(result1);

        var result2 = employeeService.getEmployeesFromCanada();
        printToFile("Canadians.txt", result2);

        var result3 = employeeService.riseSalaryInITBy500();
        System.out.println("Rows affected: " + result3);
    }

    public static <T> void printToConsole(List<T> list) {
        for (T t : list) {
            System.out.println(t);
        }
    }

    public static <T> void printToFile(String fileName, List <T> list){
        try (PrintWriter writer = new PrintWriter(new FileWriter("src/main/resources/"+fileName))) {
            for(T t : list) {
                writer.println(t);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}



