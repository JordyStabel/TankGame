package server.messagegenerator;

import server.models.TankGame;

public interface IServerMessageGenerator extends Runnable {

    TankGame getTankGame();

    void setGame(TankGame tankGame);

    void updateGame();

    void updatePlayers();

    @Override
    void run();

    void playerMoveLeft();

    void playerStopLeft();

    void playerMoveRight();

    void playerStopRight();
}
