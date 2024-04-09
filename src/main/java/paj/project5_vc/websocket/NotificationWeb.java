package paj.project5_vc.websocket;

import jakarta.ejb.Singleton;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;


@Singleton
@ServerEndpoint("/websocket/notification/{token}")
public class NotificationWeb {
    private static final Logger logger = LogManager.getLogger(NotificationWeb.class);
    HashMap<String, Session> sessions = new HashMap<String, Session>();


    // Method for sending websocket notifications
    public void send(@PathParam("token") String token, String msg) {
        Session session = sessions.get(token);
        if (session != null) {

            try {
                session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                logger.warn("Something went wrong!");
            }
        }
    }


    // Method for handling incoming WebSocket notifications
    public void handleMessageFromWebSocket(@PathParam("token") String token, String message) {
        Session session = sessions.get(token);
        if (session != null) {

            try {
                // Send confirmation back to the sender
                session.getBasicRemote().sendText("Notification received and saved");
            } catch (IOException e) {
                logger.error("Error sending confirmation message: " + e.getMessage());
            }
        }
    }


    @OnOpen
    public void toDoOnOpen(Session session, @PathParam("token") String token) {
        System.out.println("A new WebSocket session is opened for client with token: " + token);
        sessions.put(token, session);
    }

    @OnClose
    public void toDoOnClose(Session session, CloseReason reason) {
        System.out.println("Websocket session is closed with CloseCode: " +
                reason.getCloseCode() + ": " + reason.getReasonPhrase());
        for (String key : sessions.keySet()) {
            if (sessions.get(key) == session)
                sessions.remove(key);
        }
    }

    @OnMessage
    public void toDoOnMessage(Session session, String msg) {
        System.out.println("A new notification is received: " + msg);
        try {
            session.getBasicRemote().sendText("ack");
        } catch (IOException e) {
            System.out.println("Something went wrong!");
        }
    }
}

