package server.models;


import server.customexceptions.GameAlreadyStarted;
import server.customexceptions.PlayerNotReady;
import server.messagegenerator.IServerMessageGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class TankGame implements Runnable {
    private int height;
    private int width;

    private ArrayList<Player> players;
    private boolean running = false;

    private IServerMessageGenerator serverMessageGenerator;
    private Thread gameThread;

    private ExecutorService executorService;

    //GAME LOOP
    final int TICKS_PER_SECOND = 10;
    final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;


    @Override
    public void run() {
        double next_game_tick = System.currentTimeMillis();
        while (running) {
            if (System.currentTimeMillis() > next_game_tick) {
                for (int i = 0; i < players.size(); i++) {
                    Player player = players.get(i);

//                    player.getSnake().update(width, height);
//                    if (player.getSnake().getX() == apple.getX() && player.getSnake().getY() == apple.getY()) {
//                        player.getSnake().addTail();
//                        apple.update(width, height);
//                    }
                }
                next_game_tick += SKIP_TICKS;
            }
            serverMessageGenerator.updatePlayers();
        }
    }

    public TankGame(IServerMessageGenerator serverMessageGenerator, int height, int width) {
        this.serverMessageGenerator = serverMessageGenerator;
        this.height = height;
        this.width = width;
        players = new ArrayList<>();
    }

    public void start() throws GameAlreadyStarted, PlayerNotReady {
        if(running){
            throw new GameAlreadyStarted("Game already started");
        }

//        if(players.size() < 4){
//            int aiPlayers = 0;
//            for (int i = players.size(); i < 4; i++) {
//                AIPlayer aiPlayer = new AIPlayer(Integer.toString(i),"AI "+i,new Snake((int) (Math.random() * (width)),
//                        (int) (Math.random() * (height)), Color.WHITE), this.apple, width, height, i);
//                players.add(aiPlayer);
//                aiPlayer.ready();
//                aiPlayers++;
//            }
//            executorService = Executors.newFixedThreadPool(aiPlayers);
//        }

        for (int i = 0; i < players.size(); i++) {
            if(!players.get(i).getIsReady()){
                throw new PlayerNotReady(players.get(i).getPlayerName());
            }
        }
        running = true;

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void playerReady(String uuid){
        Player player = findPlayer(uuid);
        if(player == null){
            throw new IllegalArgumentException("No player found");
        }
        player.ready();

        for (int i = 0; i < players.size(); i++) {
            if(!players.get(i).getIsReady()){
                return;
            }
        }

        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() throws Exception {
        if(!running){
            throw new Exception("Game isn't running");
        }
        running = false;
    }

    public synchronized void addPlayer(Player player) {
        if (player != null) {
            players.add(player);
        }
        serverMessageGenerator.updatePlayers();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Player findPlayer(String uuid){
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getPlayerID().equals(uuid)){
                return players.get(i);
            }
        }
        return null;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public void removePlayer(String uuid){
        Player player = findPlayer(uuid);
        removePlayer(player);
    }

    public void removePlayer(Player player){
        if(player != null) {
            if (player.equals(findPlayer(player.getPlayerID()))) {
                players.remove(player);
            }
        }
    }
}