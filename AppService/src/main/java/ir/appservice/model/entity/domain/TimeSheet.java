package ir.appservice.model.entity.domain;

import ir.appservice.model.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement
public class TimeSheet extends BaseEntity {

    public final static String APPROVE_STATUS = "APPROVE_STATUS";
    public final static String REJECT_STATUS = "REJECT_STATUS";

    static {
        OBJECT_STATUS.put(APPROVE_STATUS, "Approve");
        OBJECT_STATUS.put(REJECT_STATUS, "Reject");
    }

    @OneToMany(mappedBy = "timeSheet", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    @OrderBy("dateOccurred")
    protected List<DayAttendance> dayAttendances;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    protected Employee employee;

    @XmlElement
//    @Temporal(TemporalType.DATE)
    protected Date dateRelated;

    public TimeSheet(Employee employee, Date dateRelated) {
        this.employee = employee;
        this.dateRelated = dateRelated;
    }

    public void setDayAttendances(List<DayAttendance> dayAttendances) {
        this.dayAttendances = dayAttendances;
        if (this.dayAttendances != null) {
            this.dayAttendances.forEach(dayAttendance -> dayAttendance.setTimeSheet(this));
        }
    }

    public int compare(DayAttendance d1, DayAttendance d2) {
        if (d1 == null && d2 == null)
            return 0;
        if (d1 == null && d2 != null)
            return -1;
        if (d1 != null && d2 == null)
            return 1;
        long t1 = d1.getDateOccurred().getTime();
        long t2 = d2.getDateOccurred().getTime();
        if (t1 > t2)
            return 1;
        if (t1 < t2)
            return -1;

        return 0;
    }

    public void addTimeAttendance(TimeAttendance timeAttendance) {
        if (this.dayAttendances != null) {
            for (DayAttendance dayAttendance : this.dayAttendances) {
                if (dayAttendance.isInDay(timeAttendance)) {
                    dayAttendance.addTimeAttendance(timeAttendance);
                    return;
                }
            }

        }
        DayAttendance dTemp = new DayAttendance(timeAttendance.getDateOccurred());
        dTemp.addTimeAttendance(timeAttendance);
        dTemp.setTimeSheet(this);
        this.dayAttendances.add(dTemp);
        this.dayAttendances.sort(this::compare);
    }

    public void removeTimeAttendance(TimeAttendance timeAttendance) {
        DayAttendance dayAttendance = timeAttendance.getDayAttendance();
        if (dayAttendance != null) {
            dayAttendance.removeTimeAttendance(timeAttendance);
            if (dayAttendance.getTimeAttendances() != null && dayAttendance.getTimeAttendances().isEmpty()) {
                this.dayAttendances.remove(dayAttendance);
            }
        }
    }

    public double totalHours() {
        double total = 0.0;
        for (DayAttendance dayAttendance : this.dayAttendances) {
            total += dayAttendance.getTotalHours();
        }
        return Double.valueOf(String.format("%.2f", total));
    }

    public double totalExtraHours() {
        double total = 0.0;
        for (DayAttendance dayAttendance : this.dayAttendances) {
            total += dayAttendance.getExtraHours();
        }
        return Double.valueOf(String.format("%.2f", total));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) return true;
        if (!(obj instanceof TimeSheet)) return false;
        TimeSheet temp = (TimeSheet) obj;
        long t = temp.getDateRelated().getTime();
        return t > getBeginningOfMonth().getTime() && t <= getEndOfMonth().getTime();
    }

    public Date getBeginningOfMonth() {
        Calendar temp = Calendar.getInstance();
        temp.setTime(dateRelated);
        temp.set(Calendar.DAY_OF_MONTH, temp.getActualMinimum(Calendar.DAY_OF_MONTH));
        temp.set(Calendar.HOUR_OF_DAY, temp.getActualMinimum(Calendar.HOUR_OF_DAY));
        temp.set(Calendar.MINUTE, temp.getActualMinimum(Calendar.MINUTE));
        temp.set(Calendar.SECOND, temp.getActualMinimum(Calendar.SECOND));
        temp.set(Calendar.MILLISECOND, temp.getActualMinimum(Calendar.MILLISECOND));
        return temp.getTime();
    }

    public Date getEndOfMonth() {
        Calendar temp = Calendar.getInstance();
        temp.setTime(dateRelated);
        temp.set(Calendar.DAY_OF_MONTH, temp.getActualMaximum(Calendar.DAY_OF_MONTH));
        temp.set(Calendar.HOUR_OF_DAY, temp.getActualMaximum(Calendar.HOUR_OF_DAY));
        temp.set(Calendar.MINUTE, temp.getActualMaximum(Calendar.MINUTE));
        temp.set(Calendar.SECOND, temp.getActualMaximum(Calendar.SECOND));
        temp.set(Calendar.MILLISECOND, temp.getActualMaximum(Calendar.MILLISECOND));
        return temp.getTime();
    }

    @Override
    public String toString() {
        return String.format("<Object: %s, ID: %s, DisplayName: %s, Date Related: %s, Employee: " +
                        "%s>",
                this.getClass().getName(),
                this.id, this.displayName, this.dateRelated, this.employee);
    }
}
