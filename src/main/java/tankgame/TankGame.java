package tankgame;

import tankgame.ai.IAi;
import tankgame.map.Grid;
import tankgame.map.IGrid;
import tankgame.player.Player;
import tankgame.player.Player1;
import tankgame.player.Player2;
import tankgamegui.ITankGameGUI;
import tankgamegui.enums.BlockType;
import tankgamegui.enums.ShellType;
import tankgamegui.enums.TankType;

import java.io.IOException;
import java.util.Random;

public class TankGame implements ITankGame {

    private ITankGameGUI tankGame;
    private Player1 player1;
    private Player2 player2;
    private IAi ai;
    private Player currentPlayer;

//    private ISeaBattleHost host;
//    private ISeaBattleClient client;
//    private URLReader urlReader = new URLReader();
//    private boolean multi = false;
//    private boolean localReady = false;
//    private boolean clientReady = false;

    private int playerNr;

    private int mapWidth = 100;
    private int mapHeight = 10;

    private IGrid grid = new Grid(mapWidth, mapHeight);

    @Override
    public int registerPlayer(String playerName, ITankGameGUI application, boolean singlePlayerMode) {
        if (application != null){
            tankGame = application;
        }
        if (player1 == null && singlePlayerMode){
            return registerLocalPlayer(playerName);
        }
//        else if (!singlePlayerMode && player1 == null){
//            try {
//                return registerPlayer1ForMultiplayer(playerName, application, singlePlayerMode);
//            }
//        }
        return -1;
    }

    @Override
    public boolean placeTankAutomatically(int playerNr) {
        return false;
    }

    @Override
    public boolean placeTank(int playerNr, TankType tankType, int x, int y) {
        return false;
    }

    @Override
    public boolean removeTanks(int playerNr) {
        return false;
    }

    @Override
    public boolean notifyWhenReady(int playerNr) {
        return false;
    }

    @Override
    public ShellType fireShellAtPlayer(int playerNr, int x, int y) {
        return null;
    }

    @Override
    public ShellType fireShellAtOpponent(int playerNr) {
        return null;
    }

    @Override
    public boolean startNewGame(int playerNr) throws IOException {
        return false;
    }

    private int registerLocalPlayer(String playerName){
        player1 = new Player1(playerName);
        playerNr = new Random().nextInt(2);
        return playerNr;
    }

//    private int registerPlayer1ForMultiplayer(String name, ITankGame application, boolean singlePlayerMode) throws Exception {
//        // If it's multi and no users are registered, register self as:
//        if (Boolean.parseBoolean(urlReader.readUrl("http://localhost:8090/rest/needhost"))) {
//            //the host if there are no multiplayer games available
//            host = new SeaBattleHost(this);
//            urlReader.readUrl("http://localhost:8090/rest/sethost");
//            return registerLocalPlayer(name);
//        } else {
//            //the client on an already existing multiplayer game or
//            client = new SeaBattleClient(seaBattleGUI);
//            multi = true;
//            return client.registerPlayer(name, application, singlePlayerMode);
//        }
//    }

//    @Override
//    public boolean placeShipsAutomatically(int playerNr) {
//        if (client == null) {
//            //remove all ships from the grid by getting new grinds and resetting UI
//            removeAllShips(playerNr);
//            //create new randomly placed ships
//            for (ShipType shipType :
//                    ShipType.values()) {
//                automaticShipPlacer(playerNr, shipType);
//            }
//            return true;
//        } else {
//            return client.placeShipsAutomatically(playerNr);
//        }
//    }

    private boolean placeTank(TankType tankType, int posX, int posY, IGrid grid) {
        boolean success;
        success = grid.placeTank(tankType, posX, posY);
        return success;
    }

//    @Override
//    public boolean startNewGame(int playerNr) throws IOException {
//
//        if (host != null) {
//            urlReader.readUrl("http://localhost:8090/rest/unhost");
//        }
////        return true;
//        aiOpponent = null;
//        currentTurn = null;
//        client = null;
//        multi = false;
//        localReady = false;
//        clientReady = false;
//        self = null;
//        opponent = null;
//        initGrids(playerNr);
//        return true;
//    }

    private void startGrid(int mapWidth, int mapHeight){
        grid = new Grid(mapWidth, mapHeight);
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                tankGame.showSquarePlayer(playerNr, i, j, BlockType.GROUND);
            }
        }
    }
}