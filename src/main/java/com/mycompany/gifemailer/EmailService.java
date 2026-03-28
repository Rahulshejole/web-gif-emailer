package com.mycompany.gifemailer;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * EmailService - sends HTML email + (optional) embedded GIF using Gmail SMTP.
 */
public class EmailService {

    private Session createSession(final String senderEmail, final String senderAppPassword) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderAppPassword);
            }
        });
    }

    /** Send OTP email (HTML). */
    public void sendOtpEmail(String senderEmail, String senderAppPassword, String toEmail, String otp)
            throws MessagingException {

        Session session = createSession(senderEmail, senderAppPassword);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Your Login OTP Verification Code");

        String html = "<div style='font-family:Arial,sans-serif'>"
                + "<h2>OTP Verification</h2>"
                + "<p>Your OTP is:</p>"
                + "<div style='font-size:28px;font-weight:bold;letter-spacing:4px'>" + otp + "</div>"
                + "<p>This OTP is valid for <b>2 minutes</b>.</p>"
                + "</div>";

        message.setContent(html, "text/html; charset=utf-8");
        Transport.send(message);
    }

    /** Send campaign email with embedded GIF. */
    public void sendEmailWithEmbeddedGif(String senderEmail, String senderAppPassword, String toEmail,
                                        String gifPath, String subject, String body)
            throws MessagingException, IOException {

        Session session = createSession(senderEmail, senderAppPassword);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);

        Multipart multipart = new MimeMultipart("related");

        // HTML Part
        MimeBodyPart htmlPart = new MimeBodyPart();
        String safeBody = body == null ? "" : body.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\n", "<br>");
        String htmlContent = "<div style='font-family:Arial,sans-serif'>"
                + "<div>" + safeBody + "</div>"
                + "<br>"
                + "<img src='cid:animatedGif' style='max-width:100%;height:auto'/>"
                + "</div>";
        htmlPart.setContent(htmlContent, "text/html; charset=utf-8");
        multipart.addBodyPart(htmlPart);

        // Image Part (embedded)
        if (gifPath != null && !gifPath.trim().isEmpty()) {
            File gifFile = new File(gifPath);
            if (gifFile.exists()) {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.attachFile(gifFile);
                imagePart.setContentID("<animatedGif>");
                imagePart.setDisposition(MimeBodyPart.INLINE);
                multipart.addBodyPart(imagePart);
            }
        }

        message.setContent(multipart);
        Transport.send(message);
    }
}