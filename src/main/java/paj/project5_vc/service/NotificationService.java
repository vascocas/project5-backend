package paj.project5_vc.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import paj.project5_vc.bean.NotificationBean;
import paj.project5_vc.bean.UserBean;
import paj.project5_vc.dto.MessageDto;
import paj.project5_vc.dto.NotificationDto;

import java.util.ArrayList;

@Path("/notifications")
public class NotificationService {

    private static final long serialVersionUID = 1L;

    @Inject
    UserBean userBean;

    @Inject
    NotificationBean notificationBean;

    // Endpoint to get user notifications
    @GET
    @Path("/{recipientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatMessages(@HeaderParam("token") String token, @PathParam("recipientId") int recipientId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        ArrayList<NotificationDto> notifications = notificationBean.getUserNotifications(recipientId);
        return Response.status(200).entity(notifications).build();
    }

    // Endpoint to send a notification
    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendUserNotification(@HeaderParam("token") String token, NotificationDto notificationDto) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        if (notificationBean.sendNotification(notificationDto)) {
            return Response.status(200).entity("Notification sent!").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

    // Endpoint to mark a notification as read
    @PUT
    @Path("/read/{notificationId}")
    public Response markMessageAsRead(@HeaderParam("token") String token, @PathParam("notificationId") int notificationId) {
        if (!userBean.tokenExist(token)) {
            return Response.status(401).entity("Invalid token").build();
        }
        if (notificationBean.markNotificationAsRead(notificationId)) {
            return Response.status(200).entity("Notification marked as read!").build();
        } else {
            return Response.status(403).entity("Unauthorized").build();
        }
    }

}
