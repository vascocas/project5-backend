package paj.project5_vc.websocket;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paj.project5_vc.dao.TokenDao;
import paj.project5_vc.dto.NotificationDto;
import paj.project5_vc.entity.TokenEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Singleton
@ServerEndpoint("/websocket/notification/{token}")
public class NotificationWeb {

    @EJB
    TokenDao tokenDao;
    private static final Logger logger = LogManager.getLogger(NotificationWeb.class);
    HashMap<String, Session> sessions = new HashMap<String, Session>();

    // Method for sending websocket notifications
    public void send(int receiverId, String notif) {
        // Retrieve the token entities associated with the receiver Id
        ArrayList<TokenEntity> receiverTokens = tokenDao.findAllTokensByUserId(receiverId);
        // Iterate over all sessions
        for (Session s : sessions.values()) {
            // Check if the session has the given token
            for (Map.Entry<String, Session> entry : sessions.entrySet()) {
                if (entry.getValue().equals(s)) {
                    String sessionToken = entry.getKey();
                    for (TokenEntity t : receiverTokens) {
                        if (sessionToken != null && sessionToken.equals(t.getTokenValue())) {
                            try {
                                // Send the notification to the session
                                s.getBasicRemote().sendText(notif);
                            } catch (IOException e) {
                                logger.warn("Something went wrong while sending notification to receiver", e);
                            }
                        }
                    }
                }
            }
        }
    }

    @OnOpen
    public void toDoOnOpen(Session session, @PathParam("token") String token) {
        logger.info("A new WebSocket session is opened for client");
        sessions.put(token, session);
    }

    @OnClose
    public void toDoOnClose(Session session, CloseReason reason) {
        logger.info("Websocket session is closed with CloseCode: " +
                reason.getCloseCode() + ": " + reason.getReasonPhrase());
        for (String key : sessions.keySet()) {
            if (sessions.get(key) == session)
                sessions.remove(key);
        }
    }

    @OnMessage
    public void toDoOnMessage(Session session, String msg) {
        logger.info("A new notification is received: " + msg);
        try {
            session.getBasicRemote().sendText("ack");
        } catch (IOException e) {
            logger.warn("Something went wrong!", e);
        }
    }
}

