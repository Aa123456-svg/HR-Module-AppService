package ir.appservice.model.repository;

import ir.appservice.model.entity.domain.DayAttendance;
import ir.appservice.model.entity.domain.Employee;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Date;
import java.util.List;

@Repository
@ApplicationScope
public interface DayAttendanceRepository extends CrudRepository<DayAttendance, String> {

    DayAttendance findByEmployeeAndDateOccurredIsAfterAndDateOccurredIsBefore(Employee employee,
                                                                              Date startDate,
                                                                              Date endDate);

    List<DayAttendance> findAllByEmployeeAndDateOccurredIsAfterAndDateOccurredIsBefore(Employee employee,
                                                                                       Date startDate,
                                                                                       Date endDate);

    boolean existsByEmployeeAndDateOccurredIsAfterAndDateOccurredIsBefore(Employee employee,
                                                                          Date startDate,
                                                                          Date endDate);
}
