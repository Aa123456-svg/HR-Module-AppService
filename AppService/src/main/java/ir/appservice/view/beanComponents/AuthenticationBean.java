package ir.appservice.view.beanComponents;

import ir.appservice.view.beanComponents.baseBean.BaseBean;
import ir.appservice.configuration.AppAuthenticationProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.faces.context.FacesContext;

@Setter
@Getter
@Component
@SessionScope
public class AuthenticationBean extends BaseBean {

    private String loginId = "";
    private String password = "";
    private AppAuthenticationProvider appAuthenticationProvider;

    public AuthenticationBean(AppAuthenticationProvider appAuthenticationProvider) {
        this.appAuthenticationProvider = appAuthenticationProvider;

        logger.trace("Initializing");
    }

    public void authenticate() {
        logger.trace(String.format("Authenticating: %s, password: %s", loginId, password));

        Authentication auth;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);
            auth = appAuthenticationProvider.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(auth);

            logger.trace(String.format("Authenticated %s and redirecting ...", loginId));
            FacesContext.getCurrentInstance().getExternalContext().redirect("/dashboard");

            info("Welcome :)", "Welcome to home...", null);

        } catch (AuthenticationException bce) {
            error("You are not authenticated!", bce.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            fatal("Server Error!", e.getMessage(), null);
        }
    }


}