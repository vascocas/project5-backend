package paj.project5_vc.service;


import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;


public class EmailService {
    private static final long serialVersionUID = 1L;

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587"; // Update with your SMTP server port
    private static final String SMTP_USERNAME = "your-email@example.com"; // Update with your SMTP username
    private static final String SMTP_PASSWORD = "your-email-password"; // Update with your SMTP password

    public static void sendConfirmationEmail(String recipientEmail, String token) {
        // Sender's email configuration
        String from = "your-email@example.com"; // Update with your email address

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
            message.setSubject("Email Confirmation");

            // Set Content: message body
            String confirmationLink = "http://your-api-domain/confirm/" + token; // Update with your confirmation endpoint
            String body = "Click the following link to confirm your email: " + confirmationLink;
            message.setText(body);

            // Send message
            Transport.send(message);
            System.out.println("Confirmation email sent successfully.");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}


