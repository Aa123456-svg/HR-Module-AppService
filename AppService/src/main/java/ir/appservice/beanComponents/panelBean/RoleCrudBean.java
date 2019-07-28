package ir.appservice.beanComponents.panelBean;

import ir.appservice.beanComponents.baseBean.BaseCrudBean;
import ir.appservice.model.entity.application.Role;
import ir.appservice.model.service.RoleService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Setter
@Getter
@Component
@SessionScope
public class RoleCrudBean extends BaseCrudBean<Role> {

    private RoleService roleService;

    public RoleCrudBean(RoleService roleService) {
        this.roleService = roleService;

        init();
    }

    public void init() {
        super.init(Role.class, roleService);
    }
}
