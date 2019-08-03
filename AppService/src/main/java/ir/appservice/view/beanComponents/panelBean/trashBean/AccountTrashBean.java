package ir.appservice.view.beanComponents.panelBean.trashBean;

import ir.appservice.model.entity.application.Account;
import ir.appservice.model.service.AccountService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Getter
@Setter
@Component
@SessionScope
public class AccountTrashBean extends BaseTrashBean<Account> {

    private AccountService accountService;

    public AccountTrashBean(AccountService accountService) {
        this.accountService = accountService;

        init(accountService, Account.class);
    }

}
