package ir.appservice.view.beanComponents.panelBean.trashBean;

import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.service.CrudService;
import ir.appservice.view.appComponent.AppLazyDataModel;
import ir.appservice.view.beanComponents.baseBean.BaseBean;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseTrashBean<T extends BaseEntity> extends BaseBean {

    protected Class clazz;
    protected CrudService<T> crudService;
    protected AppLazyDataModel<T> appLazyDataModel;

    public void init(CrudService<T> crudService, Class clazz) {
        this.crudService = crudService;
        this.clazz = clazz;

        this.appLazyDataModel = new AppLazyDataModel(this.crudService);
        this.appLazyDataModel.setPageSize(3);
    }

    public void permanentDelete(Long id) {
        crudService.remove(id);
    }

    public void recycle(Long id) {
        crudService.get(id).setDeleteDate(null);
    }

    public T load(Long id) {
        return crudService.get(id);
    }

}
