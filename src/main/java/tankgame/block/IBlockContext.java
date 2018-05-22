package tankgame.block;

import tankgame.Coordinates;
import tankgame.shell.Shell;

import java.util.Observer;

public interface IBlockContext {

    String getBlockState();

    Shell shootAt();

    Coordinates getBlockCoordinates();

    // TODO: Implement these methods
    void setToWater();
    void setToSurface();
    void setToGround();
    boolean setToTank();

    void addObserver(Observer observer);
}