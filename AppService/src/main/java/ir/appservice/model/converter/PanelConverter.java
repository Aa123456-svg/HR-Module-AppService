package ir.appservice.model.converter;

import ir.appservice.model.entity.application.ui.Panel;
import ir.appservice.model.service.PanelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Component
public class PanelConverter implements Converter<Panel> {

    private final static Logger logger = LoggerFactory.getLogger(PanelConverter.class);

    private PanelService panelService;

    public PanelConverter(PanelService panelService) {
        this.panelService = panelService;
    }

    @Override
    public Panel getAsObject(FacesContext context, UIComponent component, String id) {

        logger.trace(String.format("Converting %s to panel...", id));
        try {
            Panel temp = panelService.get(Long.valueOf(id));
            logger.trace(String.format("Panel: %s", temp.getId()));
            return temp;
        } catch (Exception e) {
            logger.warn(String.format("Panel: %s", e.getMessage()));
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Panel panel) {
        logger.trace(String.format("Converting panel to %s ...", panel.getId().toString()));
        try {
            logger.trace(String.format("String: %s", panel.getId().toString()));
            return panel.getId().toString();
        } catch (Exception e) {
            logger.warn(String.format("String: %s", e.getMessage()));
            return "-1";
        }
    }
}
