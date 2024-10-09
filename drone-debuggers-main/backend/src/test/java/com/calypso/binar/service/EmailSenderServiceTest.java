package com.calypso.binar.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailSenderServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailSenderService emailSenderService;


    @Test
    public void sendInitialPassword_sendsEmailWithCorrectDetails_whenSendingEmailSucceeds() {
        // Given
        String recipientEmail = "test@example.com";
        String subject = "Initial Password";
        String password = "password123";
        String expectedText = "\tHello this is you initial password for your AirAssist account, use it together with you email to log for the first time into you account.\n\nLogin: http://localhost:4200/login \nPassword: " + password + "\n\nBest Regards,\nDrone Debuggers Team";

        // Create the expected SimpleMailMessage object
        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(recipientEmail);
        expectedMessage.setSubject(subject);
        expectedMessage.setText(expectedText);

        // Mock the mailSender behavior (do nothing when send() is called)
        doNothing().when(mailSender).send(ArgumentMatchers.eq(expectedMessage));

        // When
        emailSenderService.sendInitialPassword(recipientEmail, subject, password);

        // Then
        // Verify the exact email content and behavior
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender, times(1)).send(messageCaptor.capture());

        // Assert
        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        assertEquals(recipientEmail, capturedMessage.getTo()[0]); // Verify recipient
        assertEquals(subject, capturedMessage.getSubject()); // Verify subject


    }

}
