package ir.appservice.view.beanComponents.panelBean.crudBean;

import ir.appservice.model.entity.application.Email;
import ir.appservice.model.service.EMailService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Setter
@Getter
@Component
@SessionScope
public class EmailCrudBean extends BaseLazyCrudBean<Email> {

    public EmailCrudBean(EMailService eMailService) {
        super(eMailService, Email.class);
    }
}
