package ir.appservice.model.service;

import ir.appservice.model.entity.application.Role;
import ir.appservice.model.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleService extends CrudService<Role> {

    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }

    public boolean existsByDisplayNameIgnoreCase(String displayName) {
        return roleRepository.existsByDisplayNameIgnoreCase(displayName);
    }
}
