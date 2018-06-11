package server.websocket;

import server.response.Json;

public interface IServerWebSocket {

    void sendTo(String sessionId, Json object);

    void broadcast(Json object);

    void sendToOthers(String sessionId, Json object);
}