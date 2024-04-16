package paj.project5_vc.websocket;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paj.project5_vc.bean.UserBean;
import paj.project5_vc.dao.TokenDao;
import paj.project5_vc.dao.UserDao;
import paj.project5_vc.dto.MessageDto;
import paj.project5_vc.entity.TokenEntity;
import paj.project5_vc.entity.UserEntity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Singleton
@ServerEndpoint("/websocket/message/{token}")
public class MessageWeb {
    private static final Logger logger = LogManager.getLogger(MessageWeb.class);
    HashMap<String, Session> sessions = new HashMap<>();

    @EJB
    TokenDao tokenDao;

    public void send(String token, int receiverId, String message) {
        Session ownSession = sessions.get(token);
        if (ownSession != null) {
            try {
                // Send the message to the session
                ownSession.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.warn("Something went wrong while sending message to sender", e);
            }
        }
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
                                // Send the message to the session
                                s.getBasicRemote().sendText(message);
                            } catch (IOException e) {
                                logger.warn("Something went wrong while sending message to receiver", e);
                            }
                        }
                    }
                }
            }
        }
    }

    public void markRead(String message) {
        // Iterate over all sessions
        for (Session s : sessions.values()) {
            if (s.isOpen()) {
                try {
                    // Send the message to the session
                    s.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    logger.warn("Something went wrong while sending message to receiver", e);
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
        logger.info("A new message is received: " + msg);
        try {
            session.getBasicRemote().sendText("ok");
        } catch (IOException e) {
            logger.warn("Something went wrong!", e);
        }
    }
}