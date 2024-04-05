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

    // Endpoint to send a message
    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendMessage(@HeaderParam("token") String token, MessageDto messageDto) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        if (messageBean.sendMessage(messageDto)) {
            return Response.status(200).entity("Message sent!").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

    // Endpoint to mark a message as read
    @PUT
    @Path("/read/{messageId}")
    public Response markMessageAsRead(@HeaderParam("token") String token, @PathParam("messageId") int messageId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        if (messageBean.markMessageAsRead(messageId)) {
            return Response.status(200).entity("Message marked as read!").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

    // Endpoint to get messages for a user
    @GET
    @Path("/received/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessagesForUser(@HeaderParam("token") String token, @PathParam("userId") int userId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        ArrayList<MessageDto> messages = messageBean.getReceivedUserMessages(userId);
        return Response.status(200).entity(messages).build();
    }

    // Endpoint to get messages from a user
    @GET
    @Path("/sent/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMessagesFromUser(@HeaderParam("token") String token, @PathParam("userId") int userId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        ArrayList<MessageDto> messages = messageBean.getSentUserMessages(userId);
        return Response.status(200).entity(messages).build();
    }

    // Endpoint to get messages for a user
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMessagesUser(@HeaderParam("token") String token, @PathParam("userId") int userId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        ArrayList<MessageDto> messages = messageBean.getAllUserMessages(userId);
        return Response.status(200).entity(messages).build();
    }

    // Endpoint to get chat messages
    @GET
    @Path("/chat/{senderId}/{receiverId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatMessages(@HeaderParam("token") String token, @PathParam("senderId") int senderId, @PathParam("receiverId") int receiverId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        ArrayList<MessageDto> messages = messageBean.getChatMessages(senderId, receiverId);
        return Response.status(200).entity(messages).build();
    }

}
