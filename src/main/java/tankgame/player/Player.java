package tankgame.player;

import client.game.tankgameobjects.PlayerObject;

public class Player {

    private String playerName;
    private PlayerObject playerObject;

    // Constructor for PlayerObject
    Player(String playerName){
        this.playerName = playerName;
    }

    public String getPlayerName(){
        return playerName;
    }

    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }

    public PlayerObject getPlayerObject(){
        return playerObject;
    }

    public void setPlayerObject(PlayerObject playerObject){
        this.playerObject = playerObject;
    }
}