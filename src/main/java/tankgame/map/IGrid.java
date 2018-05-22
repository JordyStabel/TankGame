package tankgame.map;

import tankgame.Coordinates;
import tankgame.block.IBlockContext;
import tankgamegui.enums.TankType;

import java.util.ArrayList;

public interface IGrid {
    IBlockContext getBlockPointer(Coordinates coordinates);

    boolean placeTank(TankType tankType, int x, int y);

    boolean removeTank(int posX, int posY);

    ArrayList<Coordinates> getTankBlocks(int x, int y);

    boolean tankPlaced();

    boolean tankDestroyed();
}