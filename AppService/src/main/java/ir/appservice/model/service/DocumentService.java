package ir.appservice.model.service;

import ir.appservice.model.entity.domain.Document;
import ir.appservice.model.repository.DocumentRepository;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
@Transactional
public class DocumentService extends CrudService<Document> {

    private DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        super(documentRepository);
        this.documentRepository = documentRepository;
    }

    public Document saveFile(String filePath) throws IOException {

        File defaultAvatarFile = new ClassPathResource(filePath).getFile();

        Document defaultAvatar = new Document();
        defaultAvatar.setDisplayName(defaultAvatarFile.getName());
        defaultAvatar.setType(Files.probeContentType(defaultAvatarFile.toPath()));
        defaultAvatar.setData(FileUtils.readFileToByteArray(defaultAvatarFile));

        return documentRepository.save(defaultAvatar);

    }

    public StreamedContent streamImage() {
        Long document_id = null;
        try {
            if (FacesContext.getCurrentInstance().getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
                // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
                logger.trace("DefaultStreamedContent");
                return new DefaultStreamedContent();
            } else {
                logger.trace("imageId: " + FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("imageId"));
                document_id = Long.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("imageId"));
                logger.trace(String.format("Streaming document: %s", document_id));
                return new DefaultStreamedContent(new ByteArrayInputStream(documentRepository.getById(document_id).getData()));
            }
        } catch (Exception e) {
            logger.error(String.format("Could not serve document: %s => %s", document_id, e.getMessage()));
            return new DefaultStreamedContent(new ByteArrayInputStream(documentRepository.getById((long) 1).getData()));
        }
    }

    public Document uploadDocument(FileUploadEvent event) {
        logger.trace(String.format("Uploading file: %s, size: %s", event.getFile().getFileName(), event.getFile().getSize() + ""));

        Document document = new Document();
        document.setDisplayName(event.getFile().getFileName());
        document.setData(event.getFile().getContents());
        document.setType(event.getFile().getContentType());
        document.setSize(event.getFile().getSize());

        logger.trace("Successful file upload", document.getDisplayName() + " is uploaded. Size (KB): " + document.getSize() / 1024f, null);

        return add(document);
    }


}