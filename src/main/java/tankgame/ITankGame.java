package tankgame;

import tankgamegui.ITankGameGUI;
import tankgamegui.enums.ShellType;
import tankgamegui.enums.TankType;

import java.io.IOException;

public interface ITankGame {

    // Register a player and the selected game mode
    int registerPlayer(String name, ITankGameGUI application, boolean singlePlayerMode) throws Exception;

    // Automatically place a tank on the grid
    boolean placeTankAutomatically(int playerNr);

    // Place a tank on the grid
    boolean placeTank(int playerNr, TankType tankType, int x, int y);

    // Remove tank(s) of playerNr from the grid
    boolean removeTanks(int playerNr);

    // Notify when player is done placing the tank(s)
    boolean notifyWhenReady(int playerNr);

    // Fire a shell at player
    ShellType fireShellAtPlayer(int playerNr, int x, int y);

    // Fire a shell in single player only
    ShellType fireShellAtOpponent(int playerNr);

    // Start new game
    boolean startNewGame(int playerNr) throws IOException;
}