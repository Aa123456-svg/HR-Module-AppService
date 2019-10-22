package ir.appservice.model.repository;

import ir.appservice.model.entity.application.Role;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public interface RoleRepository extends CrudRepository<Role, String> {


}
