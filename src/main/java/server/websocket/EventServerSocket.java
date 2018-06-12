package server.websocket;

import server.messageHandler.IServerMessageHandler;
import server.response.Json;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/tankgame/")
public class EventServerSocket implements IServerWebSocket {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static Map<String, Session> map = new HashMap<>();
    private static Map<String, Session> sessions = Collections.synchronizedMap(map);

    private static IServerMessageHandler messageHandler;

    public static IServerMessageHandler getMessageHandler() {
        return messageHandler;
    }

    public static void setMessageHandler(IServerMessageHandler messageHandler) {
        EventServerSocket.messageHandler = messageHandler;
    }

    @OnOpen
    public void onConnect(Session session) {
        LOGGER.log(Level.INFO,"[Connected] SessionID:" + session.getId());
        String message = String.format("[New client with client side session ID]: %s", session.getId());
        broadcast(message, session.getId());
        sessions.put(session.getId(), session);
        String temp = "[#sessions]: " + sessions.size();
        LOGGER.log(Level.INFO, temp);
    }

    @OnMessage
    public void onText(String message, Session session) {
        //System.out.println("[Received] From : " + session.getId() + " | Content  : " + message);
        if (messageHandler == null) {
            throw new NullPointerException("No messageHandler found");
        }

        if (message.isEmpty()) {
            sendMessage("No message found", session, session.getId());
            return;
        }

        messageHandler.handleMessage(message, session.getId());
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        String temp = "[Session ID] : " + session.getId() + "[Socket Closed: " + reason;
        LOGGER.log(Level.INFO, temp);
        sessions.remove(session.getId());
        messageHandler.disconnected(session.getId());

    }

    @OnError
    public void onError(Throwable cause, Session session) {
        String temp = "[Session ID] : " + session.getId() + "[ERROR]: ";
        LOGGER.log(Level.INFO, temp);
        cause.printStackTrace(System.err);
    }

    public void broadcast(String s, String session) {
        for (Iterator<Map.Entry<String, Session>> iterator = sessions.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, Session> entry = iterator.next();
            sendMessage(s, entry.getValue(), session);
        }
    }

    private synchronized void sendMessage(String message, Session session, String id) {
        if (message == null || session == null) {
            throw new IllegalArgumentException("Message or session can't be null");
        }
        if (id != session.getId()) //!message.toLowerCase().contains(session.getId().toLowerCase())
        {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendTo(String sessionId, Json object) {
        sendMessage(object.convertToJson(), sessions.get(sessionId), sessionId);
    }

    @Override
    public void broadcast(Json object, String session) {
        broadcast(object.convertToJson(), session);
    }

    @Override
    public void sendToOthers(String sessionId, Json object) {

    }
}