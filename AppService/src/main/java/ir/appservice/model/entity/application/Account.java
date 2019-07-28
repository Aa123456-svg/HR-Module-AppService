package ir.appservice.model.entity.application;

import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.entity.application.ui.Panel;
import ir.appservice.model.entity.domain.Document;
import ir.appservice.model.entity.domain.NaturalPerson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Account extends BaseEntity {

    @Column(unique = true)
    @NotNull(message = "Account Name can't be null!")
    private String accountName;
    @NotNull(message = "Email can't be null!")
    private String email;
    private String mobileNumber;
    @Size(min = 8, message = "At least should be 8 characters!")
    @NotNull(message = "Password can't be null!")
    private String password;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private NaturalPerson naturalPerson;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Document avatar;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Role> roles;

    @Transient
    private List<Panel> panels;

}
