package client.game.tankgameobjects;

import processing.core.PApplet;
import tankgamegui.TankGameApplication;

import java.util.ArrayList;
import java.util.List;

/* Renderer */
// Holds a list of all "RenderObj"s, anything with a draw() method.
public class Renderer {

    private List<TankGameApplication.RenderObj> objects;

    private PApplet parent;

    public Renderer(PApplet pApplet) {
        parent = pApplet;
        objects = new ArrayList<TankGameApplication.RenderObj>();
    }

    public void draw() {
        for (TankGameApplication.RenderObj obj : objects){
            obj.display();
        }
    }

    public void add(TankGameApplication.RenderObj obj) {
        objects.add(obj);
    }
    public void remove(TankGameApplication.RenderObj obj) {
        objects.remove(obj);
    }
}