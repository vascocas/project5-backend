package paj.project5_vc.websocket;

import jakarta.ejb.Singleton;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Singleton
@ServerEndpoint("/websocket/dashboard/{token}")
public class DashWeb {
    private static final Logger logger = LogManager.getLogger(DashWeb.class);
    HashMap<String, Session> dashSessions = new HashMap<>();

    public void send(String message) {
        // Iterate over all sessions
        for (Session s : dashSessions.values()) {
            try {
                if (s.isOpen()) {
                    // Send a dashboard WebSocket message
                    s.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                logger.warn("Something went wrong while sending the message", e);
            }
        }
    }

    @OnOpen
    public void toDoOnOpen(Session session, @PathParam("token") String token) {
        logger.info("A new WebSocket session is opened for client");
        dashSessions.put(token, session);
    }

    @OnClose
    public void toDoOnClose(Session session, CloseReason reason) {
        logger.info("Websocket session is closed with CloseCode: " +
                reason.getCloseCode() + ": " + reason.getReasonPhrase());
        // Create an iterator to safely remove elements
        Iterator<Map.Entry<String, Session>> iterator = dashSessions.entrySet().iterator();
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