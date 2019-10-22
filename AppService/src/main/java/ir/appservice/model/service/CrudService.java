package ir.appservice.model.service;

import ir.appservice.configuration.exception.NotFoundEntityException;
import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.repository.CrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

public abstract class CrudService<T extends BaseEntity> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    protected CrudRepository crudRepository;
    protected Class<T> clazz;

    @PersistenceContext
    private EntityManager entityManager;

    public CrudService(CrudRepository crudRepository, Class<T> clazz) {
        this.crudRepository = crudRepository;
        this.clazz = clazz;
    }

    public boolean existById(String id) {
        return this.crudRepository.existsById(id);
    }

    public List<T> addAll(Collection<T> items) {
        return this.crudRepository.saveAll(items);
    }

    public List<T> editAll(Collection<T> items) {
        List<T> editedItems = new ArrayList<>();

        items.forEach(t -> editedItems.add(edit(t)));
        return editedItems;
    }

    public T add(T t) {
        return (T) crudRepository.save(t);
    }

    public T remove(String id) {
        T temp;
        try {
            temp = (T) crudRepository.getOne(id);
            temp.setDeleteDate(new Date());
            return (T) crudRepository.save(temp);
        } catch (EntityNotFoundException e) {
            throw new NotFoundEntityException(e.getMessage());
        }
    }

    public T remove(T t) {
        return remove(t.getId());
    }

    public T recycle(String id) {
        T temp;
        try {
            temp = (T) crudRepository.getOne(id);
            temp.setDeleteDate(null);
            return (T) crudRepository.save(temp);
        } catch (EntityNotFoundException e) {
            throw new NotFoundEntityException(e.getMessage());
        }
    }

    public T activate(String id) {
        T temp;
        try {
            temp = (T) crudRepository.getOne(id);
            temp.setStatus(T.ACTIVE_STATUS);
            return (T) crudRepository.save(temp);
        } catch (EntityNotFoundException e) {
            throw new NotFoundEntityException(e.getMessage());
        }
    }

    public T deActivate(String id) {
        T temp;
        try {
            temp = (T) crudRepository.getOne(id);
            temp.setStatus(T.DE_ACTIVE_STATUS);
            return (T) crudRepository.save(temp);
        } catch (EntityNotFoundException e) {
            throw new NotFoundEntityException(e.getMessage());
        }
    }

    public T recycle(T t) {
        return recycle(t.getId());
    }

    public T permanentRemove(String id) {
        T temp;
        try {
            temp = (T) crudRepository.getById(id);
            crudRepository.deleteById(id);
            return temp;
        } catch (EntityNotFoundException e) {
            throw new NotFoundEntityException(e.getMessage());
        }
    }

    public T permanentRemove(T t) {
        return permanentRemove(t.getId());
    }

    public T edit(T t) {

        if (!crudRepository.existsById(t.getId())) {
            throw new NotFoundEntityException("Entity Not Found Exception: " + t.getId());
        }
        return (T) crudRepository.save(t);
    }

    public T get(String id) {
        return (T) crudRepository.getById(id);
    }

    public List<T> list() {
        return crudRepository.findAll();
    }

    public List<T> list(Map<String, Object> filters) {
        return crudRepository.findAll(getFilterSpecification(filters));
    }

    public Page<T> list(Pageable pageable) {
        return crudRepository.findAll(pageable);
    }

    public Page<T> list(Pageable pageable, Map<String, Object> filters) {
        return crudRepository.findAll(getFilterSpecification(filters), pageable);
    }

    private Specification<T> getFilterSpecification(
            Map<String, Object> filterValues) {

        Specification<T> specification = (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            Optional<Predicate> predicate = filterValues.entrySet().stream()
                    .filter(v -> v.getValue() != null)
                    .map(entry -> {
                        Path<?> path = root;
                        String key = entry.getKey();
                        if (entry.getKey().contains(".")) {
                            String[] splitKey = entry.getKey().split("\\.");
                            path = root.join(splitKey[0]);
                            key = splitKey[1];
                        }
                        return builder.like(path.get(key).as(String.class),
                                "%" + entry.getValue() + "%");
                    })
                    .collect(Collectors.reducing((a, b) -> builder.and(a, b)));
            return predicate.orElseGet(() -> alwaysTrue(builder));
        };

        return specification;
    }

    private Predicate alwaysTrue(CriteriaBuilder builder) {
        return builder.isTrue(builder.literal(true));
    }

    private Specification<T> getDeletedSpecifications() {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> builder.isNotNull(root.get("deleteDate"));
    }

    private Specification<T> getNotDeletedSpecifications() {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> builder.isNull(root.get("deleteDate"));
    }
}
