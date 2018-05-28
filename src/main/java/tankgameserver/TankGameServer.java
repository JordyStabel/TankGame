package tankgameserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.client.io.WebSocketClientSelectorManager;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

public class TankGameServer {

    private static final int WEBPORT = 8095;
    private static final int RESTPORT = 8090;
    private static Server webSocketServer;
    private static Server jettyServer;
    private static ServletContextHandler webSocketContext;

    public static void main(String[] args) throws Throwable{
        createRestServer();
        createWebSocketServer();
        ServerContainer webSocketContainer = WebSocketServerContainerInitializer.configureContext(webSocketContext);
        webSocketContainer.addEndpoint(TankGameServerWebSocket.class);
    }
}