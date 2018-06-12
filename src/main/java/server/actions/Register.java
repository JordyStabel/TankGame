package server.actions;

public class Register implements IAction {

    private String playerName;

    public Register(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}