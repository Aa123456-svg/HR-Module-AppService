package ir.appservice.model.service;

import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.repository.CrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class CrudService<T extends BaseEntity> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private CrudRepository crudRepository;

    public CrudService(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public T add(T t) {
        return (T) crudRepository.save(t);
    }

    public T remove(Long id) {
        BaseEntity temp = (BaseEntity) crudRepository.getOne(id);
        temp.setDeleteDate(new Date());
        return (T) crudRepository.save(temp);
    }

    public T remove(T t) {
        return remove(t.getId());
    }

    public T permanentRemove(Long id) {
        BaseEntity temp = (BaseEntity) crudRepository.getOne(id);
        crudRepository.delete(temp);
        return (T) temp;
    }

    public T permanentRemove(T t) {
        return permanentRemove(t.getId());
    }

    public T edit(T t) {
        return (T) crudRepository.save(t);
    }

    public T get(Long id) {
        return (T) crudRepository.getById(id);
    }

    public List<T> list() {
        return crudRepository.findAllByDeleteDateIsNullOrderByIdDesc();
    }

    public List<T> listDeleted() {
        return crudRepository.findAllByDeleteDateIsNotNullOrderByIdDesc();
    }

}
