package tankgame.block;

import java.util.Observable;

abstract class Block {
    private String stateName;
    private int width;
    private int height;
    private boolean destroyed = false;

    public String getStateName() {
        return stateName;
    }

    /**
     * Constructor for Tile
     * @param stateName String the state the tile should be in
     */
    Block(String stateName) {
        this.stateName = stateName;
        this.width = 5;
    }
}