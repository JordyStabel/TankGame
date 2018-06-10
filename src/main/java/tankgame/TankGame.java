package tankgame;

import tankgame.map.Grid;
import tankgame.map.IGrid;
import tankgame.player.Opponent;
import tankgame.player.Player;
import tankgame.player.Self;
import tankgameclient.ITankGameClient;
import tankgameclient.ITankGameHost;
import tankgameclient.TankGameClient;
import tankgameclient.TankGameHost;
import tankgamegui.ITankGameGUI;

import java.io.IOException;
import java.util.Random;

public class TankGame implements ITankGame {

    private ITankGameGUI tankGameGUI;
    private Self self;
    private Opponent opponent;
    private Player currentPlayer;

    private ITankGameHost host;
    private ITankGameClient client;
    private URLReader urlReader = new URLReader();
    private boolean multi = false;
    private boolean localReady = false;
    private boolean clientReady = false;

    private int localPlayerNr;

    private int mapWidth = 100;
    private int mapHeight = 10;

    private IGrid grid = new Grid(mapWidth, mapHeight);

    @Override
    public int registerPlayer(String playerName, ITankGameGUI application) {
        if (application != null){
            tankGameGUI = application;
        }
        if (self == null){
            try {
                return registerSelfForMultiplayer(playerName, application);
            }
            catch (Exception e){
                e.printStackTrace();
                return -1;
            }
        } else if (host != null && opponent == null) {
            return registerOpponentForMulti(playerName);
        }
        return -1;
    }


    @Override
    public boolean notifyWhenReady(int playerNr) {
        if (client != null) {
            return client.notifyWhenReady(playerNr);
        }
        return notifyWhenReadySingleOrServer(playerNr);
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

    private int registerOpponentForMulti(String name) {
        multi = true;
        opponent = new Opponent(name);
        tankGameGUI.setOpponentName(localPlayerNr, opponent.getPlayerName());

        return 1 - localPlayerNr;
    }

    private int registerLocalPlayer(String playerName){
        self = new Self(playerName);
        localPlayerNr = new Random().nextInt(2);
        return localPlayerNr;
    }

    private boolean notifyWhenReadySingleplayer() {
        //if yes then create an AI opponent

        opponent = new Opponent("AI");
        tankGameGUI.setOpponentName(localPlayerNr, opponent.getPlayerName());
        // and startSocket the game
        singleplayerSetStartingPlayer();
        return true;
    }

    private void singleplayerSetStartingPlayer() {
        if (localPlayerNr == 0) {
            currentPlayer = self;
        } else {
            currentPlayer = opponent;
        }
    }

    private int registerSelfForMultiplayer(String name, ITankGameGUI application) throws Exception {
        // If it's multi and no users are registered, register self as:
        if (Boolean.parseBoolean(urlReader.readUrl("http://localhost:8090/rest/needhost"))) {
            //the host if there are no multiplayer games available
            host = new TankGameHost(this);
            urlReader.readUrl("http://localhost:8090/rest/sethost");
            return registerLocalPlayer(name);
        } else {
            //the client on an already existing multiplayer game or
            client = new TankGameClient(tankGameGUI);
            multi = true;
            return client.registerPlayer(name, application);
        }
    }

    private boolean notifyWhenReadySingleOrServer(int playerNr) {
        if (playerNr == localPlayerNr && !multi) {
            return notifyWhenReadySingleplayer();
        } else if (playerNr == localPlayerNr) {
            if (notifyWhenReadyMultiLocal()) return false;
        } else if (playerNr == 1 - localPlayerNr) {
            if (notifyWhenReadyMultiClient()) return false;
        }
        return notifyWhenReadyMultiCheckBoth(playerNr);
    }

    private boolean notifyWhenReadyMultiCheckBoth(int playerNr) {
        if (localReady && clientReady) {
            //if both players are ready in multi, startSocket the game
            setStartingPlayer();
            tellOtherPlayerWhoStarts(playerNr);
            return true;
        }
        return false;
    }

    private void tellOtherPlayerWhoStarts(int playerNr) {
        if (playerNr == localPlayerNr) {
            host.startGame();
        } else {
            tankGameGUI.startGame();
        }
    }

    private boolean notifyWhenReadyMultiClient() {
        //if opponent player readies in multi, set client to ready
        if (grid.tankPlaced()) {
            clientReady = true;
        } else return true;
        return false;
    }

    private boolean notifyWhenReadyMultiLocal() {
        //if local player readies in multi, set local to ready
        if (grid.tankPlaced()) {
            localReady = true;
        } else return true;
        return false;
    }

    private void setStartingPlayer() {
        if (localPlayerNr == 0) {
            currentPlayer = self;
        } else {
            currentPlayer = opponent;
        }
    }

    private void startGrid(int mapWidth, int mapHeight){
        grid = new Grid(mapWidth, mapHeight);
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                //tankGame.showSquarePlayer(playerNr, i, j, BlockType.GROUND);
            }
        }
    }
}