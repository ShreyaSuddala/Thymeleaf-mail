package com.mail.service;

import com.mail.DTO.MailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Async
    public void sendMail(MailRequest request) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true); // Enable multipart mode

        mimeMessageHelper.setFrom(fromMail);
        mimeMessageHelper.setTo(request.getToEmail());
        mimeMessageHelper.setSubject(request.getSubject());

        if(request.isHTML()) {
            Context context = new Context();
            context.setVariable("content", request.getMessage());
            String processedString = templateEngine.process("template", context);

            mimeMessageHelper.setText(processedString, true);
        } else {
            mimeMessageHelper.setText(request.getMessage(), false);
        }

        if (request.getAttachment() != null && request.getAttachmentName() != null) {
            mimeMessageHelper.addAttachment(request.getAttachmentName(), new ByteArrayDataSource(request.getAttachment(), "application/octet-stream"));
        }

        mailSender.send(mimeMessage);
    }
}

