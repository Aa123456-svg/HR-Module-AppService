package ir.appservice.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"ir.appservice.model"})
public class AppConfiguration implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(AppConfiguration.class);

    @Autowired
    private Environment env;

    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/index.xhtml");
        registry.addViewController("/index").setViewName("/index.xhtml");
        registry.addViewController("/signin").setViewName("/signin.xhtml");
        registry.addViewController("/dashboard").setViewName("/dashboard.xhtml");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public Pattern emailPattern() {

        return Pattern.compile(env.getProperty("regx.email"));
    }

    @Bean
    public Pattern passwordPattern() {

        return Pattern.compile(env.getProperty("regx.password"));
    }

    @Bean
    public Pattern mobilePattern() {

        return Pattern.compile(env.getProperty("regx.mobile"));
    }

    @Bean
    public Map<String, String> glyphicons() throws IOException {

        Properties glyphicons = new Properties();
        glyphicons.load(getClass().getResourceAsStream("/glyphicon list.properties"));

        return new HashMap(glyphicons);
    }

    @Bean
    public Map<String, String> fontAwesomeIcons() throws IOException {

        Properties fontAwesomeIcons = new Properties();
        fontAwesomeIcons.load(getClass().getResourceAsStream("/font awesome.properties"));

        return new HashMap(fontAwesomeIcons);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}


