package com.calypso.binar.service.generate_pdf;

import com.calypso.binar.model.Case;
import com.calypso.binar.model.GeneratedPdf;
import com.calypso.binar.model.PdfGenerationResult;
import com.calypso.binar.model.dto.PDFShowHistoryDTO;
import com.calypso.binar.repository.CaseRepository;
import com.calypso.binar.repository.GeneratedPdfRepository;
import com.calypso.binar.service.exception.CaseInvalidException;
import com.lowagie.text.*;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PdfGeneratorService {

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private GeneratedPdfRepository generatedPdfRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfGeneratorService.class);

    private static final String CONTRACT="Contract_";
    private static final String PDF=".pdf";
    /**
     * Get the specified template as an InputStream
     * @param templateName The name of the template to load
     * @return The InputStream for the specified template
     */
    public InputStream getTemplateAsStream(String templateName) {

        return getClass().getClassLoader().getResourceAsStream(templateName);
    }


    /**
     * Generate a PDF for the specified case ID
     * @param systemCaseId The ID of the case to generate a PDF for
     * @return The generated PDF as a byte array together with the pdf's file name
     * @throws DocumentException If an error occurs while generating the PDF
     * @throws IOException If an error occurs while loading the PDF template
     * @throws IllegalArgumentException If the case ID is null, zero, or negative
     * @throws CaseInvalidException If the case is invalid
     */
    public PdfGenerationResult generatePdf(Integer systemCaseId) throws DocumentException, IOException, CaseInvalidException {

        if (systemCaseId == null || systemCaseId == 0) {
            throw new IllegalArgumentException("Case ID cannot be null or zero");
        }

        if(systemCaseId < 0) {
            throw new IllegalArgumentException("Case ID cannot be negative");
        }

        Optional<Case> caseOptional = caseRepository.findById(systemCaseId);

        if (caseOptional.isEmpty()) {
            throw new IllegalArgumentException("Case not found with ID: " + systemCaseId);
        }

        Case caseEntity = caseOptional.get();

        if ("INVALID".equals(caseEntity.getStatus().getStatusName())) {
            throw new CaseInvalidException("Case is invalid.");
        }

        InputStream inputStream = getTemplateAsStream("ContractAirAssist.pdf");
        if (inputStream == null) {
            throw new IOException("Failed to load PDF template.");
        }

        PdfReader pdfReader = new PdfReader(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfStamper pdfStamper = new PdfStamper(pdfReader, byteArrayOutputStream);

        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
        PdfContentByte canvas = pdfStamper.getOverContent(1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(caseEntity.getDateCreated());

        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(String.valueOf(caseEntity.getSystemCaseId()), font), 232, 738, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(formattedDate, font), 305, 738, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(caseEntity.getPassengerDetails().getFirstName() + " " + caseEntity.getPassengerDetails().getLastName(), font), 108, 670, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(caseEntity.getPassengerDetails().getAddress() + " " + caseEntity.getPassengerDetails().getPostalCode(), font), 108, 627, 0);
        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(caseEntity.getReservation().getReservationNumber(), font), 468, 683, 0);

        pdfStamper.close();
        pdfReader.close();

        byte[] pdfBytes = byteArrayOutputStream.toByteArray();

        if (pdfBytes.length == 0) {
            throw new IllegalArgumentException("Failed to generate PDF for case ID: " + systemCaseId);
        }

        String fileName = CONTRACT + caseEntity.getPassengerDetails().getLastName() +
                "_" + caseEntity.getPassengerDetails().getFirstName() +
                "_" + caseEntity.getSystemCaseId() + PDF;

        GeneratedPdf generatedPdf = new GeneratedPdf(
                CONTRACT + caseEntity.getPassengerDetails().getLastName() +
                        "_" + caseEntity.getPassengerDetails().getFirstName() +
                        "_" + caseEntity.getCaseId() + PDF,
                caseEntity,
                pdfBytes,
                Timestamp.from(Instant.now())
        );
        generatedPdfRepository.save(generatedPdf);

        LOGGER.info("PDF generated and saved to database.");
        return new PdfGenerationResult(pdfBytes, fileName);
    }

    public Optional<GeneratedPdf> getGeneratedPdf(Integer documentId) {
        return generatedPdfRepository.findById(documentId);
    }


    public List<PDFShowHistoryDTO> getAllPdfs() {
        List<GeneratedPdf> generatedPdfs = this.generatedPdfRepository.findAll();

        return mapToPDFShowHistoryDTO(generatedPdfs);
    }


    public List<PDFShowHistoryDTO> mapToPDFShowHistoryDTO(List<GeneratedPdf> generatedPdfs) {
        return generatedPdfs.stream()
                .map(pdf -> {
                    String pdfName=CONTRACT+pdf.getCaseEntity().getPassengerDetails().getFirstName()+"_"+pdf.getCaseEntity().getPassengerDetails().getLastName()+"_"+pdf.getCaseEntity().getSystemCaseId()+PDF;
                    return new PDFShowHistoryDTO(
                            pdf.getDocumentId(),
                            pdfName,
                            pdf.getCaseEntity().getPassengerDetails().getFirstName(),
                            pdf.getCaseEntity().getPassengerDetails().getLastName(),
                            pdf.getCaseEntity().getSystemCaseId(),
                            pdf.getUploadTimestamp()
                    );
                }).collect(Collectors.toList());
    }

}
