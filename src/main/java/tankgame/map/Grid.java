package tankgame.map;

import tankgame.Coordinates;
import tankgame.block.BlockContext;
import tankgame.block.IBlockContext;
import tankgame.tank.ITankCreator;
import tankgame.tank.Tank;
import tankgame.tank.TankCreator;
import tankgamegui.enums.TankType;

import java.util.ArrayList;
import java.util.Random;

public class Grid implements IGrid {

    private IBlockContext[][] blockList;
    private ArrayList<Tank> tanks = new ArrayList<>();
    private ITankCreator tankCreator = new TankCreator();

    private Random random = new Random();

    // Constructor for Grid
    public Grid(int width, int height){
        this.blockList = CreateGrid(width, height);
    }

    public IBlockContext[][] CreateGrid(int maxX, int maxY) {
        blockList = new IBlockContext[maxX][maxY];

        int height = 4;
        // Will generate a random number between -2 and 2
        int randomDifference = random.nextInt(3) - 2;

        for (int x = 0; x < maxX; x++){
            for (int y = 0; y < (height + randomDifference); y++){
                blockList[x][y] = new BlockContext(new Coordinates(x, y));
                System.out.println(x + y);
                if (y == (height + randomDifference) - 1){
                    height = y;
                }
            }
        }
        return blockList;
    }

    public IBlockContext getBlockPointer(Coordinates coordinates) {
        return null;
    }

    public boolean placeTank(TankType tankType, int x, int y) {
        return false;
    }

    public boolean removeTank(int posX, int posY) {
        return false;
    }

    public ArrayList<Coordinates> getTankBlocks(int x, int y) {
        return null;
    }

    public boolean tankPlaced() {
        return false;
    }

    public boolean tankDestroyed() {
        return false;
    }
}