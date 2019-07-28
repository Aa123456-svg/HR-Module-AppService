package ir.appservice.model.converter;

import ir.appservice.model.entity.application.ui.Menu;
import ir.appservice.model.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Component
public class MenuConverter implements Converter<Menu> {

    private final static Logger logger = LoggerFactory.getLogger(MenuConverter.class);

    private MenuService menuService;

    public MenuConverter(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    public Menu getAsObject(FacesContext context, UIComponent component, String id) {

        logger.trace(String.format("Converting %s to menu...", id));
        try {
            Menu temp = menuService.get(Long.valueOf(id));
            logger.trace(String.format("Menu: %s", temp.getId()));
            return temp;
        } catch (Exception e) {
            logger.warn(String.format("Menu: %s", e.getMessage()));
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Menu menu) {
        logger.trace(String.format("Converting menu to %s ...", menu.getId().toString()));
        try {
            logger.trace(String.format("String: %s", menu.getId().toString()));
            return menu.getId().toString();
        } catch (Exception e) {
            logger.warn(String.format("String: %s", e.getMessage()));
            return "-1";
        }
    }
}
