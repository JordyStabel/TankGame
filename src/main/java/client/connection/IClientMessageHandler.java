package client.connection;

public interface IClientMessageHandler {
    void handleMessage(String message, String sessionId);

    void disconnected(String sessionId);
}