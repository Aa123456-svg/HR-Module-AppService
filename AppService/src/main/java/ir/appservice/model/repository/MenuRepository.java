package ir.appservice.model.repository;

import ir.appservice.model.entity.application.ui.Menu;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Long> {

    Set<Menu> findAllByTypeIgnoreCaseAndParentIsNullAndDeleteDateIsNull(String type);

    Set<Menu> findAllByTypeAndParentIsNullIgnoreCaseOrderByPriorityAsc(String type);
}
