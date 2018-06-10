package tankgameclient;

import tankgamegui.ITankGameGUI;
import websocketshared.Message;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class TankGameClient implements ITankGameClient {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private ITankGameGUI tankGameGUI;
    private final ICommunicator communicator;

    public TankGameClient(ITankGameGUI tankGameGUI) throws Exception{
        this.tankGameGUI = tankGameGUI;
        communicator = ClientSocket.getInstance();
        communicator.setController(this);
        communicator.startSocket("ws://localhost:8095/tankgame");
    }

    private Object messageSender(String methodName, Object[] parameters){
        Message message = new Message();
        message.setMessageType("command");
        message.setMethodName(methodName);
        message.setParameters(parameters);
        message = sendWait(message);
        Object returnValue = message.getResponds();
//        for (SquareState c : SquareState.values()) {
//            if (c.name().equals(returnValue)) {
//                returnValue = c;
//                return returnValue;
//            }
//        }
//        for (ShotType c : ShotType.values()) {
//            if (c.name().equals(returnValue)) {
//                returnValue=c;
//                return returnValue;
//            }
//        }
        return returnValue;
    }

    private Message sendWait(Message message){
        communicator.broadcastMessage(message);
        try{
            sleep(200);
        }
        catch (InterruptedException e){
            LOGGER.log(Level.INFO, "Failed to wait", e);
            Thread.currentThread().interrupt();
        }
        return communicator.getLastMessage();
    }

    @Override
    public int registerPlayer(String name, ITankGameGUI application) throws Exception {
        return (((Double) messageSender("registerPlayer", new Object[]{name})).intValue());
    }

    @Override
    public boolean notifyWhenReady(int playerNr) {
        return false;
    }

    @Override
    public void fireShellAtPlayer(int playerNr, int x, int y) {

    }

    @Override
    public void fireShellAtOpponent(int playerNr) {

    }

    @Override
    public boolean startNewGame(int playerNr) throws IOException {
        return false;
    }
}