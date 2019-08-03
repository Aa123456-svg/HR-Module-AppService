package ir.appservice.view.appComponent;

import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.service.CrudService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AppLazyDataModel<T extends BaseEntity> extends LazyDataModel<T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected List<T> filteredItems;
    protected List<T> items;
    protected T item;

    private CrudService<T> crudService;

    public AppLazyDataModel(CrudService<T> crudService) {
        this.crudService = crudService;
        setPageSize(10);
    }

    @Override
    public List<T> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {
        logger.trace("First: {}, PageSize: {}, SortMeta: {}, Filters: {}", first, pageSize, multiSortMeta, filters);
        List<Sort.Order> orders = new ArrayList<>();
        if (multiSortMeta != null) {
            multiSortMeta.stream().forEach(
                    sortMeta -> {
                        switch (sortMeta.getSortOrder()) {
                            case ASCENDING:
                                orders.add(JpaSort.JpaOrder.asc(sortMeta.getSortField()));
                                break;
                            case DESCENDING:
                                orders.add(JpaSort.JpaOrder.desc(sortMeta.getSortField()));
                                break;
                        }
                    }
            );
        }

        List<T> list = crudService.list(PageRequest.of(first, pageSize, Sort.by(orders)));
        logger.trace("AppLazyDataModel: {}", list);
        return list;
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        logger.trace("First: {}, PageSize: {}, SortOrder: {}, Filters: {}", first, pageSize, sortOrder, filters);
        Sort sort;
        switch (sortOrder) {
            case ASCENDING:
                sort = JpaSort.by(Sort.Direction.ASC, sortField);
                break;
            case DESCENDING:
                sort = JpaSort.by(Sort.Direction.DESC, sortField);
                break;
            default:
                sort = JpaSort.unsorted();
        }

        List<T> list = crudService.list(PageRequest.of(first, pageSize, sort));
        logger.trace("AppLazyDataModel: {}", list);
        return list;
    }

    @Override
    public T getRowData(String rowKey) {
        logger.trace("getRowData: {}", rowKey);
        return crudService.get(Long.valueOf(rowKey));
    }

    @Override
    public Object getRowKey(T object) {
        logger.trace("getRowKey: {}", object.toString());
        return object.getId();
    }

}
