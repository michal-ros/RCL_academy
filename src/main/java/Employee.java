import lombok.Builder;


@Builder
public class Employee {

    private int employeeId;
    private String firstName;
    private String lastName;

    private String stateProvince;
    private String countryName;

    @Override
    public String toString() {
        String result = employeeId + " " + firstName + " " + lastName;
        if(stateProvince != null) result += " " + stateProvince;
        if(countryName != null) result += " " + countryName;
        return result;
    }
}
