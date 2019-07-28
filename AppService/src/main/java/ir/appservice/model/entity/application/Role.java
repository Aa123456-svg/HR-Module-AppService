package ir.appservice.model.entity.application;

import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.entity.application.ui.Panel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Role extends BaseEntity {

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    private List<Account> accounts;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Panel> panels;

//    @Override
//    public boolean equals(Object obj) {
//        return obj != null
//                && this.getClass() == obj.getClass()
//                && this.getDisplayName().equalsIgnoreCase(((BaseEntity) obj).getDisplayName());
//    }
//
//    @Override
//    public int hashCode() {
//        return (int) ((17 * 31) + id);
//    }
}
