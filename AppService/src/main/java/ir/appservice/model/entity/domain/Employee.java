package ir.appservice.model.entity.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.entity.application.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement
public class Employee extends BaseEntity {

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    protected Account account;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    protected NaturalPerson naturalPerson;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    protected Employee manager;

    @OneToMany(mappedBy = "manager", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    protected List<Employee> reporters;

    @JsonManagedReference
    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    protected List<TimeAttendance> timeAttendances;

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    protected List<DayAttendance> dayAttendances;

    @OneToMany(mappedBy = "employee", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @XmlElement
    @XmlIDREF
    protected List<TimeSheet> timeSheets;

    public Employee(String displayName) {
        super(displayName);
    }

    public Employee(String displayName, Account account, NaturalPerson naturalPerson, Employee manager) {
        super(displayName);
        setAccount(account);
        setNaturalPerson(naturalPerson);
        setManager(manager);
    }

    public void setTimeAttendances(List<TimeAttendance> timeAttendances) {
        this.timeAttendances = timeAttendances;
        if (this.timeAttendances == null) {
            this.timeAttendances = new ArrayList<>();
        }
        this.timeAttendances.stream().forEach(ta -> ta.setEmployee(this));
    }

    public void setReporters(List<Employee> reporters) {
        this.reporters = reporters;
        if (this.reporters == null) {
            this.reporters = new ArrayList<>();
        }
        this.reporters.stream().forEach(ta -> ta.setManager(this));
    }

    public void setTimeSheets(List<TimeSheet> timeSheets) {
        this.timeSheets = timeSheets;
        if (this.timeSheets == null) {
            this.timeSheets = new ArrayList<>();
        }
        this.timeSheets.stream().forEach(ts -> ts.setEmployee(this));
    }

    public void setDayAttendances(List<DayAttendance> dayAttendances) {
        this.dayAttendances = dayAttendances;
        if (this.dayAttendances == null) {
            this.dayAttendances = new ArrayList<>();
        }
        this.dayAttendances.stream().forEach(da -> da.setEmployee(this));
    }

}
