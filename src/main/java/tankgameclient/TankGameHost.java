package tankgameclient;

import tankgame.ITankGame;
import tankgamegui.enums.BlockType;
import tankgamegui.enums.ShellType;
import websocketshared.Message;

public class TankGameHost implements ITankGameHost {

    private ITankGame tankGame;
    private ICommunicator communicator;

    public TankGameHost(ITankGame tankGame) throws Exception {
        this.tankGame = tankGame;
        communicator = ClientSocket.getInstance();
        communicator.setController(this);
        communicator.startSocket("ws://localhost:8095/tankgame");
    }

    private void returnMessage(Object object) {
        Message message = new Message();
        message.setMessageType("return");
        message.setResponds(object);
        communicator.broadcastMessage(message);
    }

    private void messageSender(String methodName, Object[] parameters) {
        Message message = new Message();
        message.setMessageType("command");
        message.setMethodName(methodName);
        message.setParameters(parameters);
        communicator.broadcastMessage(message);
    }

    @Override
    public void setPlayerName(int playerNr, String name) {
        messageSender("setPlayerName", new Object[]{playerNr, name});
    }

    @Override
    public void setOpponentName(int playerNr, String name) {

    }

    @Override
    public void opponentFiresShot(int playerNr, ShellType shellType) {

    }

    @Override
    public void showSquarePlayer(int playerNr, int posX, int posY, BlockType blockType) {

    }

    @Override
    public void showSquareOpponent(int playerNr, int posX, int posY, BlockType blockType) {

    }

    @Override
    public void startGame() {

    }
}