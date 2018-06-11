package server.messageHandler;

public interface IServerMessageHandler {
    void handleMessage(String message, String sessionId);

    void disconnected(String sessionId);
}