package employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@ToString
public class Employee {
    private long id;
    private String firstName;
    private String lastName;
    private String countryName;
    @ToString.Exclude
    private String email;
    @ToString.Exclude
    private String phoneNumber;
    @ToString.Exclude
    private LocalDateTime hireDate;
    @ToString.Exclude
    private long jobId;
    @ToString.Exclude
    private BigDecimal salary;
    @ToString.Exclude
    private long managerId;
    @ToString.Exclude
    private long departmentId;
}
