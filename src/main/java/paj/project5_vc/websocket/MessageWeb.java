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
@ServerEndpoint("/websocket/message/{token}")
public class MessageWeb {
    private static final Logger logger = LogManager.getLogger(MessageWeb.class);
    public HashMap<String, Session> messageSessions = new HashMap<>();

    @EJB
    TokenDao tokenDao;

    public void send(int receiverId, String message) {
        // Retrieve the token entities associated with the receiver Id
        ArrayList<TokenEntity> receiverTokens = tokenDao.findAllTokensByUserId(receiverId);
        // Iterate over all user tokens
        for (TokenEntity token : receiverTokens) {
            Session session = messageSessions.get(token.getTokenValue());
            if (session != null) {
                try {
                    // Send the message to the session
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.warn("Something went wrong while sending message to receiver", e);
                }
            }
        }
    }

    @OnOpen
    public void toDoOnOpen(Session session, @PathParam("token") String token) {
        logger.info("A new WebSocket session is opened for client");
        messageSessions.put(token, session);
    }

    @OnClose
    public void toDoOnClose(Session session, CloseReason reason) {
        logger.info("Websocket session is closed with CloseCode: " +
                reason.getCloseCode() + ": " + reason.getReasonPhrase());
        // Create an iterator to safely remove elements
        Iterator<Map.Entry<String, Session>> iterator = messageSessions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Session> entry = iterator.next();
            if (entry.getValue().equals(session)) {
                iterator.remove(); // Safely remove the entry using the iterator
            }
        }
    }

    @OnMessage
    public void toDoOnMessage(Session session, String msg) {
        logger.info("A new message is received: " + msg);
        try {
            session.getBasicRemote().sendText("ok");
        } catch (IOException e) {
            logger.warn("Something went wrong!", e);
        }
    }
}