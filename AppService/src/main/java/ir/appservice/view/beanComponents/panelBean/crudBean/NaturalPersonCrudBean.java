package ir.appservice.view.beanComponents.panelBean.crudBean;

import ir.appservice.view.beanComponents.baseBean.BaseCrudBean;
import ir.appservice.model.entity.domain.NaturalPerson;
import ir.appservice.model.service.DocumentService;
import ir.appservice.model.service.NaturalPersonService;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.FileUploadEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Setter
@Getter
@Component
@SessionScope
public class NaturalPersonCrudBean extends BaseCrudBean<NaturalPerson> {

    private NaturalPersonService naturalPersonService;
    private DocumentService documentService;

    public NaturalPersonCrudBean(NaturalPersonService naturalPersonService, DocumentService documentService) {
        this.naturalPersonService = naturalPersonService;
        this.documentService = documentService;

        init(NaturalPerson.class, naturalPersonService);
    }

    public void uploadAvatar(FileUploadEvent event) {
        this.getItem().setAvatar(documentService.uploadDocument(event));
    }
}
