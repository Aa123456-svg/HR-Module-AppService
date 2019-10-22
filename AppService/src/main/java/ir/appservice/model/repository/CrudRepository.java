package ir.appservice.model.repository;

import ir.appservice.model.entity.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface CrudRepository<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T,
        ID>,
        JpaSpecificationExecutor<T> {

    boolean existsByDisplayNameIgnoreCase(String displayName);

    List<T> findAllByStatusIs(String status);

    T findByDisplayName(String displayName);

    T findByDisplayNameIgnoreCase(String displayName);

    List<T> findAllByDeleteDateIsNull();

    Page<T> findAllByDeleteDateIsNull(Pageable pageable);

    Page<T> findAllByDeleteDateIsNull(Specification<T> specification, Pageable pageable);

    List<T> findAllByDeleteDateIsNotNull();

    Page<T> findAllByDeleteDateIsNotNull(Pageable pageable);

    Page<T> findAllByDeleteDateIsNotNull(Specification<T> specification, Pageable pageable);

    T getById(String id);
}
