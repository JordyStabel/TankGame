package client.game.tankgameobjects;

import client.Client;
import processing.core.PApplet;

public class Shell implements IPhysicsObject, IRenderObject, Client.RenderObj, Client.PhysicsObj {

    private Client client;
    private Level level;
    private Renderer _renderer;
    private Physics physics;

    private PApplet parent;

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
    Shell(float x, float y, float vX, float vY, PApplet pApplet, Level level, Client client) {
        this.x = x; this.y = y;
        lastX = x; lastY = y;
        velX = vX; velY = vY;

        this.parent = pApplet;
        this.level = level;
        this.client = client;
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
        int[] collision = client.rayCast((int)lastX, (int)lastY, (int)x, (int)y);
        if (collision.length > 0) {
            _renderer.remove(this);
            physics.remove(this);
            client.explode(collision[2], collision[3], 40);
        }
        lastX = x;
        lastY = y;
    }

    public void draw() {
        parent.fill(0);
        parent.rect(x,y,30,30);
    }

    @Override
    public void display() {

    }
}