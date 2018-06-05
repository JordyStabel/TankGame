package tankgame.tankgameprocessing;

import processing.core.*;

public class Shell extends PApplet implements IPhysicsObject, IRenderObject {

    private TestGame testGame;
    private Level level;
    private Renderer _renderer;
    private Physics physics;

    // Position
    private float x;
    private float y;

    // Last position
    private float lastX;
    private float lastY;

    // Velocity
    private float velX;
    private float velY;

    // Constructor
    Shell(float x, float y, float vX, float vY) {
        this.x = x; this.y = y;
        lastX = x; lastY = y;
        velX = vX; velY = vY;
    }

    // methods implemented as a PhysicsObj
    public float getX() { return x; }
    public float getY() { return y; }
    public float getVX() { return velX; }
    public float getVY() { return velY; }
    public void setX(float pX) { x = pX; }
    public void setY(float pY) { y = pY; }
    public void setVX(float vX) { velX = vX; }
    public void setVY(float vY) { velY = vY; }

    public void checkConstraints() {
        int[] collision = testGame.rayCast((int)lastX, (int)lastY, (int)x, (int)y);
        if (collision.length > 0) {
            _renderer.remove((TestGame.RenderObj) this);
            physics.remove((TestGame.PhysicsObj) this);
            testGame.explode(collision[2], collision[3], 40);
        }
        lastX = x;
        lastY = y;
    }

    public void draw() {
        fill(0);
        rect(x,y,30,30);
    }
}