package ir.appservice.model.repository;

import ir.appservice.model.entity.domain.Document;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.annotation.ApplicationScope;

@Repository
@ApplicationScope
public interface DocumentRepository extends CrudRepository<Document, String> {

}
