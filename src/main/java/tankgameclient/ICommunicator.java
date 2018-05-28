package tankgameclient;

import websocketshared.Message;
import java.io.IOException;

public interface ICommunicator {

    void setController(Object controller);

    void startSocket(String uri);

    void stop() throws IOException;

    void broadcastMessage(Message message);

    Message getLastMessage();
}