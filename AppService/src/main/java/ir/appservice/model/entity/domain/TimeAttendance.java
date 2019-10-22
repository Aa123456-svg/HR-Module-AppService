package ir.appservice.model.entity.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ir.appservice.model.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement
public class TimeAttendance extends BaseEntity {

    public final static String EDITED_STATUS = "EDITED_STATUS";

    static {
        OBJECT_STATUS.put(EDITED_STATUS, "Edited");
    }

    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    protected Employee employee;


    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    protected DayAttendance dayAttendance;

    @XmlElement
//    @Temporal(TemporalType.TIME)
    protected Date dateOccurred;

    public TimeAttendance(String displayName) {
        super(displayName);
    }

    public TimeAttendance(String displayName, Date dateOccurred) {
        super(displayName);
        setDateOccurred(dateOccurred);
    }

    public TimeAttendance(String displayName, Employee employee, Date dateOccurred) {
        super(displayName);
        setEmployee(employee);
        setDateOccurred(dateOccurred);
    }

    public TimeAttendance(Employee employee, Date dateOccurred) {
        setEmployee(employee);
        setDateOccurred(dateOccurred);
    }

    public boolean isEdited() {
        return this.status.equals(EDITED_STATUS);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) return true;
        if (!(obj instanceof TimeAttendance)) return false;
        TimeAttendance temp = (TimeAttendance) obj;
        if (temp.getDateOccurred() == null || this.dateOccurred == null) return false;
        return temp.getDateOccurred().getTime() == this.dateOccurred.getTime();
    }

    @Override
    public String toString() {
        return String.format("<Object: %s, ID: %s, DisplayName: %s, Date Occurred: %s, Employee: " +
                        "%s>",
                this.getClass().getName(),
                this.id, this.displayName, this.dateOccurred, this.employee);
    }
}
