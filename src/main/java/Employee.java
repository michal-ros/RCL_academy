import java.util.Locale;

public class Employee {
    private final String firstName;
    private final String lastName;
    private String countryId;
    private String countryName;
    private String stateProvince;

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static Employee createWithCountry(String firstName, String lastName, String countryId) {
        Employee employee = new Employee(firstName, lastName);
        employee.countryId = countryId;
        Locale locale = new Locale("", countryId);
        employee.countryName = locale.getDisplayCountry();
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
                (countryId != null ? ", " + countryName : "") +
                (stateProvince != null ? ", " + stateProvince : "");
    }
}
