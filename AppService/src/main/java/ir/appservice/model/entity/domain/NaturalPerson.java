package ir.appservice.model.entity.domain;

import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.entity.application.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class NaturalPerson extends BaseEntity {

    private String firstName;
    private String lastName;

    @Size(min = 10, message = "Length is not correct!")
    private String mobileNumber;
    private Date birthDate;

    @OneToOne(mappedBy = "naturalPerson", cascade = CascadeType.ALL)
    private Account account;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Document avatar;
}
