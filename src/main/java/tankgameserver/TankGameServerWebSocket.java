package tankgameserver;

import websocketshared.Message;
import websocketshared.MessageDecoder;
import websocketshared.MessageEncoder;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ServerEndpoint(value = "/tankgame", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class TankGameServerWebSocket {

    private static final List<Session> sessions = new ArrayList<>();
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @OnOpen
    public void onOpen(Session session){

        LOGGER.info(session.toString());
        sessions.add(session);
        LOGGER.info(sessions.toString());
    }

    // Remove session upon closing
    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(Session session, Message message){
        LOGGER.info(message.toString());
        BroadCast(message, session);
    }

    // Check for sessions and return the responds message
    void BroadCast(Message responds, Session session){
        for (Session _session : sessions){
            if (!session.equals(_session)){
                _session.getAsyncRemote().sendObject(responds);
            }
        }
    }
}