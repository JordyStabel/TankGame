package server.messagegenerator;

import server.actions.Actions;
import server.actions.GameData;
import server.actions.Message;
import server.models.TankGame;
import server.websocket.IServerWebSocket;

import java.util.logging.Level;
import java.util.logging.Logger;

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
        serverSocket.broadcast(message, null);
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
    public void playerJump(String id) {
        Message message = new Message(Actions.JUMP, id);
        serverSocket.broadcast(message, id);
    }


    @Override
    public void playerMoveLeft(String id) {
        Message message = new Message(Actions.LEFT, id);
        serverSocket.broadcast(message, id);
    }

    @Override
    public void playerStopLeft(String id) {
        Message message = new Message(Actions.STOPLEFT, id);
        serverSocket.broadcast(message, id);
    }

    @Override
    public void playerMoveRight(String id) {
        Message message = new Message(Actions.RIGHT, id);
        serverSocket.broadcast(message, id);
    }

    @Override
    public void playerStopRight(String id) {
        Message message = new Message(Actions.STOPRIGHT, id);
        serverSocket.broadcast(message, id);
    }
}