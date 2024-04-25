package paj.project5_vc.bean;

import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.Properties;


@Stateless
public class EmailBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(EmailBean.class);
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USERNAME = "aor.scrum.board@gmail.com";
    private static final String SMTP_PASSWORD = "vndlfjgdjkozdnpa";

    // old pass: Aor1904Qq

    public static void sendConfirmationEmail(String recipientEmail, String token) {
        // Sender's email configuration
        String from = "aor.scrum.board@gmail.com";
        // Set properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        // Get the default Session object.
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            // Set Subject: header field
            message.setSubject("Confirmation Email");
            // Set Content: message body
            String confirmationLink = "http://localhost:3000/validate/?validationToken=" + token;
            String body = "Click the following link to confirm your register: " + confirmationLink;
            message.setText(body);
            // Send message
            Transport.send(message);
            logger.debug("Confirmation email sent successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void sendResetPasswordEmail(String recipientEmail, String token) {
        // Sender's email configuration
        String from = "aor.scrum.board@gmail.com";
        // Set properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_HOST);
        properties.put("mail.smtp.port", SMTP_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        // Get the default Session object.
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            // Set Subject: header field
            message.setSubject("Reset Password");
            // Set Content: message body
            String confirmationLink = "http://localhost:3000/recovery/?resetToken=" + token;
            String body = "Click the following link to reset your password: " + confirmationLink;
            message.setText(body);
            // Send message
            Transport.send(message);
            logger.debug("Recovery password email sent successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}


