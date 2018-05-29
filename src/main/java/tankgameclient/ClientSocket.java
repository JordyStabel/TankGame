package tankgameclient;

import websocketshared.Message;
import websocketshared.MessageDecoder;
import websocketshared.MessageEncoder;
import websocketshared.MethodCaller;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint(decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ClientSocket implements ICommunicator{
    private Object controller;
    private MethodCaller methodCaller;
    private String uri = "";
    private Session session;
    private boolean isRunning = false;
    private static ClientSocket instance = null;
    private Message lastMessage;

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    private ClientSocket() {
        methodCaller = new MethodCaller();
    }

    public static ClientSocket getInstance() {
        if (instance == null) {
            System.out.println("[WebSocket Client create singleton instance]");
            instance = new ClientSocket();
        }
        return instance;
    }

    @Override
    public void startSocket(String uri) {
        System.out.println("[WebSocket ClientSocket startSocket]");
        this.uri = uri;
        if (!isRunning) {
            isRunning = true;
            startClient();
        }
    }

    @Override
    public void stop() throws IOException {
        session.close();
    }


    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void onMessage(Session session, Message message) {
        lastMessage = message;
        if (message.getMessageType().equalsIgnoreCase("command")) {
            methodCaller.reflectiveMethodCaller(message, controller);
        } else if (message.getMessageType().equalsIgnoreCase("return")) {
            //Implement unlocking mechanism here
        }

    }

    @Override
    public void broadcastMessage(Message message) {
        session.getAsyncRemote().sendObject(message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("[WebSocket ClientSocket closed]");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private void startClient() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));
            System.out.println("[WebSocket ClientSocket connected]");

        } catch (Exception ex) {
            // do something useful eventually
            ex.printStackTrace();
        }
    }

    @Override
    public void setController(Object controller) {
        this.controller = controller;
    }
}
