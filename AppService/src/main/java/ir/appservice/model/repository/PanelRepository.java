package ir.appservice.model.repository;

import ir.appservice.model.entity.application.ui.Panel;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public interface PanelRepository extends CrudRepository<Panel, String> {

}
