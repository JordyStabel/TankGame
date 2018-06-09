package tankgameserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.glassfish.jersey.servlet.ServletContainer;
import javax.websocket.server.ServerContainer;

public class TankGameServer {

    private static final int WEBPORT = 8095;
    private static final int RESTPORT = 8090;
    private static Server webSocketServer;
    private static Server jettyServer;
    private static ServletContextHandler webSocketContext;

    public static void main(String[] args) throws Throwable {
        createRestServer();
        createWebSocketServer();
        // Initialize javax.websocket layer
        ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(webSocketContext);

        // Add WebSocket endpoint to javax.websocket layer
        wscontainer.addEndpoint(TankGameServerWebSocket.class);

        try {
            webSocketServer.start();
            jettyServer.start();

            webSocketServer.join();
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jettyServer.destroy();
            webSocketServer.destroy();
        }
    }

    private static void createRestServer() {
        ServletContextHandler context = new
                ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        jettyServer = new Server(RESTPORT);
        jettyServer.setHandler(context);
        ServletHolder jerseyServlet =
                context.addServlet(ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);
        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames",
                TankGameServerService.class.getCanonicalName());
    }

    // Start the web socket server
    private static void createWebSocketServer() {

        webSocketServer = new Server();
        ServerConnector connector = new ServerConnector(webSocketServer);
        connector.setPort(WEBPORT);
        webSocketServer.addConnector(connector);

        // Setup the basic application "context" for this application at "/"
        // This is also known as the handler tree (in jetty speak)
        webSocketContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        webSocketContext.setContextPath("/");
        webSocketServer.setHandler(webSocketContext);
    }
}