package ir.appservice.model.service;

import ir.appservice.model.entity.BaseEntity;
import ir.appservice.model.repository.CrudRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CrudService<T extends BaseEntity> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private CrudRepository crudRepository;

    @Autowired
    private EntityManager entityManager;

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
        return crudRepository.findAll();
    }

    public List<T> list(Pageable pageable) {
        return crudRepository.findAll(pageable).getContent();
    }

    public List<T> listDeleted() {
        return crudRepository.findAllByDeleteDateIsNullOrderByIdDesc();
    }

    public List<T> listDeleted(Pageable pageable) {
        return crudRepository.findAllByDeleteDateIsNotNullOrderByIdDesc(pageable).getContent();
    }

    public Page<T> findByFilter(Map<String, String> filters, Pageable pageable) {
        return crudRepository.findAll(getFilterSpecification(filters), pageable);
    }

    private Specification<T> getFilterSpecification(Map<String, String> filterValues) {

        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            Optional<Predicate> predicate = filterValues.entrySet().stream()
                    .filter(v -> v.getValue() != null && v.getValue().length() > 0)
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
            return predicate.orElseGet(() -> builder.isTrue(builder.literal(true)));
        };
    }
}
