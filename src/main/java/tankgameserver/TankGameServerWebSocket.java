package tankgameserver;

import websocketshared.Message;
import websocketshared.MessageDecoder;
import websocketshared.MessageEncoder;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint(value = "/tankgame", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class TankGameServerWebSocket {

    private static final List<Session> sessions = new ArrayList<>();

    @OnOpen
    public void onOpen(Session session){
        // TODO: Make logger

        System.out.println(session.toString());
        sessions.add(session);
        System.out.println(sessions.toString());
    }

    // Remove session upon closing
    @OnClose
    public void onClose(Session session){
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(Session session, Message message){
        System.out.println(message);
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