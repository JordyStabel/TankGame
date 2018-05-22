package tankgame.tank;

import tankgame.Coordinates;
import tankgame.map.IGrid;
import tankgamegui.enums.TankType;

public class TankCreator implements ITankCreator {

    private Tank newTank(Coordinates coordinates, IGrid grid){
        try {
            Tank tank = new Tank();
            observeTiles(ship);
            return ship;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Tank newTank(TankType tankType, Coordinates coordinates, IGrid grid) {
        return null;
    }
}