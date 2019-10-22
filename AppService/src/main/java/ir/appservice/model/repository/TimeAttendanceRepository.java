package ir.appservice.model.repository;

import ir.appservice.model.entity.domain.TimeAttendance;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Date;
import java.util.List;

@Repository
@ApplicationScope
public interface TimeAttendanceRepository extends CrudRepository<TimeAttendance, String> {

    List<TimeAttendance> findAllByDateOccurredIsAfterAndDateOccurredIsBefore(Date startDate,
                                                                             Date endDate);

}
