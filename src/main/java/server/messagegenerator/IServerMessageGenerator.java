package server.messagegenerator;

import server.models.TankGame;

public interface IServerMessageGenerator extends Runnable {

    TankGame getTankGame();

    void setGame(TankGame tankGame);

    void updateGame();

    void updatePlayers();

    void ping(String playerID);

    @Override
    void run();
}
