package ir.appservice.view.beanComponents.panelBean.timeSheet;

import ir.appservice.model.entity.domain.TimeAttendance;
import ir.appservice.model.service.TimeSheetService;
import ir.appservice.view.beanComponents.BaseBean;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Date;

@Setter
@Getter
@Component
@RequestScope
public class RecordTimeAttendanceBean extends BaseBean {

    private TimeAttendance timeAttendance;
    private TimeSheetService timeSheetService;

    public RecordTimeAttendanceBean(TimeSheetService timeSheetService) {
        this.timeSheetService = timeSheetService;
        newInstance();
    }

    public void newInstance() {
        this.timeAttendance = new TimeAttendance("Arrival", new Date());
    }

    public void recordTimeAttendance() {
        timeSheetService.recordTimeAttendance(timeAttendance);
        newInstance();
    }
}