package ir.appservice.model.converter;

import ir.appservice.model.entity.application.ui.Panel;
import ir.appservice.model.repository.PanelRepository;
import org.springframework.stereotype.Component;

@Component
public class PanelConverter extends BaseConverter<Panel> {

    public PanelConverter(PanelRepository panelRepository) {
        super(Panel.class, panelRepository);
    }
}
