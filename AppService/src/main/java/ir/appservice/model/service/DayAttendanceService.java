package ir.appservice.model.service;

import ir.appservice.model.entity.domain.DayAttendance;
import ir.appservice.model.repository.DayAttendanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

@Service
@ApplicationScope
public class DayAttendanceService extends CrudService<DayAttendance> {

    public DayAttendanceService(DayAttendanceRepository dayAttendanceRepository) {
        super(dayAttendanceRepository, DayAttendance.class);
    }
}