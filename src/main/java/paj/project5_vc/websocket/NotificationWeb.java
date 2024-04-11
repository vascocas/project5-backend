package paj.project5_vc.websocket;

import jakarta.ejb.Singleton;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paj.project5_vc.dto.NotificationDto;

import java.io.IOException;
import java.util.HashMap;


@Singleton
@ServerEndpoint("/websocket/notification/{token}")
public class NotificationWeb {
    private static final Logger logger = LogManager.getLogger(NotificationWeb.class);
    HashMap<String, Session> sessions = new HashMap<String, Session>();

    // Method for sending websocket notifications
    public void send(@PathParam("token") String token, String notifDto) {
        Session session = sessions.get(token);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(notifDto);
            } catch (IOException e) {
                logger.warn("Error sending WebSocket notification", e);
            }
        }
    }

    @OnOpen
    public void toDoOnOpen(Session session, @PathParam("token") String token) {
        System.out.println("A new WebSocket session is opened for client");
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

