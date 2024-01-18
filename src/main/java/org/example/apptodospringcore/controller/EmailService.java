package org.example.apptodospringcore.controller;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

@Service
@NoArgsConstructor
public class EmailService {
    private final String gmail = "farkhodovalisher@gmail.com";
    private final String password = "pxumezxbhtrgcafs";
    private final Random random =  new Random();

    public String generateCode() {
        return random.nextInt(1000, 10000) + "";
    }

    public void send(String email, String title, String body) {
        try {
            Transport.send(getMimeMessage(title, body, email));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private MimeMessage getMimeMessage(String subject, String text, String email) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(getSession());
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(
                "<h1 style='color: red'>%s</h1>".formatted(text),
                "text/html"
        );

        mimeMessage.setFrom(new InternetAddress(gmail));
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        return mimeMessage;
    }

    private Session getSession() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
//        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");

        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(gmail, password);
            }
        });
    }

}
