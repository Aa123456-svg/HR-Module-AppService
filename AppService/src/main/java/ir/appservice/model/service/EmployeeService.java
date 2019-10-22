package ir.appservice.model.service;

import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.entity.domain.Employee;
import ir.appservice.model.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.ArrayList;
import java.util.List;

@Service
@ApplicationScope
public class EmployeeService extends CrudService<Employee> {

    public EmployeeService(EmployeeRepository employeeRepository) {
        super(employeeRepository, Employee.class);
    }

    public EmployeeRepository getEmployeeRepository() {
        return (EmployeeRepository) this.crudRepository;
    }

    public List<Employee> getAllSubordinates(Employee emp) {
        emp = getEmployeeRepository().getOne(emp.getId());
        List<Employee> allReporters = new ArrayList();
        if (emp.getStatus().equals(BaseEntity.ACTIVE_STATUS)) {
            allReporters.add(emp);
        }
        emp.getReporters().forEach(employee -> allReporters.addAll(getAllSubordinates(employee)));
        return allReporters;
    }

}
