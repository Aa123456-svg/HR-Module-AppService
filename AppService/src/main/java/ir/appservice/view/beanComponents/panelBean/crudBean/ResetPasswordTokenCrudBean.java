package ir.appservice.view.beanComponents.panelBean.crudBean;

import ir.appservice.model.entity.application.ResetPasswordToken;
import ir.appservice.model.service.ResetPasswordTokenService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Setter
@Getter
@Component
@SessionScope
public class ResetPasswordTokenCrudBean extends BaseLazyCrudBean<ResetPasswordToken> {

    public ResetPasswordTokenCrudBean(ResetPasswordTokenService resetPasswordTokenService) {
        super(resetPasswordTokenService, ResetPasswordToken.class);
    }

}
