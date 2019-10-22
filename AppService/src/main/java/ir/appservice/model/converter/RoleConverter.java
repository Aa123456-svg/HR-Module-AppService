package ir.appservice.model.converter;

import ir.appservice.model.entity.application.Role;
import ir.appservice.model.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter extends BaseConverter<Role> {

    public RoleConverter(RoleRepository roleRepository) {
        super(Role.class, roleRepository);
    }
}
