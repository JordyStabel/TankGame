package tankgame.tankgameprocessing;

import processing.core.PApplet;
import processing.core.PImage;

public class TestGame extends PApplet {

    /* global variables */

    // the level contains the bitmap for all the static pixels
    Level level;
    // the background image is drawn behind the level
    PImage bg;

    // physics and rendering engines
    Physics physics; // has a list of all physics objects, and uses their velocity to move them
    Renderer _renderer; // has a list of all renderable objects, and calls their draw() method

    Player player;

    // translation, used to keep track of where the camera is
    int translateX;
    int translateY;

    // setup(), called before any looping is done
    public void setup() {
        // load our images for level and background
        bg = loadImage("images/sky-blurry.png");

        // FPS limit
        frameRate(60);

        level = new Level(loadImage("images/test_2.png"), 5); // new Level(image, destructionRes)

        // initialize the physics and rendering engines
        physics = new Physics();
        _renderer = new Renderer();

        // create the player
        player = new Player(100,100);
        physics.add(player);
        _renderer.add(player);
    }

    public void settings() {
        size(1280, 720, P2D);
    }

    // Draw loop
    public void draw() {

        // update physics
        physics.update();

        // load changes into the level
        level.update();


        /* Rendering */
        // first move our perspective to where the player is
        translateX = (int)constrain(width/2 - player.getX(), width - level.width(), 0);
        translateY = (int)constrain(height/2 - player.getY(), height - level.height(), 0);
        translate(translateX,
                translateY);

        // render the background
        background(255);
        image(bg,translateX * -0.8f,
                translateY * -0.8f);

        // draw the level
        level.draw(0,0);

        // show level normals
        //showNormals();

        // draw everything else
        _renderer.draw();

        // FPS counter
        fill(0,255,0);
        text(frameRate, 10,600);
    }
    /* Bullet */
// Acts similarly to PhysicsPixel
    /* Controls */
    public void keyPressed() {
        if (key == 'w' || key == 'W')
            player.jump();
        if (key == 'a' || key == 'A')
            player.moveLeft();
        if (key == 'd' || key == 'D')
            player.moveRight();
        println(key);
    }
    public void keyReleased() {
        if (key == 'a' || key == 'A')
            player.stopLeft();
        if (key == 'd' || key == 'D')
            player.stopRight();
    }
    public void mousePressed() {
        if (mouseButton == LEFT)
            player.shoot();
        else if (mouseButton == RIGHT)
            player.shootAlt();
    }
    public void mouseReleased() {
        if (mouseButton == LEFT)
            player.stopShooting();
        else if (mouseButton == RIGHT)
            player.stopShootingAlt();
    }
    public float getMouseX() {
        return mouseX - translateX;
    }
    public float getMouseY() {
        return mouseY - translateY;
    }

    /* Explode */
// Creates an "explosion" by finding all pixels near a point and launching them away
    public void explode(int xPos, int yPos, float radius) {
        float radiusSq = radius * radius;

        // loop through every x from xPos-radius to xPos+radius
        for (int x = xPos - (int)radius; x < xPos + (int)radius; x += level.destructionRes) {

            // first make sure that the x is within level's boundaries
            if (x > 0 && x < level.width()) {

                // next loop through every y pos in this x column
                for (int y = yPos - (int)radius; y < yPos + (int)radius; y += level.destructionRes) {

                    if (y > 0 && y < level.height()) { // boundary check

                        // first determine if this pixel (or if any contained within its square area) is solid
                        int solidX = 0,solidY = 0;
                        boolean solid = false;
                        // loop through every pixel from (xPos,yPos) to (xPos + destructionRes, yPos + destructionRes)
                        // to find whether this area is solid or not
                        for (int i = 0; i < level.destructionRes && !solid; i++) {
                            for (int j = 0; j < level.destructionRes && !solid; j++) {
                                if (level.isPixelSolid(x+i,y+j)) {
                                    solid = true;
                                    solidX = x+i;
                                    solidY = y+j;
                                }
                            }
                        }
                        if (solid) { // we know this pixel is solid, now we need to find out if it's close enough
                            float xDiff = x - xPos;
                            float yDiff = y - yPos;
                            float distSq = xDiff * xDiff + yDiff * yDiff;
                            // if the distance squared is less than radius squared, then it's within the explosion radius
                            if (distSq < radiusSq) {
                                // finally calculate the distance
                                float distance = sqrt(distSq);

                                // the speed will be based on how far the pixel is from the center
                                float speed = 800 * (1 - distance/radius);

                                if (distance == 0)
                                    distance = 0.001f; // prevent divide by zero in next two statements

                                // velocity
                                float velX = speed * (xDiff + random(-10,10)) / distance;
                                float velY = speed * (yDiff + random(-10,10)) / distance;

                                // create the dynamic pixel
                                Debris debris = new Debris(this, level.getColor(solidX, solidY), x,y, velX, velY, level.destructionRes);
                                physics.add(debris);
                                _renderer.add(debris);

                                // remove the static pixels
                                for (int i = 0; i < level.destructionRes; i++) {
                                    for (int j = 0; j < level.destructionRes; j++) {
                                        level.removePixel(x+i,y+j);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    /* PhysicsObj */
// Any object that will need motion integrated will implement this
// these methods allows the Physics class to forward the object's position using its velocity
    interface PhysicsObj {
        public float getX();
        public float getY();
        public float getVX();
        public float getVY();
        public void setX(float pX);
        public void setY(float pY);
        public void setVX(float vX);
        public void setVY(float vY);
        public void checkConstraints();
    }
    /* Physics */
    /* RayCast */
// Uses Bresenham's line algorithm to efficiently loop between two points, and find the first solid pixel
// This particular variation always starts from the first point, so collisions don't happen at the wrong end.
// returns an int array
//       ||| x = ([0],[1]) point in empty space before collision point
//       ||| o = ([2],[3]) collision point
//(end)--||ox------- (start)
//       |||
// using http://www.gamedev.net/page/resources/_/reference/programming/sweet-snippets/line-drawing-algorithm-explained-r1275
    public int[] rayCast(int startX, int startY, int lastX, int lastY) {
        int deltax = (int) abs(lastX - startX);        // The difference between the x's
        int deltay = (int) abs(lastY - startY);        // The difference between the y's
        int x = (int) startX;                       // Start x off at the first pixel
        int y = (int) startY;                       // Start y off at the first pixel
        int xinc1, xinc2, yinc1, yinc2;
        if (lastX >= startX) {                // The x-values are increasing
            xinc1 = 1;
            xinc2 = 1;
        }
        else {                         // The x-values are decreasing
            xinc1 = -1;
            xinc2 = -1;
        }

        if (lastY >= startY) {                // The y-values are increasing
            yinc1 = 1;
            yinc2 = 1;
        }
        else {                         // The y-values are decreasing
            yinc1 = -1;
            yinc2 = -1;
        }
        int den, num, numadd, numpixels;
        if (deltax >= deltay) {        // There is at least one x-value for every y-value
            xinc1 = 0;                  // Don't change the x when numerator >= denominator
            yinc2 = 0;                  // Don't change the y for every iteration
            den = deltax;
            num = deltax / 2;
            numadd = deltay;
            numpixels = deltax;         // There are more x-values than y-values
        }
        else {                         // There is at least one y-value for every x-value
            xinc2 = 0;                  // Don't change the x for every iteration
            yinc1 = 0;                  // Don't change the y when numerator >= denominator
            den = deltay;
            num = deltay / 2;
            numadd = deltax;
            numpixels = deltay;         // There are more y-values than x-values
        }
        int prevX = (int)startX;
        int prevY = (int)startY;

        for (int curpixel = 0; curpixel <= numpixels; curpixel++) {
            if (level.isPixelSolid(x, y))
                return new int[]{prevX, prevY, x, y};
            prevX = x;
            prevY = y;

            num += numadd;              // Increase the numerator by the top of the fraction

            if (num >= den) {             // Check if numerator >= denominator
                num -= den;               // Calculate the new numerator value
                x += xinc1;               // Change the x as appropriate
                y += yinc1;               // Change the y as appropriate
            }

            x += xinc2;                 // Change the x as appropriate
            y += yinc2;                 // Change the y as appropriate
        }
        return new int[]{};
    }


    // Anything we want drawn should implement this
    interface RenderObj {
        public void draw();
    }
    /* Renderer */
// Holds a list of all "RenderObj"s, anything with a draw() method.

    public void showNormals() {
        stroke(0);
        // Scan the level in a gridlike pattern, and only draw normals at pixels that have a range of solid pixels surrounding them
        for (int x = 0; x < level.width(); x += 10) {
            for (int y = 0; y < level.height(); y += 10) {
                int solidCount = 0;
                // scan solid pixels around this pixel
                for (int i = -5; i <= 5; i++) {
                    for (int j = -5; j <= 5; j++) {
                        if (level.isPixelSolid(x+i,y+j)) {
                            solidCount++;
                        }
                    }
                }
                // if there's too many solid pixels, then it's probably underground, and not a surface
                // if there's not enough solid pixels, then it's probably in the air, and not a surface
                if (solidCount < 110 && solidCount > 30) {
                    float[] pixelNormal = level.getNormal(x,y);
                    if (pixelNormal.length > 0 && !Float.isNaN(pixelNormal[0]) && !Float.isNaN(pixelNormal[1]))
                        line(x,y, x + 10 * pixelNormal[0], y + 10 * pixelNormal[1]);
                }
            }
        }
    }
    /* Level */
// Provides methods for determining solid/empty pixels, and for removing/adding solid pixels
    //public int sketchWidth() { return 600; }
    //public int sketchHeight() { return 450; }
    //public String sketchRenderer() { return JAVA2D; }
    static public void main(String args[]) {
        PApplet.main(new String[] { "tankgame.tankgameprocessing.TestGame" });
    }
}
