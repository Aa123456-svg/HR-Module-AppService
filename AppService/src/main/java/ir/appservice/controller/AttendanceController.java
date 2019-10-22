package ir.appservice.controller;

import ir.appservice.model.entity.domain.Employee;
import ir.appservice.model.entity.domain.TimeAttendance;
import ir.appservice.model.repository.EmployeeRepository;
import ir.appservice.model.service.TimeSheetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;


@RestController("/attendance")
@Controller
public class AttendanceController extends BaseController {

    private TimeSheetService timeSheetService;
    private EmployeeRepository employeeRepository;

    public AttendanceController(TimeSheetService timeSheetService, EmployeeRepository employeeRepository) {
        this.timeSheetService = timeSheetService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<TimeAttendance> add(@RequestParam String employeeId, @RequestParam String date,
                                              @RequestParam(
                                                      "displayName") String displayName) {

        try {
            if (displayName == null) {
                displayName = "Arrival";
            }
            Date temp = new Date();
            if (date != null) {
                temp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").parse(date);
            }

            Employee employee = employeeRepository.getById(employeeId);
            TimeAttendance timeAttendance = new TimeAttendance(displayName, employee, temp);
            timeSheetService.recordTimeAttendance(timeAttendance);
            return ResponseEntity.ok(timeAttendance);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
