package ir.appservice.view.beanComponents.panelBean.timeSheet;

import ir.appservice.model.entity.domain.DayAttendance;
import ir.appservice.model.entity.domain.Employee;
import ir.appservice.model.entity.domain.TimeAttendance;
import ir.appservice.model.entity.domain.TimeSheet;
import ir.appservice.model.repository.EmployeeRepository;
import ir.appservice.model.repository.TimeSheetRepository;
import ir.appservice.model.service.EmployeeService;
import ir.appservice.model.service.TimeSheetService;
import ir.appservice.view.appComponent.AppLazyDataModel;
import ir.appservice.view.beanComponents.SessionBean;
import ir.appservice.view.beanComponents.panelBean.BasePanelBean;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;

@Setter
@Getter
@Component
@SessionScope
public class TimeSheetPanelBean extends BasePanelBean {


    protected AppLazyDataModel<DayAttendance> appLazyDataModel;

    private TimeAttendance timeAttendance;
    private TimeSheet timeSheet;
    private Date timeSheetDate;
    private Employee employee;

    private Locale clientLocale;
    private Calendar calendar;

    private TimeSheetService timeSheetService;
    private TimeSheetRepository timeSheetRepository;
    private EmployeeRepository employeeRepository;

    private SessionBean sessionBean;

    private List<Employee> subOrdinates;

    public TimeSheetPanelBean(SessionBean sessionBean,
                              TimeSheetService timeSheetService,
                              TimeSheetRepository timeSheetRepository,
                              EmployeeService employeeService,
                              EmployeeRepository employeeRepository
    ) {
        this.sessionBean = sessionBean;
        this.timeSheetService = timeSheetService;
        this.timeSheetRepository = timeSheetRepository;
        this.employeeRepository = employeeRepository;

        try {
            setSubOrdinates(employeeService.getAllSubordinates(sessionBean.getAccount().getEmployee()));
        } catch (Exception e) {
            setSubOrdinates(new ArrayList<>());
        }
        newInstance();
    }

    public boolean searchForTimeSheet() {
        return getEmployee() != null && getTimeSheetDate() != null;
    }

    public void initTimSheet() {
        logger.trace("initDataModel: Employee: {}, Date: {}", getEmployee(), getTimeSheetDate());
        if (searchForTimeSheet()) {

            Date start = timeSheetService.getBeginningOfMonth(getTimeSheetDate());
            Date end = timeSheetService.getEndOfMonth(getTimeSheetDate());
            setTimeSheet(timeSheetRepository.findByEmployeeAndDateRelatedIsAfterAndDateRelatedIsBefore(
                    getEmployee(), start, end
            ));

            if (getTimeSheet() != null) {
                logger.trace("{} dayAttendances between {}, {}\n{}",
                        start, end, getTimeSheet().getDayAttendances().size(),
                        getTimeSheet().getDayAttendances());
            }
        }
    }

    public void changeTimeSheetStatus(String status) {
        getTimeSheet().setStatus(status);
    }

    @Transactional
    public void saveChanges() {
        setTimeSheet(getTimeSheetRepository().save(getTimeSheet()));
    }

    public void newInstance() {
        setTimeAttendance(new TimeAttendance("Arrival", new Date()));
    }

    public boolean isManager() {
        setEmployee(employeeRepository.getById(getEmployee().getId()));
        return getEmployee() != null && getEmployee().getManager() != null && getEmployee().getManager().equals(getSessionBean().getAccount().getEmployee());
    }

    public void addTimeAttendance() {
        logger.trace("Time Attendance: {}, {}", getTimeAttendance());
        getTimeAttendance().setEmployee(getEmployee());
        getTimeSheet().addTimeAttendance(getTimeAttendance());
        newInstance();
    }

    public void removeTimeAttendance(TimeAttendance timeAttendance) {
        timeAttendance.setStatus(TimeAttendance.DE_ACTIVE_STATUS);
    }

    public void recycle(TimeAttendance timeAttendance) {
        timeAttendance.setStatus(TimeAttendance.ACTIVE_STATUS);
    }

    public String rowStyleClass(DayAttendance dayAttendance) {
        double extraHours = dayAttendance.getExtraHours();
        if (extraHours < 0) {
            return "lowAttendance";
        } else if (extraHours > 30) {
            return "highAttendance";
        } else {
            return "";
        }
    }
}