package paj.project5_vc.service;

import jakarta.ejb.EJB;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import paj.project5_vc.bean.UserBean;

import java.util.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

    @Path("/confirm")
    public class ConfirmationService {

        @EJB
        UserBean userBean;

        @POST
        @Path("/{token}")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response confirmEmail(@PathParam("token") String token) {
            // Validate the token
            if (isValidToken(token)) {
                // Update user's state to validated in the database

                return Response.status(Response.Status.OK)
                        .entity("Email confirmed successfully. You can now log in.")
                        .build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid or expired token.")
                        .build();
            }
        }

        // token from the email
        private boolean isValidToken(String token) {
            // Validate the token against the database
            // Return true if the token is valid, false otherwise
            return true; // Placeholder implementation
        }


        private void sendConfirmationEmail(String recipientEmail, String token) {
            // Sender's email configuration
            String from = "aor.scrum.board@gmail.com";
            String host = "smtp.example.com";
            String username = "aor.scrum.board@gmail.com";
            String password = "Aor1904Qq";

            // Set properties
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.port", "587");

            // Get the default Session object.
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
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
                message.setText("Click the following link to confirm your email: http://your-api-domain/confirm/" + token);

                // Send message
                Transport.send(message);
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }
    }


