package ir.appservice.model.converter;

import ir.appservice.model.entity.domain.Document;
import ir.appservice.model.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@Component
public class DocumentConverter implements Converter<Document> {

    private final static Logger logger = LoggerFactory.getLogger(DocumentConverter.class);

    private DocumentService documentService;

    public DocumentConverter(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Override
    public Document getAsObject(FacesContext context, UIComponent component, String id) {

        logger.trace(String.format("Converting %s to document...", id));
        try {
            Document temp = documentService.get(Long.valueOf(id));
            logger.trace(String.format("Document: %s", temp.getId()));
            return temp;
        } catch (Exception e) {
            logger.warn(String.format("Document: %s", e.getMessage()));
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Document document) {
        logger.trace(String.format("Converting document to %s ...", document.getId().toString()));
        try {
            logger.trace(String.format("String: %s", document.getId().toString()));
            return document.getId().toString();
        } catch (Exception e) {
            logger.warn(String.format("String: %s", e.getMessage()));
            return "-1";
        }
    }
}
