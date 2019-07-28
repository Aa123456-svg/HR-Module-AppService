package ir.appservice.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;

@Configuration
@EnableWebSecurity
public class AppSecurity extends WebSecurityConfigurerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AppSecurity.class);

    private SessionRegistry sessionRegistry;
    private AppAuthenticationProvider appAuthenticationProvider;

    public AppSecurity(SessionRegistry sessionRegistry, AppAuthenticationProvider appAuthenticationProvider) {
        this.sessionRegistry = sessionRegistry;
        this.appAuthenticationProvider = appAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(appAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
                .maximumSessions(100)
                .expiredUrl("/index")
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry);

        http
                .authorizeRequests()
                .antMatchers("/javax.faces.resource/**", "/", "/index", "/index.xhtml", "/signout", "/error", "/resetPassword/*").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .disable()

                .logout()
                .logoutUrl("/signout")
                .logoutSuccessUrl("/")
                .permitAll()

                .and()
                .csrf().disable();

//        http.authorizeRequests().antMatchers("/home/**/*").hasRole("USER");
//        http.authorizeRequests().antMatchers("/admin/**/*").hasRole("ADMINISTRATOR");
    }


}
