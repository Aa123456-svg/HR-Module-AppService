package ir.appservice.beanComponents.baseBean;

import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.service.CrudService;
import lombok.Getter;
import lombok.Setter;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;

@Setter
@Getter
public abstract class BaseCrudBean<T extends BaseEntity> extends BaseBean {

    private CrudService<T> crudService;

    //    private DataTable dataTable;
    private List<T> items;
    private List<T> filteredItems;
    private Class<T> clazz;
    private T item;

    private int rowsPerPage = 5;

    public void init(Class<T> clazz, CrudService crudService) {
        logger.trace("Initializing");

//        this.dataTable = new DataTable();
//        this.dataTable.setWidgetVar("ITEMS_LIST");
        this.crudService = crudService;
        this.clazz = clazz;

        newInstance();
        list();
    }

    public void newInstance() {
        item = null;
        try {
            item = this.clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        logger.trace("Item: " + item);
    }

    public void add() {
        logger.trace("add");
        T temp = crudService.add(item);
        items.add(0, temp);
        newInstance();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, String.format("%s added", temp.getClass().getSimpleName()), String.format("%s %s was added.", temp.getClass().getSimpleName(), temp.getDisplayName())));
        logger.trace(String.format("%s saved: ID: %s, DisplayName: %s", temp.getClass().getSimpleName(), temp.getId(), temp.getDisplayName()));

    }

    public void edit() {
        logger.trace("edit");
        T temp = crudService.edit(item);
        items.remove(temp);
        items.add(0, temp);
        newInstance();

        logger.trace(String.format("%s saved: ID: %s, DisplayName: %s", temp.getClass().getSimpleName(), temp.getId(), temp.getDisplayName()));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, String.format("%s edited", temp.getClass().getSimpleName()), String.format("%s %s was edited.", temp.getClass().getSimpleName(), temp.getDisplayName())));
    }


    public void remove(Long id) {
        logger.trace("remove");
        T temp = crudService.remove(id);
        items.remove(temp);

        logger.trace(String.format("%s removed: ID: %s, DisplayName: %s", temp.getClass().getSimpleName(), temp.getId(), temp.getDisplayName()));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, String.format("%s removed", temp.getClass().getSimpleName()), String.format("%s %s was removed.", temp.getClass().getSimpleName(), temp.getDisplayName())));
    }

    public void permanentRemove(Long id) {
        logger.trace("permanentRemove");
        T temp = crudService.permanentRemove(id);
        items.remove(temp);

        logger.trace(String.format("%s permanently removed: ID: %s, DisplayName: %s", temp.getClass().getSimpleName(), temp.getId(), temp.getDisplayName()));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, String.format("%s removed", temp.getClass().getSimpleName()), String.format("%s %s was removed.", temp.getClass().getSimpleName(), temp.getDisplayName())));
    }

    public void load(Long id) {
        logger.trace("load");
        item = crudService.get(id);

        logger.trace(String.format("%s loaded: ID: %s, DisplayName: %s", item.getClass().getSimpleName(), item.getId(), item.getDisplayName()));
//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, String.format("%s editing", item.getClass().getSimpleName()), String.format("%s %s is loading ...", item.getClass().getSimpleName(), item.getDisplayName())));
    }

    public void list() {
        logger.trace("list");
        items = crudService.list();
    }

    public void listDeletedItems() {
        logger.trace("listDeletedItems");
        items = crudService.list();
    }

}
