package ir.appservice.view.beanComponents;

import ir.appservice.view.beanComponents.baseBean.BaseBean;
import ir.appservice.model.entity.application.ui.Panel;
import ir.appservice.model.service.PanelService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import javax.faces.context.FacesContext;
import java.util.Map;

@Setter
@Getter
@Component
@SessionScope
public class PanelHandler extends BaseBean {

    private SessionBean sessionBean;
    private PanelService panelService;
    private Panel activePanel = null;

    public PanelHandler(SessionBean sessionBean, PanelService panelService) {
        this.sessionBean = sessionBean;
        this.panelService = panelService;

        init();
    }

    public void init() {
        activePanel = panelService.get((long) 1);
        logger.trace("Init Active Panel: " + activePanel.getDisplayName());
    }

    public void openPanel(long id) {
        Panel panel = panelService.get(id);

        if (sessionBean.isAuthorized(panel)) {
            activePanel = panel;
        } else {
            error("Access Denied!", "You have not allowed to access this panel.", null);
            activePanel = panelService.get((long) 1);
        }

        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        for (Map.Entry<String, Object> panelBean : sessionMap.entrySet()) {
            if (panelBean.getValue().toString().startsWith("ir.appservice.view.beanComponents.panelBean")) {
                sessionMap.remove(panelBean.getKey());
                logger.trace(String.format("Bean \"%s\"=\"%s\" removed.", panelBean.getKey(), panelBean.getValue()));
            }
        }
        logger.trace(String.format("Active Panel: %s", activePanel.getDisplayName()));
    }


}
