package com.calypso.binar.service;

import com.calypso.binar.model.Case;
import com.calypso.binar.model.Document;
import com.calypso.binar.model.DocumentType;
import com.calypso.binar.model.dto.AttachedFilesDTO;
import com.calypso.binar.model.dto.DocumentCaseDTO;
import com.calypso.binar.repository.CaseRepository;
import com.calypso.binar.model.dto.DocumentDTO;
import com.calypso.binar.repository.DocumentRepository;
import com.calypso.binar.repository.DocumentTypeRepository;
import com.calypso.binar.service.exception.FileTooLargeException;
import com.calypso.binar.service.exception.InvalidFileType;
import com.calypso.binar.service.validation.CaseValidator;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.calypso.binar.service.exception.DocumentsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    CaseValidator caseValidator;

    private static final String PDF_FILE_TYPE = "application/pdf";
    private static final String JPEG_FILE_TYPE = "image/jpeg";
    private static final String JPG_FILE_TYPE = "image/jpg";
    private static final String PNG_FILE_TYPE = "image/png";

    public List<DocumentCaseDTO> getAllDocumentsForCase(String caseId){
        Optional<List<Document>> documents = documentRepository.findAllByCaseId(caseId);

        if (documents.isEmpty()) {
            documents = Optional.of(List.of());
        }


        return mapDocumentsToDTO(documents.get());
    }

    private List<DocumentCaseDTO> mapDocumentsToDTO(List<Document> documents) {
        return documents.stream()
                .map(document -> new DocumentCaseDTO(
                document.getDocumentId(),
                document.getDocumentType().getDocumentTypeName(),
                        document.getData(),
                        document.getDocumentName(),
                        document.getDocumentDataType(),
                document.getUploadTimestamp()
        )).collect(Collectors.toList());
    }

    public void saveCaseFiles(String systemCaseId, AttachedFilesDTO[] attachments) throws IOException, InvalidFileType, FileTooLargeException {
        caseValidator.validateFiles(attachments);
        for(AttachedFilesDTO file : attachments){
            ByteArrayOutputStream byteArrayOutputStream;

            if(file.getFileType().equals(PDF_FILE_TYPE)){
                byteArrayOutputStream = getPdfDocument(file);
            }
            else if (file.getFileType().equals(JPEG_FILE_TYPE) || file.getFileType().equals(JPG_FILE_TYPE) || file.getFileType().equals(PNG_FILE_TYPE)){
                byteArrayOutputStream = getImageDocument(file);
            }
            else {
                throw new InvalidFileType(file.getFileType());
            }

            Optional<DocumentType> documentType = documentTypeRepository.findDocumentTypeByDocumentTypeName(file.getDocumentType());
            if(documentType.isEmpty()){
                throw new InvalidFileType(file.getDocumentType());
            }
            Case caseEntity = caseRepository.findBySystemCaseId(systemCaseId).get();
            Document document = new Document(caseEntity, documentType.get(), file.getFileName() ,file.getFileType() ,byteArrayOutputStream.toByteArray(), file.getUploadDate());

            documentRepository.save(document);
        }
    }

    public ByteArrayOutputStream getPdfDocument(AttachedFilesDTO file) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            PdfReader pdfReader = new PdfReader(file.getFileData());
            PdfStamper pdfStamper = new PdfStamper(pdfReader, byteArrayOutputStream);

            pdfStamper.close();
            pdfReader.close();
        return byteArrayOutputStream;
    }

    public ByteArrayOutputStream getImageDocument(AttachedFilesDTO file) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getFileData());
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            ImageIO.write(bufferedImage, "jpeg", byteArrayOutputStream);

        return byteArrayOutputStream;
    }

    public DocumentCaseDTO getDocumentById(Integer documentId) throws DocumentsNotFoundException {
        return documentRepository.findById(documentId)
                .map(this::mapDocumentToDTO)
                .orElseThrow(DocumentsNotFoundException::new);
    }

    private DocumentCaseDTO mapDocumentToDTO(Document document) {
        return new DocumentCaseDTO(
                document.getDocumentId(),
                document.getDocumentType().getDocumentTypeName(),
                document.getData(),
                document.getDocumentName(),
                document.getDocumentDataType(),
                document.getUploadTimestamp()
        );
    }
}
