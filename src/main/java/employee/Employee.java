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
@ToString(onlyExplicitlyIncluded = true)
public class Employee {
    @ToString.Include
    private long id;
    @ToString.Include
    private String firstName;
    @ToString.Include
    private String lastName;
    @ToString.Include
    private String countryName;
    private String email;
    private String phoneNumber;
    private LocalDateTime hireDate;
    private long jobId;
    private BigDecimal salary;
    private long managerId;
    private long departmentId;
}
