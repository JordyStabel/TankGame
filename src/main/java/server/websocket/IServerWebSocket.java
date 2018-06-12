package server.websocket;

import server.response.Json;

public interface IServerWebSocket {

    void sendTo(String sessionId, Json object);

    void broadcast(Json object, String session);

    void sendToOthers(String sessionId, Json object);
}