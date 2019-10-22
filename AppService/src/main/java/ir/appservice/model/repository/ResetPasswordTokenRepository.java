package ir.appservice.model.repository;

import ir.appservice.model.entity.application.ResetPasswordToken;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public interface ResetPasswordTokenRepository extends CrudRepository<ResetPasswordToken, String> {

    ResetPasswordToken findByToken(String token);

    boolean existsByToken(String token);

    int countByToken(String token);

}
