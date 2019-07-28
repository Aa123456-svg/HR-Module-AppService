package ir.appservice.model.repository;

import ir.appservice.model.entity.application.Account;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    boolean existsByAccountNameIgnoreCase(String name);

    boolean existsByEmailIgnoreCase(String email);

    int countByAccountName(String name);

    Account findByAccountNameIgnoreCase(String name);

    Account findByMobileNumber(String phoneNumber);

    Account findByEmailIgnoreCase(String phoneNumber);


}
