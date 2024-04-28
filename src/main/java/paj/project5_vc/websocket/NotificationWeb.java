package paj.project5_vc.websocket;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paj.project5_vc.dao.TokenDao;
import paj.project5_vc.entity.TokenEntity;

import java.io.IOException;
import java.util.*;


@Singleton
@ServerEndpoint("/websocket/notification/{token}")
public class NotificationWeb {

    @EJB
    TokenDao tokenDao;
    private static final Logger logger = LogManager.getLogger(NotificationWeb.class);
    HashMap<String, Session> notifSessions = new HashMap<String, Session>();

    // Method for sending websocket notifications
    public void send(int receiverId, String notif) {
        // Retrieve the token entities associated with the receiver Id
        ArrayList<TokenEntity> receiverTokens = tokenDao.findAllTokensByUserId(receiverId);
        // Iterate over all user tokens
        for (TokenEntity token : receiverTokens) {
            Session session = notifSessions.get(token.getTokenValue());
            if (session != null) {
                try {
                    // Send the notification to the session
                    session.getBasicRemote().sendText(notif);
                } catch (IOException e) {
                    logger.warn("Something went wrong while sending notification to receiver", e);
                }
            }

        }
    }

    @OnOpen
    public void toDoOnOpen(Session session, @PathParam("token") String token) {
        logger.info("A new WebSocket session is opened for client");
        notifSessions.put(token, session);
    }

    @OnClose
    public void toDoOnClose(Session session, CloseReason reason) {
        logger.info("Websocket session is closed with CloseCode: " +
                reason.getCloseCode() + ": " + reason.getReasonPhrase());
        // Create an iterator to safely remove elements
        Iterator<Map.Entry<String, Session>> iterator = notifSessions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Session> entry = iterator.next();
            if (entry.getValue().equals(session)) {
                iterator.remove(); // Safely remove the entry using the iterator
            }
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

