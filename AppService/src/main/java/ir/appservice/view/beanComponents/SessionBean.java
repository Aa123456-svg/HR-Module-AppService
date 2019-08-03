package ir.appservice.view.beanComponents;

import com.github.adminfaces.template.config.AdminConfig;
import ir.appservice.view.beanComponents.baseBean.BaseBean;
import ir.appservice.model.entity.application.Account;
import ir.appservice.model.entity.application.Role;
import ir.appservice.model.entity.application.ui.Panel;
import ir.appservice.model.entity.domain.Document;
import ir.appservice.model.service.AccountService;
import ir.appservice.model.service.DocumentService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Setter
@Getter
@Component
@SessionScope
public class SessionBean extends BaseBean {

    private HttpSession httpSession;
    private AccountService accountService;
    private DocumentService documentService;
    private AdminConfig adminConfig;
    private SessionRegistry sessionRegistry;

    private Account account = null;

    public SessionBean(HttpSession httpSession, AccountService accountService, DocumentService documentService, AdminConfig adminConfig, SessionRegistry sessionRegistry) {
        this.httpSession = httpSession;
        this.accountService = accountService;
        this.documentService = documentService;
        this.adminConfig = adminConfig;
        this.sessionRegistry = sessionRegistry;

        init();
    }

    public void init() {
        logger.trace("SessionBean account: " + account);
    }

    public void redirectAnonymousUsers(ComponentSystemEvent event) throws IOException {
        if (!isAuthenticated()) {
            logger.trace(event.getComponent().getId() + " => redirecting to index ..." + adminConfig.getIndexPage());
            FacesContext.getCurrentInstance().getExternalContext().redirect(adminConfig.getIndexPage());
        }
    }

    public boolean isAuthenticated() {
//        logger.trace("SessionBean isAuthenticated: " + account);

        return account != null;
    }

    public boolean isAuthorized(Object object) {
        boolean isAuthorized = false;
        if (object instanceof Panel) {
            isAuthorized = isAuthenticated() && account.getPanels().contains(object);
            logger.trace(String.format("Panel %s isAuthorized: %s", object, isAuthorized));
        } else if (object instanceof Role && object != null) {
            isAuthorized = isAuthenticated() && account.getRoles().contains(object);
            logger.trace(String.format("Role %s isAuthorized: %s", object, isAuthorized));
        } else {
            logger.trace("SessionBean isAuthorized: " + isAuthorized);
        }

        return isAuthorized;
    }

    public void edit() {
        try {
            account = accountService.edit(account);
            info("Update Profile Successful", "...", null);
        } catch (Exception e) {
            e.printStackTrace();
            fatal("Error!", e.getLocalizedMessage(), null);
        }
    }

    public void reload() {
        try {
            account = accountService.get(account.getId());
            logger.trace("Reload Profile Successful");
        } catch (Exception e) {
            e.printStackTrace();
            fatal("Error!", e.getLocalizedMessage(), null);
        }
    }

    public void uploadAvatar(FileUploadEvent event) {

        Document document = documentService.uploadDocument(event);
        account.setAvatar(document);
        account = accountService.edit(account);

        info("Successful file upload", document.getDisplayName() + " is uploaded. Size (KB): " + document.getSize() / 1024f, null);
    }

    public void showSessions() {
        logger.trace("All Sessions:");
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            SessionInformation sessionInformation = (SessionInformation) principal;
            logger.warn(String.format("Principal: %s, Session Id: %s, Last request data: %s, Expired: %s", sessionInformation.getPrincipal(), sessionInformation.getSessionId(), sessionInformation.getLastRequest(), sessionInformation.isExpired()));
        }

    }

}