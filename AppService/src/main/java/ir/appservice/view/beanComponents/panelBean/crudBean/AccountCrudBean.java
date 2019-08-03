package ir.appservice.view.beanComponents.panelBean.crudBean;

import ir.appservice.view.beanComponents.baseBean.BaseCrudBean;
import ir.appservice.model.entity.application.Account;
import ir.appservice.model.service.AccountService;
import ir.appservice.model.service.DocumentService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Setter
@Getter
@Component
@SessionScope
public class AccountCrudBean extends BaseCrudBean<Account> {

    private AccountService accountService;
    private DocumentService documentService;

    public AccountCrudBean(AccountService accountService, DocumentService documentService) {
        this.accountService = accountService;
        this.documentService = documentService;

        init(Account.class, accountService);
    }

    public void uploadAvatar(FileUploadEvent event) {
        this.getItem().setAvatar(documentService.uploadDocument(event));
    }
}
