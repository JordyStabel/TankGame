package tankgame.tank;

import tankgame.Coordinates;
import tankgame.block.IBlockContext;
import tankgame.map.IGrid;
import tankgamegui.enums.ShellType;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public abstract class Tank implements Observer {
    private int length;
    private int height;
    private ArrayList<IBlockContext> blocks;

    // Constructor for Tank
    Tank(int length, int height, Coordinates coordinates, IGrid grid) {
        this.length = length;
        this.height = height;
        blocks = new ArrayList<>();

        if (!setBlocks(coordinates, grid)) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private boolean setBlocks(Coordinates coordinates, IGrid grid) {
        int x = coordinates.getX();
        int y = coordinates.getY();

        return setBlocks(grid, x, y);
    }

    // Reserve blocks for the tank
    private boolean setBlocks(IGrid grid, int x, int y) {
        try {
            for (int i = x; i < y + length; i++) {
                IBlockContext block;
                block = grid.getBlockPointer(new Coordinates(i, y));
                if (block.getBlockState().equals("GROUND")) {
                    block.setToTank();
                    blocks.add(block);
                } else {
                    resetToGround();
                    return false;
                }
            }
        } catch (Exception e) {
            resetToGround();
            return false;
        }
        return true;
    }

    private void resetToGround() {
        for (IBlockContext block : blocks) {
            block.setToGround();
        }
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<IBlockContext> getBlocks() {
        return blocks;
    }

    @Override
    public void update(Observable observable, Object object) {
        boolean destroyTank = true;
        for (IBlockContext block : blocks) {
            if (!block.getBlockState().equals(ShellType.HIT)) {
                destroyTank = false;
            }
        }
        if (destroyTank) {
            for (IBlockContext block : blocks) {
                // TODO: Set to destroyed tank
                block.setToWater();
            }
        }
    }

    public boolean remove() {
        try {
            resetToGround();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}