package ir.appservice.model.repository;

import ir.appservice.model.entity.domain.Employee;
import ir.appservice.model.entity.domain.TimeSheet;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Date;
import java.util.List;

@Repository
@ApplicationScope
public interface TimeSheetRepository extends CrudRepository<TimeSheet, String> {

    TimeSheet findByEmployeeAndDateRelatedIsAfterAndDateRelatedIsBefore(Employee employee,
                                                                        Date startDate,
                                                                        Date endDate);

    List<TimeSheet> findAllByEmployeeAndDateRelatedIsAfterAndDateRelatedIsBefore(Employee employee,
                                                                                 Date startDate,
                                                                                 Date endDate);

    boolean existsByEmployeeAndDateRelatedIsAfterAndDateRelatedIsBefore(Employee employee,
                                                                        Date startDate,
                                                                        Date endDate);
}
