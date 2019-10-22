package ir.appservice.model.repository;

import ir.appservice.model.entity.application.Account;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;


@Repository
@ApplicationScope
public interface AccountRepository extends CrudRepository<Account, String> {

    List<Account> findAllByNaturalPersonIsNullAndStatusIs(String status);

    List<Account> findAllByEmployeeIsNullAndStatusIs(String status);

    boolean existsByAccountNameIgnoreCase(String name);

    boolean existsByEmailIgnoreCase(String email);

    int countByAccountName(String name);

    Account findByAccountNameIgnoreCase(String name);

    Account findByMobileNumber(String phoneNumber);

    Account findByEmailIgnoreCase(String phoneNumber);

    boolean existsByAccountNameOrMobileNumberOrEmailAndPassword(String accountName, String mobileNumber, String email, String password);

}
