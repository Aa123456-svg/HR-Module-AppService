package ir.appservice.model.converter;

import ir.appservice.model.entity.application.ui.Menu;
import ir.appservice.model.repository.MenuRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuConverter extends BaseConverter<Menu> {

    public MenuConverter(MenuRepository menuRepository) {
        super(Menu.class, menuRepository);
    }
}
