package server.messageHandler;

import com.google.gson.Gson;
import server.actions.Actions;
import server.actions.Message;
import server.models.TankGame;

public class ServerMessageHandler implements IServerMessageHandler {

    private TankGame tankGame;

    public ServerMessageHandler(TankGame tankGame) {
        this.tankGame = tankGame;
    }

    @Override
    public void handleMessage(String messageString, String sessionId) {
        Gson gson = new Gson();
        Message message = gson.fromJson(messageString,Message.class);

        switch (message.getAction()){
            case REGISTER:
                Register register = (Register) message.parseData(Register.class);
                registerPlayer(register, sessionId);
                break;
            case READY:
                playerReady(sessionId);
                break;
            case UP:
                moveSnake(Actions.UP, sessionId);
                break;
            case LEFT:
                moveSnake(Actions.LEFT, sessionId);
                break;
            case DOWN:
                moveSnake(Actions.DOWN, sessionId);
                break;
            case RIGHT:
                moveSnake(Actions.RIGHT, sessionId);
                break;
            case PING:
                ping(sessionId);
                break;


        }
    }

    @Override
    public void disconnected(String sessionId) {
        game.removePlayer(sessionId);
    }

    private void ping(String sessionId){
        game.ping(sessionId);
    }

    private void playerReady(String sessionId){
        game.playerReady(sessionId);
    }

    private void moveSnake(Actions action, String sessionId){
        Player player = game.findPlayer(sessionId);
        if(player == null){
            throw new IllegalArgumentException("No player found");
        }
        switch (action) {
            case LEFT:
                player.getSnake().left();
                break;
            case DOWN:
                player.getSnake().down();
                break;
            case RIGHT:
                player.getSnake().right();
                break;
            case UP:
                player.getSnake().up();
                break;
            default:
                throw new IllegalArgumentException("No direction found");
        }
    }

    private void registerPlayer(Register register, String sessionId){
        if(game.findPlayer(sessionId) == null){
            Player player = new Player(
                    sessionId,
                    register.getName(),
                    new Snake(
                            (int) (Math.random()*game.getWidth()),
                            (int) (Math.random()*game.getHeight()),
                            register.getColor()));

            game.addPlayer(player);
        }
    }

}