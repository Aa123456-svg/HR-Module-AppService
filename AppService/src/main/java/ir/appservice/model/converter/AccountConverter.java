package ir.appservice.model.converter;

import ir.appservice.model.entity.application.Account;
import ir.appservice.model.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Component
public class AccountConverter implements Converter<Account> {

    private final static Logger logger = LoggerFactory.getLogger(AccountConverter.class);

    private AccountService accountService;

    public AccountConverter(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Account getAsObject(FacesContext context, UIComponent component, String id) {

        logger.trace(String.format("Converting %s to account...", id));
        try {
            Account temp = accountService.get(Long.valueOf(id));
            logger.trace(String.format("Account: %s", temp.getId()));
            return temp;
        } catch (Exception e) {
            logger.warn(String.format("Account: %s", e.getMessage()));
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Account account) {
        logger.trace(String.format("Converting account to %s ...", account.getId().toString()));
        try {
            logger.trace(String.format("String: %s", account.getId().toString()));
            return account.getId().toString();
        } catch (Exception e) {
            logger.warn(String.format("String: %s", e.getMessage()));
            return "-1";
        }
    }
}
