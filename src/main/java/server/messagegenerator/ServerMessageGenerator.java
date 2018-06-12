package server.messagegenerator;

import server.actions.GameData;
import server.actions.Message;
import server.actions.Actions;
import server.models.TankGame;
import server.websocket.IServerWebSocket;

import java.util.logging.Level;
import java.util.logging.Logger;

import static processing.core.PApplet.println;

public class ServerMessageGenerator implements IServerMessageGenerator {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private IServerWebSocket serverSocket;
    private TankGame tankGame;

    public ServerMessageGenerator(Class <? extends IServerWebSocket> serverSocket) {
        try {
            this.serverSocket = serverSocket.newInstance();
        } catch (InstantiationException e) {
            LOGGER.log(Level.SEVERE, "Error: ", e);
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Error: ", e);
        }
    }

    @Override
    public TankGame getTankGame() {
        return tankGame;
    }

    @Override
    public void setGame(TankGame tankGame) {
        this.tankGame = tankGame;
    }

    @Override
    public void updateGame(){
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void updatePlayers(){
        GameData gameData = new GameData(tankGame.getPlayers());
        Message message = new Message(Actions.GAMEDATA, gameData);
        serverSocket.broadcast(message);
    }

    @Override
    public void run() {
//        Field field = new Field(tankGame.getWidth(), tankGame.getHeight());
//        for (int i = 0; i < tankGame.getPlayers().size(); i++) {
//            Player player = tankGame.getPlayers().get(i);
//            Snake snake = player.getSnake();
//            field.setColor(snake.getX(), snake.getY(), snake.getSnakeColor());
//            for (int j = 0; j < snake.getTail().size(); j++) {
//                Tail tail = snake.getTail().get(i);
//                field.setColor(tail.getX(), tail.getY(), snake.getTailColor());
//            }
//        }
//        serverSocket.broadcast(field);
    }

    @Override
    public void playerMoveRight() {
        Message message = new Message(Actions.RIGHT);
        serverSocket.broadcast(message);
        println("Move Right");
    }

    @Override
    public void playerStopRight() {
        Message message = new Message(Actions.STOPRIGHT);
        serverSocket.broadcast(message);
        println("Stop Right");
    }
}