package client.game.tankgameobjects;

import client.Client;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/* Renderer */
// Holds a list of all "RenderObj"s, anything with a draw() method.
public class Renderer {

    private List<Client.RenderObj> objects;

    private PApplet parent;

    public Renderer(PApplet pApplet) {
        parent = pApplet;
        objects = new ArrayList<Client.RenderObj>();
    }

    public void draw() {
        for (Client.RenderObj obj : objects){
            obj.display();
        }
    }

    public void add(Client.RenderObj obj) {
        objects.add(obj);
    }
    public void remove(Client.RenderObj obj) {
        objects.remove(obj);
    }
}