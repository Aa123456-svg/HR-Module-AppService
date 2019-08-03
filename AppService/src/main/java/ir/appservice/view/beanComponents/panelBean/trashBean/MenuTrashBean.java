package ir.appservice.view.beanComponents.panelBean.trashBean;

import ir.appservice.model.entity.application.ui.Menu;
import ir.appservice.model.service.MenuService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Getter
@Setter
@Component
@SessionScope
public class MenuTrashBean extends BaseTrashBean<Menu> {

    private MenuService menuService;

    public MenuTrashBean(MenuService menuService) {
        this.menuService = menuService;

        init(menuService, Menu.class);
    }

}
