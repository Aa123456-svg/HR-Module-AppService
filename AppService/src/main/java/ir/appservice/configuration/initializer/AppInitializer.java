package ir.appservice.configuration.initializer;

import ir.appservice.model.entity.application.Account;
import ir.appservice.model.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@ConfigurationProperties(prefix = "app.initializer")
public class AppInitializer implements InitializingBean, DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(AppInitializer.class);

    private Account account;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public void afterPropertiesSet() {
        logger.trace(this.getClass().getName());
        initializeApplication();
    }

    private void initializeApplication() {
        if (account != null && !accountService.existsByAccountNameIgnoreCase(account.getAccountName())) {
            account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
            accountService.add(account);
            logger.trace(String.format("New account %s was added.", account.getAccountName()));
        }
    }

    @Override
    public void destroy() {

    }
}
