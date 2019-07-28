package ir.appservice.model.converter;

import ir.appservice.model.entity.application.Role;
import ir.appservice.model.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Component
public class RoleConverter implements Converter<Role> {

    private final static Logger logger = LoggerFactory.getLogger(RoleConverter.class);

    private RoleService roleService;

    public RoleConverter(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Role getAsObject(FacesContext context, UIComponent component, String id) {

        logger.trace(String.format("Converting %s to role...", id));
        try {
            Role temp = roleService.get(Long.valueOf(id));
            logger.trace(String.format("Role: %s", temp.getId()));
            return temp;
        } catch (Exception e) {
            logger.warn(String.format("Role: %s", e.getMessage()));
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Role role) {
        logger.trace(String.format("Converting role to %s ...", role.getId().toString()));
        try {
            logger.trace(String.format("String: %s", role.getId().toString()));
            return role.getId().toString();
        } catch (Exception e) {
            logger.warn(String.format("String: %s", e.getMessage()));
            return "-1";
        }
    }
}
