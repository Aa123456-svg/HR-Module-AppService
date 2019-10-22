package ir.appservice.model.repository;

import ir.appservice.model.entity.domain.Employee;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;

@Repository
@ApplicationScope
public interface EmployeeRepository extends CrudRepository<Employee, String> {

    List<Employee> findAllByAccountIsNullAndStatusIs(String status);

    List<Employee> findAllByNaturalPersonIsNullAndStatusIs(String status);

    List<Employee> findAllByStatusIs(String status);

}
