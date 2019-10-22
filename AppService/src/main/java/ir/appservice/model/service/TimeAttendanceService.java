package ir.appservice.model.service;

import ir.appservice.model.entity.domain.TimeAttendance;
import ir.appservice.model.repository.TimeAttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

@Service
@ApplicationScope
public class TimeAttendanceService extends CrudService<TimeAttendance> {

    public TimeAttendanceService(TimeAttendanceRepository timeAttendanceRepository) {
        super(timeAttendanceRepository, TimeAttendance.class);
    }
}