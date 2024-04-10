package paj.project5_vc.service;

import java.util.ArrayList;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import paj.project5_vc.bean.MessageBean;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import paj.project5_vc.bean.UserBean;
import paj.project5_vc.dto.MessageDto;

@Path("/messages")
public class MessageService {

    private static final long serialVersionUID = 1L;

    @EJB
    MessageBean messageBean;
    @Inject
    UserBean userBean;

    // Endpoint to get all user messages
    @GET
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMessagesUser(@HeaderParam("token") String token, @PathParam("userId") int userId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        ArrayList<MessageDto> messages = messageBean.getAllUserMessages(userId);
        return Response.status(200).entity(messages).build();
    }

    // Endpoint to get messages for a user (Received)
    @GET
    @Path("/received/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessagesForUser(@HeaderParam("token") String token, @PathParam("userId") int userId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        ArrayList<MessageDto> messages = messageBean.getReceivedUserMessages(userId);
        return Response.status(200).entity(messages).build();
    }

    // Endpoint to get messages from a user (Sent)
    @GET
    @Path("/sent/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessagesFromUser(@HeaderParam("token") String token, @PathParam("userId") int userId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        ArrayList<MessageDto> messages = messageBean.getSentUserMessages(userId);
        return Response.status(200).entity(messages).build();
    }

    // Endpoint to get chat messages
    @GET
    @Path("/chat/{senderId}/{receiverId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatMessages(@HeaderParam("token") String token, @PathParam("senderId") int senderId, @PathParam("receiverId") int receiverId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        ArrayList<MessageDto> messages = messageBean.getChatMessages(senderId, receiverId);
        return Response.status(200).entity(messages).build();
    }

    // Endpoint to send a message
    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMessage(@HeaderParam("token") String token, MessageDto messageDto) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        // Check if message text is empty
        if (messageDto.getMessageText() == null || messageDto.getMessageText().trim().isEmpty()) {
            return Response.status(400).entity("Message text cannot be empty").build();
        }
        if (messageBean.sendMessage(token, messageDto)) {
            return Response.status(200).entity("Message sent!").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

    // Endpoint to mark a message as read
    @PUT
    @Path("/read/{messageId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response markMessageAsRead(@HeaderParam("token") String token, @PathParam("messageId") int messageId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        if (messageBean.markMessageAsRead(messageId)) {
            return Response.status(200).entity("Messages marked as read!").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

}
