package ir.appservice.view.beanComponents.panelBean.crudBean;

import ir.appservice.view.beanComponents.baseBean.BaseCrudBean;
import ir.appservice.model.entity.domain.Document;
import ir.appservice.model.service.DocumentService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;


@Setter
@Getter
@Component
@SessionScope
public class DocumentCrudBean extends BaseCrudBean<Document> {

    private DocumentService documentService;
    private StreamedContent streamedContent;

    public DocumentCrudBean(DocumentService documentService) {
        this.documentService = documentService;

        init(Document.class, documentService);
    }

    public void uploadDocument(FileUploadEvent event) {
        logger.trace("Uploading file: " + event.getFile().getFileName());
        this.setItem(documentService.uploadDocument(event));
    }
}
