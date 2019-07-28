package ir.appservice.model.repository;

import ir.appservice.model.entity.domain.NaturalPerson;
import org.springframework.stereotype.Repository;

@Repository
public interface NaturalPersonRepository extends CrudRepository<NaturalPerson, Long> {
}
