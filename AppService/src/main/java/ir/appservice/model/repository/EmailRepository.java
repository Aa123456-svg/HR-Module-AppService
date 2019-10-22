package ir.appservice.model.repository;

import ir.appservice.model.entity.application.Email;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public interface EmailRepository extends CrudRepository<Email, String> {

}
