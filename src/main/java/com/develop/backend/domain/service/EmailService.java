package com.develop.backend.domain.service;

import com.develop.backend.infrastructure.exception.EmailSendNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String to, String subject, String body) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            javaMailSender.send(message);
            log.info("Email enviado a: {}", to + body);
        } catch (MessagingException e) {
            log.error("Error enviando email a: {}. Error: {}", to, e.getMessage());
            throw new EmailSendNotFoundException("Error enviando email a: " + to + ". Error: " + e);
        }
    }

    @Async
    public void sendEmailWithAttachment(String to, String subject, String text, byte[] pdfContent, String filename) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);

        ByteArrayResource resource = new ByteArrayResource(pdfContent);
        helper.addAttachment(filename, resource);

        javaMailSender.send(mimeMessage);
        log.info("Email con PDF enviado a: {}", to);
    }

    @Async("emailExecutor")
    public void sendEmailWithMultipleAttachment(String to, String subject, String text, List<MultipartFile> files) throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String filename = Objects.requireNonNull(file.getOriginalFilename());
                    ByteArrayResource resource = new ByteArrayResource(file.getBytes());
                    log.info("Adjuntando archivo: {}", filename);
                    helper.addAttachment(filename, resource);
                }
            }

            javaMailSender.send(mimeMessage);
            log.info("Email con múltiples adjuntos enviado a: {}", to);

        } catch (MessagingException | IOException e) {
            log.error("Error enviando email con múltiples adjuntos a: {}. Error: {}", to, e.getMessage());
            throw new EmailSendNotFoundException("Error enviando email con adjunto a: " + to + ". Error: " + e.getMessage());
        }
    }
}
