package server.actions;

import server.models.Player;

import java.util.List;

public class GameData implements IAction {

    private List<Player> allPlayers;

    public GameData(List<Player> players) {
        this.allPlayers = players;
    }

    public List<Player> getPlayers() {
        return allPlayers;
    }

    public void setPlayers(List<Player> players) {
        this.allPlayers = players;
    }
}