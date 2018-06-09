package tankgamegui;

public interface ITankGameGUI {

    void setPlayerName(int playerNr, String playerName);

    void setOpponentName(int playerNr, String playerName);

    void startGame();
}