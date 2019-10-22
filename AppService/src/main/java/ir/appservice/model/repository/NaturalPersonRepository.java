package ir.appservice.model.repository;

import ir.appservice.model.entity.domain.NaturalPerson;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.List;

@Repository
@ApplicationScope
public interface NaturalPersonRepository extends CrudRepository<NaturalPerson, String> {

    List<NaturalPerson> findAllByAccountIsNullAndStatusIs(String status);

    List<NaturalPerson> findAllByEmployeeIsNullAndStatusIs(String status);
}
