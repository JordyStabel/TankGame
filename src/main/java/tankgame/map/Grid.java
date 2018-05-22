package tankgame.map;

import tankgame.block.IBlockContext;
import tankgame.tank.Tank;

import java.util.ArrayList;

public class Grid implements IGrid {

    private IBlockContext[][] blockList;
    private ArrayList<Tank> tanks = new ArrayList<>();

}
