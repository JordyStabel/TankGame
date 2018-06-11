package server.customexceptions;

public class GameAlreadyStarted extends Exception {
    // Empty Constructor
    public GameAlreadyStarted() {}

    // Constructor that accepts a message
    public GameAlreadyStarted(String message)
    {
        super(message);
    }
}