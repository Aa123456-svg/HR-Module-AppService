package ir.appservice.view.beanComponents.panelBean.trashBean;

import ir.appservice.model.entity.application.ui.Panel;
import ir.appservice.model.service.PanelService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Getter
@Setter
@Component
@SessionScope
public class PanelTrashBean extends BaseTrashBean<Panel> {

    private PanelService panelService;

    public PanelTrashBean(PanelService panelService) {
        this.panelService = panelService;

        init(panelService, Panel.class);
    }

}
