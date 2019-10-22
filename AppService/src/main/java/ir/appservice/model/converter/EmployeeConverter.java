package ir.appservice.model.converter;

import ir.appservice.model.entity.domain.Employee;
import ir.appservice.model.repository.EmployeeRepository;
import org.springframework.stereotype.Component;

@Component
public class EmployeeConverter extends BaseConverter<Employee> {

    public EmployeeConverter(EmployeeRepository employeeRepository) {
        super(Employee.class, employeeRepository);
    }
}
