package tankgame.ai;

import tankgame.Coordinates;
import tankgame.ITankGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ai {

    private Random rand = new Random();
    private List<Coordinates> fireCoordinates = new ArrayList<>();
    private ITankGame tankGame;
    private int AIPlayerNr;

    // Constructor for Ai
    public Ai(ITankGame tankGame, int AIPlayerNr){
        this.tankGame = tankGame;
        this.AIPlayerNr = AIPlayerNr;
    }

    //@Override
    public boolean aiTurn(){
        Coordinates coordinates = null;
        boolean success = false;

        while (!success){
            // TODO: Implement, shot at random block
        }

        try {
            tankGame.fireShellAtPlayer(AIPlayerNr, coordinates.getX(), coordinates.getY());
            fireCoordinates.add(coordinates);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    // TODO: Make this more complicated
    private Coordinates createRandomCoordinates() {
        return new Coordinates(rand.nextInt(10), rand.nextInt(10));
    }
}