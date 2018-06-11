package server.customexceptions;

public class PlayerNotReady extends Exception {
    // Parameterless Constructor
    public PlayerNotReady() {}

    // Constructor that accepts a message
    public PlayerNotReady(String playerName)
    {
        super(String.format("Player %s isn't ready", playerName));
    }
}