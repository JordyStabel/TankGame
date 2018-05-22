package tankgame.tank;

import tankgame.Coordinates;
import tankgame.map.IGrid;
import tankgamegui.enums.TankType;

public interface ITankCreator {
    Tank newTank(TankType tankType, Coordinates coordinates, IGrid grid);
}