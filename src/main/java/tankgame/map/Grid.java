package tankgame.map;

import tankgame.block.IBlockContext;
import tankgame.tank.ITankCreator;
import tankgame.tank.Tank;
import tankgame.tank.TankCreator;

import java.util.ArrayList;

public class Grid implements IGrid {

    private IBlockContext[][] blockList;
    private ArrayList<Tank> tanks = new ArrayList<>();
    private ITankCreator tankCreator = new TankCreator();

    // Constructor for Grid
    Grid(int width, int height){
        this.blockList =
    }
}