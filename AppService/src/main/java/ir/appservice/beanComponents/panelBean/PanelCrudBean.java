package ir.appservice.beanComponents.panelBean;

import ir.appservice.beanComponents.baseBean.BaseCrudBean;
import ir.appservice.model.entity.application.ui.Panel;
import ir.appservice.model.service.PanelService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.File;
import java.util.Map;

@Setter
@Getter
@Component
@SessionScope
public class PanelCrudBean extends BaseCrudBean<Panel> {

    private PanelService panelService;
    private Map<String, File> resources;

    public PanelCrudBean(PanelService panelService) {
        this.panelService = panelService;
        this.resources = panelService.getPanelsResources();

        init();
    }

    public void init() {
        super.init(Panel.class, panelService);
    }

    @Override
    public void add() {
        logger.trace("add");
        Panel temp = panelService.addWithMenu(this.getItem());
        this.getItems().add(0, temp);
        newInstance();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, String.format("%s added", temp.getClass().getSimpleName()), String.format("%s %s was added.", temp.getClass().getSimpleName(), temp.getDisplayName())));
        logger.trace(String.format("%s saved: ID: %s, DisplayName: %s", temp.getClass().getSimpleName(), temp.getId(), temp.getDisplayName()));
    }
}
