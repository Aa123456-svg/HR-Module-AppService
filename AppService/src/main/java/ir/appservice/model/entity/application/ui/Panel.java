package ir.appservice.model.entity.application.ui;

import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.entity.application.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Panel extends BaseEntity {

    @Lob
    private String resourceLocation;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Menu menu;

    @ManyToMany(mappedBy = "panels", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Role> roles;
}
