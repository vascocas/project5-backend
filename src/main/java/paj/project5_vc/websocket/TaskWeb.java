package paj.project5_vc.websocket;


import jakarta.ejb.Singleton;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paj.project5_vc.dto.TaskStateDto;
import paj.project5_vc.dto.WebSocketMessage;

import java.io.IOException;
import java.util.HashMap;


@Singleton
@ServerEndpoint("/websocket/task/{token}")
public class TaskWeb {
    private static final Logger logger = LogManager.getLogger(TaskWeb.class);
    HashMap<String, Session> sessions = new HashMap<>();


    public void moveTask(TaskStateDto stateDto) {
        // Iterate over all sessions
        for (Session s : sessions.values()) {
            try {
                if(s.isOpen()) {
                    // When sending a task update WebSocket message
                    s.getBasicRemote().sendObject(new WebSocketMessage("update", stateDto));
                }
            } catch (IOException | EncodeException e) {
                logger.warn("Something went wrong while sending stateDto", e);
            }
        }
    }

    public void deleteTask(int taskId) {
        // Iterate over all sessions
        for (Session s : sessions.values()) {
            try {
                if(s.isOpen()) {
                    // When sending a task deletion WebSocket message
                    s.getBasicRemote().sendObject(new WebSocketMessage("delete", taskId));
                }
            } catch (IOException | EncodeException e) {
                logger.warn("Something went wrong while sending stateDto", e);
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
    public void onMessage(String message) {
        // Handle incoming websocket messages
        if (message.equals("update")) {
            // Handle logic for moving task
            // Send updates to all active clients
            broadcastUpdate(message);
        } else if (message.equals("delete")) {
            // Handle logic for deleting task
            // Send updates to all active clients
            broadcastDelete(message);
        }
    }

    private void broadcastUpdate(String message) {
        // Broadcast update message to all connected clients
        for (Session s : sessions.values()) {
            if (s.isOpen()) {
                s.getAsyncRemote().sendText(message);
            }
        }
    }

    private void broadcastDelete(String message) {
        // Broadcast delete message to all connected clients
        for (Session s : sessions.values()) {
            if (s.isOpen()) {
                s.getAsyncRemote().sendText(message);
            }
        }
    }

}
