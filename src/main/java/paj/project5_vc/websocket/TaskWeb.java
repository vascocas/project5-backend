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
@ServerEndpoint("/websocket/task/{token}")
public class TaskWeb {
    private static final Logger logger = LogManager.getLogger(TaskWeb.class);
    HashMap<String, Session> taskSessions = new HashMap<>();


    public void taskChange(String token, String taskAction) {
        Session ownSession = taskSessions.get(token);
        // Iterate over all sessions
        for (Session s : taskSessions.values()) {
            try {
                if(s!=ownSession) {
                    if (s.isOpen()) {
                        // Send a task WebSocket message
                        s.getBasicRemote().sendText(taskAction);
                    }
                }
            } catch (IOException e) {
                logger.warn("Something went wrong while sending the message", e);
            }
        }
    }

    @OnOpen
    public void toDoOnOpen(Session session, @PathParam("token") String token) {
        logger.info("A new WebSocket session is opened for client");
        taskSessions.put(token, session);
    }

    @OnClose
    public void toDoOnClose(Session session, CloseReason reason) {
        logger.info("Websocket session is closed with CloseCode: " +
                reason.getCloseCode() + ": " + reason.getReasonPhrase());
        // Create an iterator to safely remove elements
        Iterator<Map.Entry<String, Session>> iterator = taskSessions.entrySet().iterator();
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
