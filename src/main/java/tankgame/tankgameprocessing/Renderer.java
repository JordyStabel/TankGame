package tankgame.tankgameprocessing;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/* Renderer */
// Holds a list of all "RenderObj"s, anything with a draw() method.
public class Renderer {

    private List<TestGame.RenderObj> objects;

    private PApplet parent;

    Renderer(PApplet pApplet) {
        parent = pApplet;
        objects = new ArrayList<TestGame.RenderObj>();
    }

    public void draw() {
        for (TestGame.RenderObj obj : objects){
            obj.display();
        }
    }

    public void add(TestGame.RenderObj obj) {
        objects.add(obj);
    }
    public void remove(TestGame.RenderObj obj) {
        objects.remove(obj);
    }
}