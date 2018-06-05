package tankgame.tankgameprocessing;

import processing.core.*;

import java.util.ArrayList;
import java.util.List;

/* Renderer */
// Holds a list of all "RenderObj"s, anything with a draw() method.
public class Renderer extends PApplet {

    private List<TestGame.RenderObj> objects;

    Renderer() {
        objects = new ArrayList<TestGame.RenderObj>();
    }

    public void draw() {
        for (TestGame.RenderObj obj : objects)
            obj.draw();
    }

    public void add(TestGame.RenderObj obj) {
        objects.add(obj);
    }
    public void remove(TestGame.RenderObj obj) {
        objects.remove(obj);
    }
}