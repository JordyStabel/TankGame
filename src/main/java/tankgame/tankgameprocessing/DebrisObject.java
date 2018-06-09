package tankgame.tankgameprocessing;

import processing.core.*;
import tankgamegui.TankGameApplication;

import static java.sql.DriverManager.println;

public class DebrisObject implements TankGameApplication.PhysicsObj, TankGameApplication.RenderObj {

    private PApplet parent;

    private TankGameApplication tankGameApplication;
    private Level level;
    private Renderer _renderer;
    private Physics physics;

    // Position
    private float x;
    private float y;

    // Last location, used for our "ray casting"
    private float lastX;
    private float lastY;

    // Velocity
    private float velX;
    private float velY;

    private float stickiness = 1500; // minimum speed for this pixel to stick
    private float bounceFriction = 0.85f; // scalar multiplied to velocity after bouncing

    private int col; // color of the pixel

    private int size = 1; // width and height of the pixel

    public DebrisObject(PApplet pApplet, int c, float x, float y, float vX, float vY, int size) {
        col = c;
        this.x = x; this.y = y;
        lastX = x; lastY = y;
        velX = vX; velY = vY;

        parent = pApplet;

        this.size = size;
    }

    // Drawing on the screen
    public void display(){
        parent.fill(col);
        parent.noStroke();
        parent.rect(x, y, size, size);
        println("Display");
    }


    // Render the pixel (method implemented as a RenderObj)
    public void draw() {
        println("Draw in debris");
    }

    // Methods implemented as a PhysicsObj
    public float getX() { return x; }
    public float getY() { return y; }
    public float getVX() { return velX; }
    public float getVY() { return velY; }
    public void setX(float pX) { x = pX; }
    public void setY(float pY) { y = pY; }
    public void setVX(float vX) { velX = vX; }
    public void setVY(float vY) { velY = vY; }

    // CheckConstraints, also implemented as a PhysicsObj
    public void checkConstraints() {
        // Find if there's a collision between the current and last points
        int[] collision = tankGameApplication.rayCast((int)lastX, (int)lastY, (int)x, (int)y);
        if (collision.length > 0)
            collide(collision[0], collision[1], collision[2], collision[3]);

        // reset last positions
        lastX = x;
        lastY = y;

        // Boundary constraints... only remove the pixel if it exits the sides or bottom of the map
        if (x > level.width() || x < 0 || y > level.height()) {
            _renderer.remove(this);
            physics.remove(this);
        }
    }

    /* Collide */
    // called whenever checkConstraints() detects a solid pixel in the way
    public void collide(int thisX, int thisY, int thatX, int thatY) {
        // first determine if we should stick or if we should bounce
        if (velX * velX + velY * velY < stickiness * stickiness) { // if the velocity's length is less than our stickiness property, add the pixel
            // draw a rectangle by looping from x to size, and from y to size
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    level.addPixel(col, thisX+i, thisY+j);
                }
            }
            // remove this dynamic pixel
            _renderer.remove(this);
            physics.remove(this);
        }
        else { // otherwise, bounce
            // find the normal at the collision point

            // to do this, we need to reflect the velocity across the edge normal at the collision point
            // this is done using a 2D vector reflection formula ( http://en.wikipedia.org/wiki/Reflection_(mathematics) )

            float pixelNormal[] = level.getNormal((int)thatX, (int)thatY);

            float d = 2 * (velX * pixelNormal[0] + velY * pixelNormal[1]);

            velX -= pixelNormal[0] * d;
            velY -= pixelNormal[1] * d;

            // apply bounce friction
            velX *= bounceFriction;
            velY *= bounceFriction;

            // reset x and y so that the pixel starts at point of collision
            x = thisX;
            y = thisY;
        }
    }
}