package tankgamegui;

import javafx.application.Platform;
import processing.core.PApplet;
import processing.core.PImage;
import tankgame.ITankGame;
import tankgame.TankGame;
import tankgame.tankgameprocessing.*;

public class TankGameApplication extends PApplet implements ITankGameGUI {

    /* global variables */
    private String playerName = null;
    private String opponentName = null;

    private int playerNr = 0;

    private ITankGame game;

    private boolean bothReady = false;
    private boolean gameEnded = false;

    // the level contains the bitmap for all the static pixels
    Level level;
    // the background image is drawn behind the level
    PImage bg;

    // physics and rendering engines
    Physics physics; // has a list of all physics objects, and uses their velocity to move them
    Renderer _renderer; // has a list of all renderable objects, and calls their draw() method

    PlayerObject playerObject;

    // translation, used to keep track of where the camera is
    int translateX = 0; // don't instantiate
    int translateY = 0; // don't instantiate

    // setup(), called before any looping is done
    public void setup() {

        game = new TankGame();

        // load our images for level and background
        bg = loadImage("images/sky-blurry.png");

        // FPS limit
        frameRate(60);

        // new Level(image, destructionRes)
        level = new Level(this, loadImage("images/tree.png"), 5);

        // initialize the physics and rendering engines
        physics = new Physics(this);
        _renderer = new Renderer(this);
//
//        // create the playerObject
        playerObject = new PlayerObject(this, level,100,100);
        physics.add(playerObject);
        _renderer.add(playerObject);
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
        // first move our perspective to where the playerObject is
        translateX = (int)constrain(width/2 - playerObject.getX(), width - level.width(), 0);
        translateY = (int)constrain(height/2 - playerObject.getY(), height - level.height(), 0);
        translate(translateX,
                translateY);

//        // render the background
//        background(255);
        image(bg,translateX * -0.8f,
                translateY * -0.8f);

        // draw the level
        level.update();

        // draw everything else
        _renderer.draw();

        // FPS counter
        fill(0,255,0);
        text(frameRate, 10,600);

        // Added with the new class/object system
        //playerObject.display();
        level.display();
        playerObject.display();
    }
    /* Bullet */
// Acts similarly to PhysicsPixel
    /* Controls */
    public void keyPressed() {
        if (key == 'w' || key == 'W')
            playerObject.jump();
        if (key == 'a' || key == 'A')
            playerObject.moveLeft();
        if (key == 'd' || key == 'D')
            playerObject.moveRight();
        if (key == 'y' || key == 'Y') {
            try {
                registerPlayer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void keyReleased() {
        if (key == 'a' || key == 'A')
            playerObject.stopLeft();
        if (key == 'd' || key == 'D')
            playerObject.stopRight();
    }
    public void mousePressed() {
        if (mouseButton == LEFT)
            playerObject.shoot();
        else if (mouseButton == RIGHT)
            playerObject.shootAlt();
    }
    public void mouseReleased() {
        if (mouseButton == LEFT)
            playerObject.stopShooting();
        else if (mouseButton == RIGHT)
            playerObject.stopShootingAlt();
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
                                DebrisObject debrisObject = new DebrisObject(this, level.getColor(solidX, solidY), x,y, velX, velY, level.destructionRes);
                                //physics.add(debrisObject);
                                //_renderer.add(debrisObject);

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

    @Override
    public void setPlayerName(int nr, String name) {
        if (nr != this.playerNr) {
            println("ERROR: Wrong player number method setPlayerName()");
            return;
        } else {
            println("player name " + name + " registered");
        }
        playerName = name;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                println(playerName + "'s name has been set");
            }
        });
    }

    @Override
    public void setOpponentName(int nr, String name) {
        if (playerNr != this.playerNr) {
            println("ERROR: Wrong player number method setOpponentName()");
            return;
        } else {
            println("Your opponent is " + name);
        }
        opponentName = name;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                println(opponentName + "'s name has been set");
            }
        });
    }

    @Override
    public void startGame() {
        this.start();
    }

    /* PhysicsObj */
// Any object that will need motion integrated will implement this
// these methods allows the Physics class to forward the object's position using its velocity
    public interface PhysicsObj {
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
    public interface RenderObj {
        //public void draw();
        public void display();
    }

    private void registerPlayer() throws Exception {
        playerName = "";//textFieldPlayerName.getText();
        if ("".equals(playerName) || playerName == null) {
            println("Enter your name before continuing");
        } else {
            println(playerName + " Has registered!");
            playerNr = game.registerPlayer(playerName, (ITankGameGUI) this, false);
            if (playerNr != -1) {

                println("player " + playerName + " registered");
            } else {
                println("Name already defined");
            }
        }
    }

    private void notifyWhenReady() {
        // Notify that the player is ready is startSocket the game.
        bothReady = game.notifyWhenReady(playerNr);
        if (bothReady) {
            startGame();
        } else {
            println("Place all ships and then press Ready to play");
        }
    }

//    private void message(){
//        JOptionPane pane = new JOptionPane();
//        JDialog dialog = pane.createDialog("Hi there!");
//        dialog.setAlwaysOnTop(true);
//        dialog.show();
//    }

    /* Level */
// Provides methods for determining solid/empty pixels, and for removing/adding solid pixels
    //public int sketchWidth() { return 600; }
    //public int sketchHeight() { return 450; }
    //public String sketchRenderer() { return JAVA2D; }
    static public void main(String args[]) {

//        String input;
//        boolean registered = false;
//
//        public void checkInput(){
//
//        }
//
//        String input = JOptionPane.showInputDialog(null, "Username", "Please Enter Your Username", JOptionPane.PLAIN_MESSAGE);
//
//        try {
//            if (input.equals("")){
//                input = JOptionPane.showInputDialog(null, "Username", "Please Enter Your Username", JOptionPane.PLAIN_MESSAGE);
//            }
//        }catch (NullPointerException e){
//            System.exit(0);
//        }

        PApplet.main(new String[] { "tankgamegui.TankGameApplication" });
    }
}