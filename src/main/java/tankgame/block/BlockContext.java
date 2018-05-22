package tankgame.block;

import tankgame.Coordinates;
import tankgame.shell.Shell;
import tankgamegui.enums.BlockType;

import java.util.Observable;

public class BlockContext extends Observable implements IBlockContext {

    private Block block;
    private Coordinates coordinates;

    // Constructor for BlockContext
    public BlockContext(Coordinates coordinates) {
        this.coordinates = coordinates;
        block = new Block(BlockType.GROUND.toString());
    }

    // Return Coordinates of this Block
    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getBlockState() {
        return block.getStateName();
    }

    public Shell shootAt() {
        // TODO: Shoot at a target
        return null;
    }

    public Coordinates getBlockCoordinates() {
        return null;
    }

    public void setToWater() {
        block = new Block(BlockType.WATER.toString());
    }

    public void setToSurface() {
        block = new Block(BlockType.SURFACE.toString());
    }

    public void setToGround() {
        block = new Block(BlockType.GROUND.toString());
    }

    @Override
    public boolean setToTank() {
        try {
            block = new Block(BlockType.TANK.toString());
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    private void Notify() {
        setChanged();
        notifyObservers();
    }
}