package server.actions;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import server.response.Json;

public class Message implements Json {

    private Actions action;
    private String content;
    private IAction data;

    public Message(Actions action) {
        this.action = action;
    }

    public Message(Actions action, String content) {
        this.action = action;
        this.content = content;
    }

    public Message(Actions action, IAction data) {
        this.action = action;
        Gson gson = new Gson();
        this.content = gson.toJson(data);
    }

    public Actions getAction() {
        return action;
    }

    public String getContent() {
        return content;
    }

    public IAction getData() {
        return data;
    }

    public IAction parseData(Class<? extends IAction> iAction){
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(content).getAsJsonObject();
        Gson g = new Gson();
        this.data = g.fromJson(jsonObject, iAction);
        return this.data;
    }

    @Override
    public String convertToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
