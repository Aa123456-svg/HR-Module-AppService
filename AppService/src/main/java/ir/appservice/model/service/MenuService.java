package ir.appservice.model.service;

import ir.appservice.model.entity.application.ui.Menu;
import ir.appservice.model.entity.application.ui.Panel;
import ir.appservice.model.repository.MenuRepository;
import ir.appservice.model.repository.PanelRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
@Transactional
public class MenuService extends CrudService<Menu> {

    private MenuRepository menuRepository;
    private PanelRepository panelRepository;

    public MenuService(MenuRepository menuRepository) {
        super(menuRepository);
        this.menuRepository = menuRepository;
    }

    public Set<Menu> list(String type) {
        return menuRepository.findAllByTypeIgnoreCaseAndParentIsNullAndDeleteDateIsNull(type);
    }

}