package server.models;

public class Player {

    private String playerName;
    private String playerID;
    protected Tank tank;
    private boolean isReady = false;

    public Player(String playerID, String playerName, Tank tank){
        this.playerName = playerName;
        this.playerID = playerID;
        this.tank = tank;
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Tank getTank() {
        return tank;
    }

    public boolean getIsReady() {
        return isReady;
    }

    public void ready(){
        isReady = true;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(!(obj instanceof Player)) return false;
        Player other = (Player) obj;
        return this.getPlayerID().equals(other.getPlayerID());
    }
}