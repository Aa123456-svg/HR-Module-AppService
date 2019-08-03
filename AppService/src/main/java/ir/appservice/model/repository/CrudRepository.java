package ir.appservice.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface CrudRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    boolean existsByDisplayNameIgnoreCase(String displayName);

    T findByDisplayName(String displayName);

    T findByDisplayNameIgnoreCase(String displayName);

    List<T> findAllByDeleteDateIsNullOrderByIdDesc();

    Page<T> findAllByDeleteDateIsNullOrderByIdDesc(Pageable pageable);

    List<T> findAllByDeleteDateIsNotNullOrderByIdDesc();

    Page<T> findAllByDeleteDateIsNotNullOrderByIdDesc(Pageable pageable);

    T getById(Long id);
}
