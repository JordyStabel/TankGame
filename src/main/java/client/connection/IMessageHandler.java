package client.connection;

public interface IMessageHandler {
    void handleMessage(String message, String sessionId);

    void disconnected(String sessionId);
}