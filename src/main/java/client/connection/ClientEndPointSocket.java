package client.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

@ClientEndpoint
public class ClientEndPointSocket {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private IMessageHandler messageHandler;
    private Session session;

    public ClientEndpointSocket() {
    }

    public void connect(String ip) {
        URI uri = URI.create("ws://" + ip);
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, uri);

        } catch (DeploymentException | IOException e) {
            LOGGER.log(Level.SEVERE, "Can't connect. ", e);
        }
    }

    @OnMessage
    public void onText(String message) {
        if(messageHandler == null){
            throw new NullPointerException("No messageHandler found");
        }

        if(!message.isEmpty()) {
            messageHandler.handleMessage(message);
        }
    }


    public void setMessageHandler(IMessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void sendMessage(Message message) {
        try {
            Gson json = new GsonBuilder().create();
            String jsontosend = json.toJson(message);
            session.getBasicRemote().sendText(jsontosend);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Can't send message. ", e);
        }
    }
}
