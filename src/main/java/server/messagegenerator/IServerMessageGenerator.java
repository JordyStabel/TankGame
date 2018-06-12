package server.messagegenerator;

import server.models.TankGame;

public interface IServerMessageGenerator extends Runnable {

    TankGame getTankGame();

    void setGame(TankGame tankGame);

    void updateGame();

    void updatePlayers();

    @Override
    void run();

    void playerMoveLeft(String id);

    void playerStopLeft(String id);

    void playerMoveRight(String id);

    void playerStopRight(String id);
}
