package ir.appservice.model.converter;

import ir.appservice.model.entity.domain.Document;
import ir.appservice.model.repository.DocumentRepository;
import org.springframework.stereotype.Component;

@Component
public class DocumentConverter extends BaseConverter<Document> {

    public DocumentConverter(DocumentRepository documentRepository) {
        super(Document.class, documentRepository);
    }
}
