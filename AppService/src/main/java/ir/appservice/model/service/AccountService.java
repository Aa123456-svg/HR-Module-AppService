package ir.appservice.model.service;

import ir.appservice.model.entity.application.Account;
import ir.appservice.model.entity.application.Role;
import ir.appservice.model.entity.application.ui.Panel;
import ir.appservice.model.repository.AccountRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService extends CrudService<Account> {

    @Autowired
    private Pattern emailPattern;
    @Autowired
    private Pattern mobilePattern;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        super(accountRepository);
        this.accountRepository = accountRepository;
    }

    public boolean existsByAccountNameIgnoreCase(String accountName) {
        return accountRepository.existsByAccountNameIgnoreCase(accountName);
    }

    public Account loadPanels(Account account) {
        List<Panel> panels = new ArrayList<>();
        for (Role role : account.getRoles()) {
            panels.addAll(role.getPanels());
        }
        account.setPanels(panels.stream()
                .sorted((o1, o2) -> o1.getMenu().getPriority() > o2.getMenu().getPriority() ? 1 : o1.getMenu().getPriority() < o2.getMenu().getPriority() ? 0 : -1)
                .distinct().collect(Collectors.toList()));
        return account;
    }

    @Override
    public Account add(Account account) {
        if (account.getPassword() == null || account.getPassword().trim().isEmpty()) {
            account.setPassword(bCryptPasswordEncoder.encode(RandomStringUtils.randomAlphabetic(8)));
        }

        if (accountRepository.existsByAccountNameIgnoreCase(account.getAccountName())) {
            throw new DuplicateKeyException(String.format("Account %s already exist!", account.getAccountName()));
        }

        return super.add(account);
    }

    public Account loginByAny(String loginId, String password) {

        Account account;
        if (emailPattern.matcher(loginId).matches()) {
            logger.trace("Check by email ...");
            account = accountRepository.findByEmailIgnoreCase(loginId);

        } else if (mobilePattern.matcher(loginId).matches()) {
            logger.trace("Check by mobile ...");
            account = accountRepository.findByMobileNumber(loginId);

        } else {
            logger.trace("Check by account name ...");
            account = accountRepository.findByAccountNameIgnoreCase(loginId);
        }

        if (account != null && bCryptPasswordEncoder.matches(password, account.getPassword())) {
            logger.trace("Account: " + account.getDisplayName());

            return loadPanels(account);

        } else {
            logger.warn(String.format("Account '%s' not found!", loginId));
            throw new UsernameNotFoundException("Try again with correct Login ID and Password!");
        }
    }

}