package tankgame.tank;

import tankgame.Coordinates;
import tankgame.block.IBlockContext;
import tankgame.map.IGrid;
import tankgamegui.enums.TankType;

public class TankCreator implements ITankCreator {

    private Tank newTank(Coordinates coordinates, IGrid grid){
        try {
            Tank tank = newTank(coordinates, grid);
            observeBlocks(tank);
            return tank;
        } catch (Exception e) {
            return null;
        }
    }

    private void observeBlocks(Tank tank) {
        for (IBlockContext block :
                tank.getBlocks()) {
            block.addObserver(tank);
        }
    }

    @Override
    public Tank newTank(TankType tankType, Coordinates coordinates, IGrid grid) {
        return null;
    }
}