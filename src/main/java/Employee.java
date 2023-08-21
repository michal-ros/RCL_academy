
public class Employee {
    private final String firstName;
    private final String lastName;
    private String countryName;
    private String stateProvince;

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Employee createWithCountry(String firstName, String lastName, String countryName) {
        Employee employee = new Employee(firstName, lastName);
        employee.countryName = countryName;
        return employee;
    }

    public static Employee createWithState(String firstName, String lastName, String stateProvince) {
        Employee employee = new Employee(firstName, lastName);
        employee.stateProvince = stateProvince;
        return employee;
    }

    @Override
    public String toString() {
        return firstName +
                " " + lastName +
                (countryName != null ? ", " + countryName : "") +
                (stateProvince != null ? ", " + stateProvince : "");
    }
}
