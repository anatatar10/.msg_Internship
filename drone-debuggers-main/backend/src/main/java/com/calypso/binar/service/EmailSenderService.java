package com.calypso.binar.service;

import com.calypso.binar.model.Case;
import com.calypso.binar.model.PdfGenerationResult;
import com.calypso.binar.model.User;
import com.calypso.binar.repository.CaseRepository;
import com.calypso.binar.service.exception.CaseNotFoundException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CaseRepository caseRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSenderService.class);

    private static final String EMAIL_TEXT = "Hello, this is your initial password for your AirAssist account. Use it together with your email to log in for the first time into your account.\n\nLogin: http://localhost:4200/login \nPassword: %s\n\nBest Regards,\nDrone Debuggers Team";

    private static final String PDF_SUBJECT_TEXT = "AirAssist: Please Read Carefully CASE ";

    private static final String PDF_TEXT = " This is the contract generated for your case, please read it carefully and send it back to us signed.\n\nBest Regards,\nDrone Debuggers Team";

    // Static string attributes for subject and body template
    private static final String SUBJECT_TEMPLATE = "Update on Your Case %s";
    private static final String BODY_TEMPLATE = "Dear user,\n\n" +
            "We wanted to inform you that the status of your case with ID %s has been updated to: %s.\n\n" +
            "Thank you for your attention.\nBest regards,\nYour Company Team";

    /**
     * Sends an email with the initial password to the specified recipient.
     *
     * @param to       the recipient's email address.
     * @param subject  the subject of the email.
     * @param password the initial password to be included in the email body.
     */
    @Async
    public void sendInitialPassword(String to, String subject, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(String.format(EMAIL_TEXT, password));
            mailSender.send(message);
        } catch (Exception e) {
            // Log the error message
            LOGGER.error("Error occurred while sending email: {}", e.getMessage());
        }
    }


    @Async
    public void sendPdf(String email, PdfGenerationResult generatedPdf, String systemCaseId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // 'true' indicates multipart message

            helper.setTo(email);
            helper.setSubject(PDF_SUBJECT_TEXT + systemCaseId);
            helper.setText(PDF_TEXT);

            // Assuming generatedPdf contains the byte array of the PDF
            helper.addAttachment(generatedPdf.getFileName(), new ByteArrayResource(generatedPdf.getPdfData()));

            mailSender.send(message);
        } catch (Exception e) {
            LOGGER.error("Error occurred while sending email: " + e.getMessage());
        }
    }

    @Async
    public void sendUpdate(String systemCaseId, String status) {
        try {
            // Format the subject and body using String.format()
            Optional<Case> compensationCase = this.caseRepository.findBySystemCaseId(systemCaseId);
            if(compensationCase.isEmpty()){
                throw new CaseNotFoundException("Case with systemCaseId: " + systemCaseId + " not found");
            }

            User user = compensationCase.get().getPassenger();
            String email = user.getEmail();
            String subject = String.format(SUBJECT_TEMPLATE, systemCaseId);
            String body = String.format(BODY_TEMPLATE, systemCaseId, status);

            // Create and set up the message
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            mailSender.send(message);
            LOGGER.info("Email successfully sent to: " + email);

        } catch (Exception e) {
            LOGGER.error("Error occurred while sending email: " + e.getMessage());
        }
    }
}
