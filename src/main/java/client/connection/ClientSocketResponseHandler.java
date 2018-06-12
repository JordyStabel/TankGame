package client.connection;

import client.Client;
import client.game.TankGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.actions.GameData;
import server.actions.Message;
import server.models.Player;

import java.util.ArrayList;

public class ClientSocketResponseHandler implements IClientMessageHandler {
    private TankGame tankGame;
    private Client client;

    public ClientSocketResponseHandler(TankGame tankGame) {
        this.tankGame = tankGame;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void handleMessage(String json) {

        Gson gson = new Gson();
        Message message;

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();

        message = gson.fromJson(jsonObject, Message.class);

        switch(message.getAction()) {
            case GAMEDATA:
                GameData gameData = (GameData) message.parseData(GameData.class);
                updateGame(gameData);
                break;
            case RIGHT:
                client.opponentMoveRight();
                break;
            case STOPRIGHT:
                client.opponentStopRight();
                break;
            case UPDATES:
                break;
        }
    }

    //TODO: Remove or implement
//    private void ping(){
//        if(client != null){
//            client.setPingGot(System.currentTimeMillis());
//        }
//    }

    private void updateGame(GameData gameData){
        tankGame.setPlayers((ArrayList<Player>) gameData.getPlayers());
    }
}
