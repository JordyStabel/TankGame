package tankgame;

import tankgamegui.ITankGameGUI;

import java.io.IOException;

public interface ITankGame {

    // Register a player and the selected game mode
    int registerPlayer(String name, ITankGameGUI application, boolean singlePlayerMode) throws Exception;

    // Lets player jump
    boolean jump(int playerNr);

    // Notify when player is done placing the tank(s)
    boolean notifyWhenReady(int playerNr);

    // Fire a shell at player
    void fireShellAtPlayer(int playerNr, int x, int y);

    // Fire a shell in single player only
    void fireShellAtOpponent(int playerNr);

    // Start new game
    boolean startNewGame(int playerNr) throws IOException;
}