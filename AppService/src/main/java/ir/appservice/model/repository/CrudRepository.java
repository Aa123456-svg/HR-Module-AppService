package ir.appservice.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface CrudRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    boolean existsByDisplayNameIgnoreCase(String displayName);

    T findByDisplayName(String displayName);

    T findByDisplayNameIgnoreCase(String displayName);

    List<T> findAllByDeleteDateIsNullOrderByIdDesc();

    List<T> findAllByDeleteDateIsNotNullOrderByIdDesc();

    T getById(Long id);
}
