package tankgame.player;

public class Player {

    private String playerName;

    // Constructor for Player
    Player(String playerName){
        this.playerName = playerName;
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }
}