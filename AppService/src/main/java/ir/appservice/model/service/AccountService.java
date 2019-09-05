package ir.appservice.model.service;

import ir.appservice.configuration.exception.DuplicateEntityException;
import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.entity.application.Account;
import ir.appservice.model.entity.application.Role;
import ir.appservice.model.entity.application.ui.Menu;
import ir.appservice.model.entity.application.ui.Panel;
import ir.appservice.model.repository.AccountRepository;
import ir.appservice.view.beanComponents.SessionBean;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@ApplicationScope
public class AccountService extends CrudService<Account> implements AuthenticationProvider {

    @Autowired
    private SessionBean sessionBean;
    @Autowired
    private Pattern emailPattern;
    @Autowired
    private Pattern mobilePattern;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AccountService(AccountRepository accountRepository) {
        super(accountRepository, Account.class);
    }

    public boolean existsByAccountNameIgnoreCase(String accountName) {
        return getAccountRepository().existsByAccountNameIgnoreCase(accountName);
    }

    public boolean isAuthorized(Account account, BaseEntity object) {
        List<Role> roles = getAccountRepository().getById(account.getId()).getRoles();
        if (object instanceof Panel && object != null) {
            for (Role role : roles) {
                if (role.getPanels().contains(object)) {
                    logger.trace(String.format("%s authorized to: %s", account, object));
                    return true;
                }
            }
        } else if (object instanceof Role && object != null) {
            account.getRoles().contains(object);
            logger.trace(String.format("%s authorized to: %s", account, object));
        }

        logger.trace(String.format("%s not authorized to: %s", account, object));
        return false;
    }

    public Account getWithRolesAndPanelsAndMenus(String id) {
        Account account = getAccountRepository().getOne(id);

        Set<Menu> rootAccountMenus = new HashSet();
        Set<Menu> availableAccountMenus = new HashSet();
        Set<Panel> accountPanels = new HashSet();

        account.getRoles().forEach(role -> role.getPanels().stream().forEach(panel -> {
            Menu temp = panel.getMenu();
            while (temp != null) {
                if (temp.getParent() == null) {
                    rootAccountMenus.add(temp);
                } else {
                    availableAccountMenus.add(temp);
                }

                temp = temp.getParent();
            }
            accountPanels.add(panel);
        }));

        account.setPanels(accountPanels.stream().sorted((o1, o2) -> {
                    if (o1.getMenu() == null) {
                        return -1;
                    }
                    if (o2.getMenu() == null) {
                        return 1;
                    }
                    return o1.getMenu().getPriority() > o2.getMenu().getPriority() ? 1 :
                            o1.getMenu().getPriority() < o2.getMenu().getPriority() ? -1 : 0;
                }).collect(Collectors.toList())
        );

        rootAccountMenus.forEach(menu -> addChildMenu(menu, availableAccountMenus));

        account.setAccessMenus(rootAccountMenus.stream().sorted(Comparator.comparingInt(Menu::getPriority)).collect(Collectors.toList()));

        account.getAccessMenus().forEach(menu -> printMenus(menu, 0));

        logger.trace("Account Roles: {}", account.getRoles());
        logger.trace("Account Panels: {}", account.getPanels());
        logger.trace("Root Account Menus: {}", account.getAccessMenus());
        logger.trace("All Account Menus: {}", availableAccountMenus);

        return account;
    }

    private void addChildMenu(Menu p, Set<Menu> availableChild) {
        Set<Menu> removeItems = new HashSet();
        p.setSubMenus(new ArrayList());
        availableChild.forEach(m -> {
            Menu parent = m.getParent();
            if (p.equals(parent)) {
                logger.trace("{} is parent of {}", p, m);
                p.getSubMenus().add(m);
                removeItems.add(m);
            }
        });

        logger.trace("removeItems: {}", removeItems);
        availableChild.removeAll(removeItems);
        logger.trace("availableChild: {}", availableChild);

        if (!availableChild.isEmpty()) {
            p.getSubMenus().forEach(subMenu -> addChildMenu(subMenu, availableChild));
        }

        p.setSubMenus(p.getSubMenus().stream().sorted(Comparator.comparingInt(Menu::getPriority)).collect(Collectors.toList()));

    }

    private void printMenus(Menu menu, int level) {
        logger.trace("{}: {}", StringUtils.repeat("\t\t", level), menu);
        menu.getSubMenus().forEach(sub -> printMenus(sub, level + 1));
    }

    public AccountRepository getAccountRepository() {
        return (AccountRepository) this.crudRepository;
    }

    @Override
    public Account add(Account account) {
        if (account.getPassword() == null || account.getPassword().trim().isEmpty()) {
            account.setPassword(bCryptPasswordEncoder.encode(RandomStringUtils.randomAlphabetic(8)));
        } else {
            account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        }

        if (getAccountRepository().existsByAccountNameIgnoreCase(account.getAccountName())) {
            throw new DuplicateEntityException(String.format("Account %s already exist!", account.getAccountName()));
        }

        return super.add(account);
    }

    public Account loginByAny(String loginId, String password) throws UsernameNotFoundException {

        Account account;
        if (emailPattern.matcher(loginId).matches()) {
            account = getAccountRepository().findByEmailIgnoreCase(loginId);
            logger.info("Check by email ... {}", account);

        } else if (mobilePattern.matcher(loginId).matches()) {
            account = getAccountRepository().findByMobileNumber(loginId);
            logger.info("Check by mobile ... {}", account);

        } else {
            account = getAccountRepository().findByAccountNameIgnoreCase(loginId);
            logger.info("Check by account name ... {}", account);
        }

        if (account != null) {
// && bCryptPasswordEncoder.matches(password, account.getPassword())
            logger.info("Authenticated account: " + account);

            return getWithRolesAndPanelsAndMenus(account.getId());

        } else {
            logger.warn("Account '{}' not found!", loginId);
            throw new UsernameNotFoundException("Try again with correct Login ID and Password!");
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = authentication.getCredentials().toString();

        Account loginAccount = loginByAny(loginId, password);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        loginAccount.getRoles().forEach(role -> {
            logger.trace("Account Role: " + role.getDisplayName());
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getDisplayName()));
        });

        sessionBean.setAccount(loginAccount);

        return new UsernamePasswordAuthenticationToken(loginAccount.getAccountName(), loginAccount.getPassword(), grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}