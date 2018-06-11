package client.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TankGame {
    private int height;
    private int width;

    private ArrayList<Player> players;
    private Apple apple;

    public Game(int height, int width) {
        this.height = height;
        this.width = width;

        players = new ArrayList<>();
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void addPlayer(Player player) {
        if (player != null) {
            players.add(player);
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Apple getApple() {
        return apple;
    }

    public void setApple(Apple apple) {
        this.apple = apple;
    }
}