package server.messageHandler;

import com.google.gson.Gson;
import server.actions.Actions;
import server.actions.Message;
import server.actions.Register;
import server.models.Player;
import server.models.Tank;
import server.models.TankGame;

import static processing.core.PApplet.println;

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
            case LEFT:
                moveTank(Actions.LEFT, sessionId);
                println(sessionId);
                break;
            case STOPLEFT:
                moveTank(Actions.STOPLEFT, sessionId);
                println(sessionId);
                break;
            case RIGHT:
                moveTank(Actions.RIGHT, sessionId);
                break;
            case STOPRIGHT:
                moveTank(Actions.STOPRIGHT, sessionId);
                break;
        }
    }

    @Override
    public void disconnected(String sessionId) {
        tankGame.removePlayer(sessionId);
    }

    private void playerReady(String sessionId){
        tankGame.playerReady(sessionId);
    }

    private void moveTank(Actions action, String sessionId){
        Player player = tankGame.findPlayer(sessionId);
        if(player == null){
            throw new IllegalArgumentException("No player found");
        }
        switch (action) {
            case LEFT:
                tankGame.moveLeft(sessionId);
                break;
            case STOPLEFT:
                tankGame.stopLeft(sessionId);
                break;
            case RIGHT:
                tankGame.moveRight(sessionId);
                break;
            case STOPRIGHT:
                tankGame.stopRight(sessionId);
                break;
            default:
                throw new IllegalArgumentException("No direction found");
        }
    }

    private void registerPlayer(Register register, String sessionId){
        if(tankGame.findPlayer(sessionId) == null){
            Player player = new Player(sessionId, register.getPlayerName(), new Tank());
            tankGame.addPlayer(player);
        }
    }
}