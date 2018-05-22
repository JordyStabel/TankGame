package tankgame.tank;

import tankgame.Coordinates;
import tankgame.map.IGrid;

public class NormalTank extends Tank {
    public NormalTank(Coordinates coordinates, IGrid grid){
        super(3,1, coordinates, grid);
    }
}