package tankgame.map;

import org.junit.Test;
import tankgame.Coordinates;
import tankgame.block.BlockContext;
import tankgame.block.IBlockContext;

import java.util.Random;

public class GridTest {

    public Random random = new Random();
    private IBlockContext[][] blockList;

    @Test
    public void createGrid() {
        int height = 4;
        // Will generate a random number between -2 and 2
        int randomDifference = random.nextInt(3) - 2;

        for (int x = 0; x < 1000; x++){
            for (int y = 0; y < (height + randomDifference); y++){
                blockList[x][y] = new BlockContext(new Coordinates(x, y));
                System.out.println(x + y);
                if (y == (height + randomDifference) - 1){
                    height = y;
                }
            }
        }
    }
}