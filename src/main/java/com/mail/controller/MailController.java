package com.mail.controller;

import com.mail.DTO.MailRequest;
import com.mail.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/mail")
public class MailController {
    private final EmailService emailingService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String sendMail(@RequestBody MailRequest request) throws MessagingException {
        emailingService.sendMail(request);
        return "Email sent successfully";
    }

    @PostMapping("/attach")
    @ResponseStatus(HttpStatus.OK)
    public String sendMailWithAttachment(@RequestParam("toEmail") String toEmail,
                                         @RequestParam("subject") String subject,
                                         @RequestParam("message") String message,
                                         @RequestParam("isHTML") boolean isHTML,
                                         @RequestParam("attachment") MultipartFile attachment) throws MessagingException, IOException {
        MailRequest request = MailRequest.builder()
                .toEmail(toEmail)
                .subject(subject)
                .message(message)
                .isHTML(isHTML)
                .attachment(attachment.getBytes())
                .attachmentName(attachment.getOriginalFilename())
                .build();

        emailingService.sendMail(request);
        return "Email with attachment sent successfully";
    }
}
