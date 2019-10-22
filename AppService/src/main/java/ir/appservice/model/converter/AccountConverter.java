package ir.appservice.model.converter;

import ir.appservice.model.entity.application.Account;
import ir.appservice.model.repository.AccountRepository;
import org.springframework.stereotype.Component;

@Component
public class AccountConverter extends BaseConverter<Account> {

    public AccountConverter(AccountRepository accountRepository) {
        super(Account.class, accountRepository);
    }
}
