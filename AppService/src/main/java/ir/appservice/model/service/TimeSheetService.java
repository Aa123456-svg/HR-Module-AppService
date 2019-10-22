package ir.appservice.model.service;

import ir.appservice.model.entity.domain.DayAttendance;
import ir.appservice.model.entity.domain.TimeAttendance;
import ir.appservice.model.entity.domain.TimeSheet;
import ir.appservice.model.repository.DayAttendanceRepository;
import ir.appservice.model.repository.EmployeeRepository;
import ir.appservice.model.repository.TimeAttendanceRepository;
import ir.appservice.model.repository.TimeSheetRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Calendar;
import java.util.Date;

@Service
@ApplicationScope
public class TimeSheetService extends CrudService<TimeSheet> {

    private EmployeeRepository employeeRepository;
    private DayAttendanceRepository dayAttendanceRepository;
    private TimeAttendanceRepository timeAttendanceRepository;
    private TimeSheetRepository timeSheetRepository;

    public TimeSheetService(EmployeeRepository employeeRepository, DayAttendanceRepository dayAttendanceRepository, TimeAttendanceRepository timeAttendanceRepository, TimeSheetRepository timeSheetRepository) {
        super(timeSheetRepository, TimeSheet.class);
        this.employeeRepository = employeeRepository;
        this.dayAttendanceRepository = dayAttendanceRepository;
        this.timeAttendanceRepository = timeAttendanceRepository;
        this.timeSheetRepository = timeSheetRepository;
    }

    public void recordTimeAttendance(TimeAttendance timeAttendance) {
        logger.trace("Time Attendance: {}", timeAttendance);

        DayAttendance dayAtt;
        Date beginningOfDay = getBeginningOfDay(timeAttendance.getDateOccurred());
        Date endOfDay = getEndOfDay(timeAttendance.getDateOccurred());
        logger.trace("beginningOfDay: {}", beginningOfDay);
        logger.trace("endOfDay: {}", endOfDay);
        if (dayAttendanceRepository.existsByEmployeeAndDateOccurredIsAfterAndDateOccurredIsBefore(timeAttendance.getEmployee(), beginningOfDay, endOfDay)) {
            dayAtt = dayAttendanceRepository.findByEmployeeAndDateOccurredIsAfterAndDateOccurredIsBefore(timeAttendance.getEmployee(), beginningOfDay, endOfDay);
        } else {
            Date beginningOfMonth = getBeginningOfMonth(timeAttendance.getDateOccurred());
            Date endOfMonth = getEndOfMonth(timeAttendance.getDateOccurred());
            logger.trace("beginningOfMonth: {}", beginningOfMonth);
            logger.trace("endOfMonth: {}", endOfMonth);
            TimeSheet timeSheet;
            if (timeSheetRepository.existsByEmployeeAndDateRelatedIsAfterAndDateRelatedIsBefore(timeAttendance.getEmployee(), beginningOfMonth, endOfMonth)) {
                timeSheet = timeSheetRepository.findByEmployeeAndDateRelatedIsAfterAndDateRelatedIsBefore(timeAttendance.getEmployee(), beginningOfMonth, endOfMonth);
            } else {
                timeSheet = new TimeSheet(timeAttendance.getEmployee(), timeAttendance.getDateOccurred());
            }

            logger.trace("Time Sheet Month {} created.", timeAttendance.getDateOccurred());
            dayAtt = new DayAttendance(timeAttendance.getDateOccurred());
            dayAtt.setDisplayName(timeAttendance.getDisplayName());
            dayAtt.setEmployee(timeAttendance.getEmployee());
            dayAtt.setTimeSheet(timeSheet);
        }

        timeAttendance.setDayAttendance(dayAtt);
        timeAttendanceRepository.save(timeAttendance);
    }

    public Date getBeginningOfDay(Date date) {
        Calendar temp = Calendar.getInstance();
        temp.setTime(date);
        temp.set(Calendar.HOUR_OF_DAY, temp.getActualMinimum(Calendar.HOUR_OF_DAY));
        temp.set(Calendar.MINUTE, temp.getActualMinimum(Calendar.MINUTE));
        temp.set(Calendar.SECOND, temp.getActualMinimum(Calendar.SECOND));

        temp.set(Calendar.MILLISECOND, temp.getActualMinimum(Calendar.MILLISECOND));
        return temp.getTime();
    }

    public Date getEndOfDay(Date date) {
        Calendar temp = Calendar.getInstance();
        temp.setTime(date);
        temp.set(Calendar.HOUR_OF_DAY, temp.getActualMaximum(Calendar.HOUR_OF_DAY));
        temp.set(Calendar.MINUTE, temp.getActualMaximum(Calendar.MINUTE));
        temp.set(Calendar.SECOND, temp.getActualMaximum(Calendar.SECOND));
        temp.set(Calendar.MILLISECOND, temp.getActualMaximum(Calendar.MILLISECOND));
        return temp.getTime();
    }

    public Date getBeginningOfMonth(Date date) {
        Calendar temp = Calendar.getInstance();
        temp.setTime(date);
        temp.set(Calendar.DAY_OF_MONTH, temp.getActualMinimum(Calendar.DAY_OF_MONTH));
        temp.set(Calendar.HOUR_OF_DAY, temp.getActualMinimum(Calendar.HOUR_OF_DAY));
        temp.set(Calendar.MINUTE, temp.getActualMinimum(Calendar.MINUTE));
        temp.set(Calendar.SECOND, temp.getActualMinimum(Calendar.SECOND));
        temp.set(Calendar.MILLISECOND, temp.getActualMinimum(Calendar.MILLISECOND));
        return temp.getTime();
    }

    public Date getEndOfMonth(Date date) {
        Calendar temp = Calendar.getInstance();
        temp.setTime(date);
        temp.set(Calendar.DAY_OF_MONTH, temp.getActualMaximum(Calendar.DAY_OF_MONTH));
        temp.set(Calendar.HOUR_OF_DAY, temp.getActualMaximum(Calendar.HOUR_OF_DAY));
        temp.set(Calendar.MINUTE, temp.getActualMaximum(Calendar.MINUTE));
        temp.set(Calendar.SECOND, temp.getActualMaximum(Calendar.SECOND));
        temp.set(Calendar.MILLISECOND, temp.getActualMaximum(Calendar.MILLISECOND));
        return temp.getTime();
    }


}
