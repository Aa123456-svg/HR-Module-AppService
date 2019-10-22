package ir.appservice.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@MappedSuperclass
@Getter
@Setter
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseEntity {

    public final static String ACTIVE_STATUS = "ACTIVE_STATUS";
    public final static String DE_ACTIVE_STATUS = "DE_ACTIVE_STATUS";
    public static final Map<String, String> OBJECT_STATUS = new HashMap() {{
        put(ACTIVE_STATUS, "Active");
        put(DE_ACTIVE_STATUS, "De Active");
    }};

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(generator = "uuid2")
    @XmlElement
    @XmlID
    protected String id;
    @XmlElement
    protected String displayName;
    @XmlElement
    protected String description;
    @XmlElement
    protected String status = ACTIVE_STATUS;
    @CreationTimestamp
    @XmlElement
    protected Date registeredDate;
    @UpdateTimestamp
    @XmlElement
    protected Date updatedDate;
    @Temporal(TemporalType.TIMESTAMP)
    @XmlElement
    protected Date deleteDate;

    public BaseEntity(String displayName) {
        setDisplayName(displayName);
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (this == obj) return true;

        if (!(obj instanceof BaseEntity)) return false;

        BaseEntity be = (BaseEntity) obj;

//        && this.displayName.equals(be.getDisplayName()) && this.registeredDate.equals(be.getRegisteredDate())
        boolean equality =
                this.id != null ? this.id.equals(be.getId()) : be.getId() == null;

        return equality;
    }

    public boolean isActive() {
        return this.status.equals(ACTIVE_STATUS);
    }

    public boolean isDeActive() {
        return this.status.equals(DE_ACTIVE_STATUS);
    }

    @Override
    public int hashCode() {
        if (id == null) {
            return -1;
        } else {
            return id.hashCode();
        }
    }

    @Override
    public String toString() {
        return String.format("<Object: %s, ID: %s, DisplayName: %s, Registered Date: %s>",
                this.getClass().getName(),
                this.id, this.displayName, this.registeredDate);
    }
}
