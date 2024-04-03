package paj.project5_vc.websocket;

    import java.util.List;

    import jakarta.ejb.EJB;
    import paj.project5_vc.bean.MessageBean;
    import jakarta.ws.rs.*;
    import jakarta.ws.rs.core.MediaType;
    import jakarta.ws.rs.core.Response;

    import paj.project5_vc.dto.MessageDto;

    @Path("/messages")
    public class MessageService {


        private static final long serialVersionUID = 1L;


        @EJB
        MessageBean messageBean;

        // Endpoint to send a message
        @POST
        @Path("/send")
        @Consumes(MediaType.APPLICATION_JSON)
        public Response sendMessage(MessageDto messageDto) {
            messageBean.sendMessage(messageDto);
            return Response.status(200).entity("Message sent!").build();
        }

        // Endpoint to mark a message as read
        @PUT
        @Path("/{messageId}/read")
        public Response markMessageAsRead(@PathParam("messageId") int messageId) {
            messageBean.markMessageAsRead(messageId);
            return Response.status(200).entity("Message marked as read!").build();
        }

        // Endpoint to get messages for a user
        @GET
        @Path("/user/{userId}")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getMessagesForUser(@PathParam("userId") int userId) {
            List<MessageDto> messages = messageBean.getMessagesForUser(userId);
            return Response.status(200).entity(messages).build();
        }


}
