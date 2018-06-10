package tankgameclient;

import tankgamegui.ITankGameGUI;
import websocketshared.Message;

import java.io.IOException;

import static java.lang.Thread.sleep;

public class TankGameClient implements ITankGameClient {

    private ITankGameGUI tankGameGUI;
    private final ICommunicator communicator;

    public TankGameClient(ITankGameGUI tankgameGUI) throws Exception{
        this.tankGameGUI = tankgameGUI;
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
        Object responds = message.getResponds();
        return responds;
    }

    private Message sendWait(Message message){
        communicator.broadcastMessage(message);
        try{
            sleep(300);
        }
        catch (InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        message = communicator.getLastMessage();

        return message;
    }

    @Override
    public int registerPlayer(String name, ITankGameGUI application, boolean singlePlayerMode) throws Exception {
        return (((Double) messageSender("registerPlayer", new Object[]{name, singlePlayerMode})).intValue());
    }

    @Override
    public boolean jump(int playerNr) {
        return ((boolean) messageSender("jump", new Object[]{playerNr}));
    }

    @Override
    public boolean notifyWhenReady(int playerNr) {
        return ((boolean) messageSender("notifyWhenReady", new Object[]{playerNr}));
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

    public void startGame() {
        tankGameGUI.startGame();
    }

    public void setPlayerName(int playerNr, String name) {
        tankGameGUI.setPlayerName(playerNr, name);
    }

    public void setOpponentName(int playerNr, String name) {
        tankGameGUI.setPlayerName(playerNr, name);
    }
}