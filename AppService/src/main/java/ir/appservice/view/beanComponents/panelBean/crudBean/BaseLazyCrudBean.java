package ir.appservice.view.beanComponents.panelBean.crudBean;

import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.service.CrudService;
import ir.appservice.view.appComponent.AppLazyDataModel;
import ir.appservice.view.beanComponents.panelBean.BasePanelBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseLazyCrudBean<T extends BaseEntity> extends BasePanelBean {

    protected AppLazyDataModel<T> appLazyDataModel;
    protected Class<T> itemClass;
    protected CrudService<T> crudService;

    public BaseLazyCrudBean(CrudService<T> crudService, Class<T> itemClass) {
        this.crudService = crudService;
        this.itemClass = itemClass;
        initDataModel();
    }

    protected void initDataModel() {
        this.appLazyDataModel = new AppLazyDataModel<T>(crudService, itemClass) {
        };
    }

    public String rowClass(T item) {
        try {
            if (item.getDeleteDate() != null) {
                return "alert alert-danger text-black";
            } else if (item.getStatus().equals(T.DE_ACTIVE_STATUS)) {
                return "alert alert-warning text-black";
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return "";
        }
    }

}
