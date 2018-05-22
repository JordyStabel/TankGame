package tankgamegui;

import tankgame.block.Block;
import tankgamegui.enums.BlockType;
import tankgamegui.enums.ShellType;

public interface ITankGameGUI {
    /**
     * Set the name of the player in the GUI.
     * @param playerNr identification of player
     * @param name player's name
     */
    public void setPlayerName(int playerNr, String name);

    /**
     * Set the name of the opponent in the GUI.
     * @param playerNr identification of player
     * @param name opponent's name
     */
    public void setOpponentName(int playerNr, String name);

    /**
     * Communicate the result of a shot fired by the opponent.
     * The result of the shot will be one of the following:
     * MISSED  - No ship was hit
     * HIT     - A ship was hit
     * SUNK    - A ship was sunk
     * ALLSUNK - All ships are sunk
     * @param playerNr identification of player
     * @param shellType result of shot fired by opponent
     */
    public void opponentFiresShot(int playerNr, ShellType shellType);

    /**
     * Show state of a square in the ocean area.
     * The new state of the square will be shown in the area where
     * the ships of the player are placed (ocean area).
     * @param playerNr identification of player
     * @param posX        x-position of square
     * @param posY        y-position of square
     * @param blockType state of square
     */
    public void showSquarePlayer(int playerNr, int posX, int posY, BlockType blockType);

    /**
     * Show state of a square in the target area.
     * The new state of the square will be shown in the area where
     * the ships of the opponent are placed (target area)
     * @param playerNr identification of player
     * @param posX        x-position of square
     * @param posY        y-position of square
     * @param blockType state of square
     */
    public void showSquareOpponent(int playerNr, int posX, int posY, BlockType blockType);

    // Start the game
    void startGame();
}
