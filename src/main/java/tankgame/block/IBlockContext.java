package tankgame.block;

import java.util.Observer;

public interface IBlockContext {

    String getBlockState();

    void setToWater();
    void setToSurface();
    void setToGround();

    void addObserver(Observer observer);
}