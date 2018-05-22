package tankgamegui.enums;

public enum BlockType {
    WATER,      // Block of water, will destroy tanks
    GROUND,     // Ground block, tanks can't be placed on this block
    SURFACE,    // Surface block, tanks can be placed on this block
    TANK        // Tank block, this represents a tank
}