package com.javaweb.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SendGridMailService {

    @Value("${sendgrid.api-key}")
    private String apiKey;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    private SendGrid sendGrid;

    @PostConstruct
    public void init() {
        this.sendGrid = new SendGrid(apiKey);
    }

    public void sendTextMail(String to, String subject, String textContent) {
        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", textContent);
        Mail mail = new Mail(from, subject, toEmail, content);

        send(mail);
    }

    public void sendHtmlMail(String to, String subject, String htmlContent) {
        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);

        send(mail);
    }

    private void send(Mail mail) {
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            if (response.getStatusCode() != 202) {
                throw new RuntimeException(
                        "SendGrid error: " +
                                response.getStatusCode() + " - " +
                                response.getBody()
                );
            }

        } catch (Exception e) {
            throw new RuntimeException("SendGrid send mail failed", e);
        }
    }
}
