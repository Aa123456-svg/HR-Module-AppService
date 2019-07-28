package ir.appservice.beanComponents.panelBean;

import ir.appservice.beanComponents.baseBean.BaseCrudBean;
import ir.appservice.model.entity.application.ui.Menu;
import ir.appservice.model.service.MenuService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Setter
@Getter
@Component
@SessionScope
public class MenuCrudBean extends BaseCrudBean<Menu> {

    private MenuService menuService;

    public MenuCrudBean(MenuService menuService) {
        this.menuService = menuService;

        init();
    }

    public void init() {
        super.init(Menu.class, menuService);
    }

}
