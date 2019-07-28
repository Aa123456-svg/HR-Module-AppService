package ir.appservice.model.service;

import ir.appservice.model.entity.application.ui.Menu;
import ir.appservice.model.entity.application.ui.Panel;
import ir.appservice.model.repository.MenuRepository;
import ir.appservice.model.repository.PanelRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PanelService extends CrudService<Panel> {

    private PanelRepository panelRepository;
    private MenuRepository menuRepository;

    public PanelService(PanelRepository panelRepository, MenuRepository menuRepository) {
        super(panelRepository);
        this.panelRepository = panelRepository;
        this.menuRepository = menuRepository;
    }

    public boolean existsByDisplayNameIgnoreCase(String displayName) {
        return panelRepository.existsByDisplayNameIgnoreCase(displayName);
    }

    public Panel addWithMenu(Panel panel) {

        Menu menu = new Menu();
        menu.setType(Menu.SIDE_BAR_MENU);
        menu.setPriority(Integer.MAX_VALUE);
        menu.setDisplayName(panel.getDisplayName());
        menuRepository.save(menu);
        panel.setMenu(menu);
        return panelRepository.save(panel);
    }

    public Map<String, File> getPanelsResources() {

        List<File> results = new ArrayList<>();
        for (File file : new File("./src/main/resources/META-INF/resources/").listFiles()) {
            fileSearch(file, results);
        }

        Map<String, File> panelsResources = new HashMap<>();
        results.forEach(file -> panelsResources.put(file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("resources")).replace("resources\\", ""), file));
        return panelsResources;
    }

    private void fileSearch(File file, List<File> result) {
        if (file.isFile() && file.getName().endsWith(".xhtml")) {
            result.add(file);
        } else if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                fileSearch(f, result);
            }
        }
    }
}
