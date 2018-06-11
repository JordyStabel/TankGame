package server;

import client.game.TankGame;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import server.messagegenerator.IServerMessageGenerator;
import server.messagegenerator.ServerMessageGenerator;
import server.websocket.EventServerSocket;

import javax.websocket.server.ServerContainer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void main(String[] args){
        startWebSocketServer();
    }

    private static void startWebSocketServer() {
        LOGGER.log(Level.INFO, "Starting websocket");

        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(9090);
        server.addConnector(connector);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);


        IServerMessageGenerator iMessageGenerator = new ServerMessageGenerator(EventServerSocket.class);
        TankGame game = new TankGame(iMessageGenerator, 50,50);
        iMessageGenerator.setGame(game);

        GameMessageHandler messageHandler = new GameMessageHandler(game);
        try {
            ServerContainer container = WebSocketServerContainerInitializer.configureContext(context);
            EventServerSocket.setMessageHandler(messageHandler);
            container.addEndpoint(EventServerSocket.class);
            server.start();
            LOGGER.log(Level.INFO, "Websocket started on: " + server.getURI());
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}