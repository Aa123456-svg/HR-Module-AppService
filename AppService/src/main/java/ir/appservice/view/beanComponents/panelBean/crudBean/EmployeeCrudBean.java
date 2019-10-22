package ir.appservice.view.beanComponents.panelBean.crudBean;

import ir.appservice.model.entity.domain.Employee;
import ir.appservice.model.service.EmployeeService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Setter
@Getter
@Component
@SessionScope
public class EmployeeCrudBean extends BaseLazyCrudBean<Employee> {

    public EmployeeCrudBean(EmployeeService employeeService) {
        super(employeeService, Employee.class);
    }
}
