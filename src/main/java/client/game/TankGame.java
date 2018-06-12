package client.game;

import client.Client;
import server.models.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static processing.core.PApplet.println;

public class TankGame {

    private Client client;

    private ArrayList<Player> players;
    //private Apple apple;

    public TankGame() {
        players = new ArrayList<>();
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void addPlayer(Player player) {
        if (player != null) {
            players.add(player);
            println("Added players: " + player.getPlayerName());
        }
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public Client getClient(){
        return this.client;
    }


//    //public Apple getApple() {
//        return apple;
//    }
//
//    //public void setApple(Apple apple) {
//        this.apple = apple;
//    }
}