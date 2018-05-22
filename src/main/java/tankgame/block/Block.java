package tankgame.block;

public class Block {

    private String blockType;
    private boolean isDestroyed;

    public String getStateName() {
        return blockType;
    }

    // Constructor for Block
    Block(String blockType) {
        this.blockType = blockType;
        this.isDestroyed = false;
    }
}