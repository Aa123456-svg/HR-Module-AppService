package ir.appservice.configuration;

import ir.appservice.beanComponents.SessionBean;
import ir.appservice.model.entity.application.Account;
import ir.appservice.model.entity.application.Role;
import ir.appservice.model.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AppAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(AppAuthenticationProvider.class);

    private SessionBean sessionBean;
    private AccountService accountService;

    public AppAuthenticationProvider(SessionBean sessionBean, AccountService accountService) {
        this.sessionBean = sessionBean;
        this.accountService = accountService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = authentication.getCredentials().toString();

        Account loginAccount = accountService.loginByAny(loginId, password);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        for (Role role : loginAccount.getRoles()) {
            logger.trace("Account Role: " + role.getDisplayName());
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getDisplayName()));
        }

        logger.trace("Account Access Panels: " + loginAccount.getPanels());

        sessionBean.setAccount(loginAccount);

        return new UsernamePasswordAuthenticationToken(loginAccount.getAccountName(), loginAccount.getPassword(), grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}
