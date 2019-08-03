package ir.appservice.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String displayName;
    @CreationTimestamp
    protected Date registeredDate;
    @UpdateTimestamp
    protected Date updatedDate;
    @Temporal(TemporalType.TIMESTAMP)
    protected Date deleteDate;

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && this.getClass() == obj.getClass()
                && this.getId() == ((BaseEntity) obj).getId();
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return (int) ((17 * 31) + -1);
        } else {
            return (int) ((17 * 31) + id);
        }
    }

    @Override
    public String toString() {
        return String.format("<Object: %s, ID: %s, DisplayName: %s>", this.getClass().getSimpleName(), this.id, this.displayName);
    }
}
