package tankgame.tankgameprocessing;

import processing.core.*;
import tankgamegui.TankGameApplication;

public class PlayerObject implements TankGameApplication.RenderObj, TankGameApplication.PhysicsObj {

    PApplet parent;

    private TankGameApplication tankGameApplication;
    private Level level;
    private Renderer _renderer;
    private Physics physics;

    public int getPlayerNr() {
        return playerNr;
    }

    public void setPlayerNr(int playerNr) {
        this.playerNr = playerNr;
    }

    private int playerNr;

    // Position
    private float posX;
    private float posY;

    private float velX;
    private float velY;

    // variables to track whether or not the user is pressing A/D
    private boolean goLeft;
    private boolean goRight;

    // Are we shooting?
    private boolean shooting;
    private boolean shootingAlt;

    // last time (ms) a bullet was shot, used to limit the firing rate
    private long lastShot;

    // variables for physics
    public boolean onGround; // are we allowed to jump?
    private boolean topBlocked;

    // PlayerObject size
    private int playerWidth;
    private int playerHeight;

    // Constructor
    public PlayerObject(PApplet pApplet, Level level, int positionX, int positionY) {
        this.posX = positionX;
        this.posY = positionY;

        parent = pApplet;
        this.level = level;

        // Start out standing still
        velX = 0;
        velY = 0;

        // initialize the playerObject as a 15x15 px box
        playerWidth = 15;
        playerHeight = 15;
    }

    // Shooting toggles
    public void shoot() {
        if (!shootingAlt)
            shooting = true;
    }

    public void stopShooting() {
        shooting = false;
    }

    public void shootAlt() {
        if (!shooting)
            shootingAlt = true;
    }

    public void stopShootingAlt() {
        shootingAlt = false;
    }

    // jump
    public void jump() {
        if (onGround && !topBlocked && velY > -500)
            velY -= 500;
    }

    // moving toggles
    public void moveLeft() {
        goLeft = true;
    }

    public void moveRight() {
        goRight = true;
    }

    public void stopLeft() {
        goLeft = false;
    }

    public void stopRight() {
        goRight = false;
    }

    // draw - implemented as a RenderObj
    public void display() {
        parent.stroke(0);
        parent.fill(255);
        parent.rect(posX - (float)playerWidth / 2, posY - (float)playerHeight / 2, playerWidth, playerHeight);
    }

    @Override
    public float getX() {
        return posX;
    }

    @Override
    public float getY() {
        return posY;
    }

    @Override
    public float getVX() {
        return velX;
    }

    @Override
    public float getVY() {
        return velY;
    }

    @Override
    public void setX(float pX) {
        posX = pX;
    }

    @Override
    public void setY(float pY) {
        posY = pY;
    }

    @Override
    public void setVX(float vX) {
        velX = vX;
    }

    @Override
    public void setVY(float vY) {
        velY = vY;
    }

    // checkConstraints - implemented as a PhysicsObj
    public void checkConstraints() {
        // controls

        // shooting
        if (shooting || shootingAlt) {
            // Primary fire happens every 200 ms, alternate fire happens every 25 ms.
            if (!(shooting && parent.millis() - lastShot < 150) && !(shootingAlt && parent.millis() - lastShot < 15)) {
                // Create a vector between the playerObject and the mouse, then normalize that vector (to change its length to 1)
                // after multiplying by the desired bullet speed, we get how fast along each axis we want the bullet to be traveling
                float diffX = tankGameApplication.getMouseX() - posX;
                float diffY = tankGameApplication.getMouseY() - posY;
                float len = parent.sqrt(diffX * diffX + diffY * diffY);
                if (shooting) {
                    // create the bullet at 2000 px/sec, and add it to our Physics and Rendering lists
                    Shell shell = new Shell(posX, posY, 2000 * diffX / len, 2000 * diffY / len, parent);
                    physics.add((TankGameApplication.PhysicsObj) shell);
                    _renderer.add((TankGameApplication.RenderObj) shell);
                } else {
                    // Change our color from RGB to HSB so we can cycle through hues
                    parent.colorMode(parent.HSB, 255);
                    for (int i = 0; i < 150; i++) { // create 150 particles
                        DebrisObject debrisObject = new DebrisObject(new PApplet(), parent.color((int) (((parent.millis() / 5000f) * 255f) % 255), 255, 255), // color
                                this.posX, this.posY, // position
                                parent.random(-50, 50) + parent.random(1500, 2500) * diffX / len, parent.random(-50, 50) + parent.random(1500, 2500) * diffY / len, // speed
                                level.destructionRes); // size
                        physics.add(debrisObject);
                        _renderer.add(debrisObject);
                    }
                    parent.colorMode(parent.RGB, 255);
                }
                // reset lastShot
                lastShot = parent.millis();
            }
        }

        // movement
        if (goLeft) {
            if (velX > -500)
                velX -= 40;
        } else if (velX < 0)
            velX *= 0.8f; // slow down side-ways velocity if we're not moving left

        if (goRight) {
            if (velX < 500)
                velX += 40;
        } else if (velX > 0)
            velX *= 0.8f;

        // Collision detection/handling
        // Loop along each edge of the square until we find a solid pixel
        // if there is one, we find out if there's any adjacent to it (loop perpendicular from that pixel into the box)
        // Once we hit empty space, we move the box to that empty space

        onGround = false;
        for (int bottomX = (int) posX - playerWidth / 2; bottomX <= (int) posX + playerWidth / 2; bottomX++) {
            if (level.isPixelSolid(bottomX, (int) posY + playerHeight / 2 + 1) && (velY > 0)) {
                onGround = true;
                for (int yCheck = (int) posY + playerHeight / 4; yCheck < (int) posY + playerHeight / 2; yCheck++) {
                    if (level.isPixelSolid(bottomX, yCheck)) {
                        posY = yCheck - playerHeight / 2;
                        break;
                    }
                }
                if (velY > 0)
                    velY *= -0.25f;
            }
        }

        topBlocked = false;
        // start with the top edge
        for (int topX = (int) posX - playerWidth / 2; topX <= (int) posX + playerWidth / 2; topX++) {
            if (level.isPixelSolid(topX, (int) posY - playerHeight / 2 - 1)) { // if the pixel is solid
                topBlocked = true;
                if (velY < 0) {
                    velY *= -0.5f;
                }
            }
        }
        // loop left edge
        if (velX < 0) {
            for (int leftY = (int) posY - playerHeight / 2; leftY <= (int) posY + playerHeight / 2; leftY++) {
                if (level.isPixelSolid((int) posX - playerWidth / 2, leftY)) {
                    // next move from the edge to the right, inside the box (stop it at 1/4th the playerObject width)
                    for (int xCheck = (int) posX - playerWidth / 4; xCheck < (int) posX - playerWidth / 2; xCheck--) {
                        if (level.isPixelSolid(xCheck, leftY)) {
                            posX = xCheck + playerWidth / 2; // push the block over
                            break;
                        }
                    }
                    if (leftY > posY && !topBlocked) {
                        posY -= 1;
                    } else {
                        velX *= -0.4f;
                        posX += 2;
                    }
                }
            }
        }
        // do the same for the right edge
        if (velX > 0) {
            for (int rightY = (int) posY - playerHeight / 2; rightY <= (int) posY + playerHeight / 2; rightY++) {
                if (level.isPixelSolid((int) posX + playerWidth / 2, rightY)) {
                    for (int xCheck = (int) posX + playerWidth / 4; xCheck < (int) posX + playerWidth / 2 + 1; xCheck++) {
                        if (level.isPixelSolid(xCheck, rightY)) {
                            posX = xCheck - playerWidth / 2;
                            break;
                        }
                    }
                    if (rightY > posY && !topBlocked) {
                        posY -= 1;
                    } else {
                        velX *= -0.4f;
                        posX -= 2;
                    }
                }
            }
        }

        // Boundary Checks
        if (posX < 0 && velX < 0) {
            posX -= posX;
            velX *= -1;
        }
        if (posY < 0 && velY < 0) {
            posY -= posY;
            velY *= -1;
        }
        if (posX > level.width() && velX > 0) {
            posX += level.width() - posX;
            velX *= -1;
        }
        if (posY + playerHeight / 2 > level.height() && velY > 0) {
            posY += level.height() - posY - playerHeight / 2;
            velY = 0;
            onGround = true;
        }
    }
}