package ir.appservice.model.entity.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ir.appservice.model.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement
public class DayAttendance extends BaseEntity {

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    protected Employee employee;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    protected TimeSheet timeSheet;
    @XmlElement
//    @Temporal(TemporalType.DATE)
    protected Date dateOccurred;

    @JsonManagedReference
    @OneToMany(mappedBy = "dayAttendance", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    @OrderBy("dateOccurred")
    protected List<TimeAttendance> timeAttendances;

    public DayAttendance(Date dateOccurred) {
        setDateOccurred(dateOccurred);
    }

    public void setTimeAttendances(List<TimeAttendance> timeAttendances) {
        this.timeAttendances = timeAttendances;
        if (this.timeAttendances != null) {
            this.timeAttendances.forEach(timeAttendance -> timeAttendance.setDayAttendance(this));
        }
    }

    public double getTotalHours() {
        double total = 0.0;
        List<TimeAttendance> timeAttendances = getActiveTimeAttendances();
        if (timeAttendances == null || timeAttendances.size() < 2 || (timeAttendances.size() % 2 != 0)) {
            return total;
        }
        for (int i = 0; i < timeAttendances.size() - 1; i = i + 2) {
            long diff = timeAttendances.get(i + 1).getDateOccurred().getTime() - timeAttendances.get(i).getDateOccurred().getTime();
            total += diff;
        }
        return Double.valueOf(String.format("%.1f", (total / (60 * 60 * 1000) % 24)));
    }

    public List<TimeAttendance> getActiveTimeAttendances() {
        if (this.timeAttendances != null) {
            return this.timeAttendances.stream().filter(timeAttendance -> timeAttendance.getStatus().equals(TimeAttendance.ACTIVE_STATUS)).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public void addTimeAttendance(TimeAttendance timeAttendance) {
        if (this.timeAttendances == null) {
            this.timeAttendances = new ArrayList<>();
        }
        if (!this.timeAttendances.contains(timeAttendance)) {
            timeAttendance.setDayAttendance(this);
            this.timeAttendances.add(timeAttendance);
        }
        this.timeAttendances.sort(this::comparator);
//        boolean added = false;
//        for (int i = 0; i < this.timeAttendances.size(); i++) {
//            long t = timeAttendance.getDateOccurred().getTime();
//            long t1 = this.timeAttendances.get(i).getDateOccurred().getTime();
//            if (t < t1) {
//                timeAttendance.setDayAttendance(this);
//                this.timeAttendances.add(i, timeAttendance);
//                added = true;
//                break;
//            }
//        }
//        if (!added) {
//            timeAttendance.setDayAttendance(this);
//            this.timeAttendances.add(timeAttendance);
//        }
    }

    public void removeTimeAttendance(TimeAttendance timeAttendance) {
        if (this.timeAttendances != null) {
            this.timeAttendances.remove(timeAttendance);
        }
    }

    public boolean isInDay(TimeAttendance timeAttendance) {
        long t = timeAttendance.getDateOccurred().getTime();
        return getBeginningOfDay().getTime() < t && t <= getEndOfDay().getTime();
    }

    public double getExtraHours() {
        double total = getTotalHours();
        return Double.valueOf(String.format("%.1f", total - 9.0));
    }

    public boolean isDayAttendanceEnough() {
        return getExtraHours() > 0;
    }

    public int comparator(TimeAttendance ta1, TimeAttendance ta2) {
        if (ta1 == null && ta2 == null)
            return 0;
        if (ta1 == null && ta2 != null)
            return -1;
        if (ta1 != null && ta2 == null)
            return 1;
        long t1 = ta1.getDateOccurred().getTime();
        long t2 = ta2.getDateOccurred().getTime();
        if (t1 > t2)
            return 1;
        if (t1 < t2)
            return -1;

        return 0;
    }

    public Date getBeginningOfDay() {
        Calendar temp = Calendar.getInstance();
        temp.setTime(dateOccurred);
        temp.set(Calendar.HOUR_OF_DAY, temp.getActualMinimum(Calendar.HOUR_OF_DAY));
        temp.set(Calendar.MINUTE, temp.getActualMinimum(Calendar.MINUTE));
        temp.set(Calendar.SECOND, temp.getActualMinimum(Calendar.SECOND));

        temp.set(Calendar.MILLISECOND, temp.getActualMinimum(Calendar.MILLISECOND));
        return temp.getTime();
    }

    public Date getEndOfDay() {
        Calendar temp = Calendar.getInstance();
        temp.setTime(dateOccurred);
        temp.set(Calendar.HOUR_OF_DAY, temp.getActualMaximum(Calendar.HOUR_OF_DAY));
        temp.set(Calendar.MINUTE, temp.getActualMaximum(Calendar.MINUTE));
        temp.set(Calendar.SECOND, temp.getActualMaximum(Calendar.SECOND));
        temp.set(Calendar.MILLISECOND, temp.getActualMaximum(Calendar.MILLISECOND));
        return temp.getTime();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) return true;
        if (!(obj instanceof DayAttendance)) return false;
        DayAttendance temp = (DayAttendance) obj;
        long t = temp.getDateOccurred().getTime();
        if (temp.getDateOccurred() == null || this.dateOccurred == null) return false;
        return t > getBeginningOfDay().getTime() && t <= getEndOfDay().getTime();
    }
}
