package ir.appservice.model.entity.application.ui;

import ir.appservice.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor()
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Menu extends BaseEntity {

    public final static String SIDE_BAR_MENU = "SIDE_BAR_MENU";
    public final static String IN_PAGE_MENU = "IN_PAGE_MENU";
    public final static String CATEGORY_MENU = "CATEGORY_MENU";
    public static final Map<String, String> MENU_TYPES;

    static {
        MENU_TYPES = new HashMap();
        MENU_TYPES.put(SIDE_BAR_MENU, "Side Bar Menu");
        MENU_TYPES.put(IN_PAGE_MENU, "In Page Menu");
        MENU_TYPES.put(CATEGORY_MENU, "Category Menu");
    }

    private int priority;

    private String type;
    private String icon;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Menu parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Menu> subMenus;

    @OneToOne(mappedBy = "menu", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Panel panel;
}
