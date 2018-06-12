package client;

import client.connection.ClientEndPointSocket;
import client.game.TankGame;
import client.game.tankgameobjects.*;
import processing.core.PApplet;
import processing.core.PImage;
import server.actions.Actions;
import server.actions.Message;
import server.models.Player;
import tankgame.ITankGame;

import java.util.logging.Logger;

public class Client extends PApplet {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private TankGame tankGame;

    private Player player;
    private Player opponent;

    private ClientEndPointSocket clientEndPointSocket;
    private boolean info = false;

    private ITankGame game;


    // the level contains the bitmap for all the static pixels
    Level level;
    // the background image is drawn behind the level
    PImage bg;

    // physics and rendering engines
    public Physics physics; // has a list of all physics objects, and uses their velocity to move them
    public Renderer _renderer; // has a list of all renderable objects, and calls their draw() method

    PlayerObject playerObject;
    PlayerObject opponentObject;

    // translation, used to keep track of where the camera is
    int translateX = 0; // don't instantiate
    int translateY = 0; // don't instantiate

    public Client(TankGame tankGame, ClientEndPointSocket clientEndPointSocket) {
        this.tankGame = tankGame;
        this.clientEndPointSocket = clientEndPointSocket;
    }

    public void settings() {
        size(1280, 400, P2D);
    }

    // setup(), called before any looping is done
    public void setup() {

// TODO: Do something with this
//        game = new TankGame();
//        //String test = this.args[1];
//
//        try {
//            registerPlayer(); // this.args[0];
//        } catch (Exception e) {
//            e.printStackTrace();
//        } //

        Player player = tankGame.getPlayers().get(0);

        surface.setTitle(player.getPlayerName());

        // load our images for level and background
        bg = loadImage("images/background.jpg");

        // FPS limit
        frameRate(60);

        // new Level(image, destructionRes)
        level = new Level(this, loadImage("images/level.png"), 5);

        // initialize the physics and rendering engines
        physics = new Physics(this);
        _renderer = new Renderer(this);

        // create the playerObject
        playerObject = new PlayerObject(this, this, level,100,100);
        opponentObject = new PlayerObject(this, this, level, 150, 150);

        physics.add(playerObject);
        physics.add(opponentObject);
        _renderer.add(playerObject);
        _renderer.add(opponentObject);
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

        // Added with the new class/object system
        //playerObject.display();
        level.display();
        playerObject.display();
        //text(playerName, playerObject.getX(), playerObject.getY());
        //text(opponentName + "test", playerObject.getX(), playerObject.getY() - 20);

        opponentObject.display();

        // FPS counter
        fill(0,255,0);
        text(frameRate, 10,600);
    }
    /* Bullet */
// Acts similarly to PhysicsPixel
    /* Controls */
    public void keyPressed() {
        if (key == 'w' || key == 'W')
            playerObject.jump();
        if (key == 'a' || key == 'A')
        {
            playerObject.moveLeft();
            clientEndPointSocket.sendMessage(new Message(Actions.LEFT));
        }
        if (key == 'd' || key == 'D')
        {
            playerObject.moveRight();
            clientEndPointSocket.sendMessage(new Message(Actions.RIGHT, player.getPlayerID()));
        }
        if (key == '1')
        {
            player = tankGame.getPlayers().get(0);
            opponent = tankGame.getPlayers().get(1);
        }
        if (key == '2')
        {
            opponent = tankGame.getPlayers().get(0);
            player = tankGame.getPlayers().get(1);
        }
    }
    public void keyReleased() {
        if (key == 'a' || key == 'A')
        {
            playerObject.stopLeft();
            clientEndPointSocket.sendMessage(new Message(Actions.STOPLEFT));
        }
        if (key == 'd' || key == 'D')
        {
            playerObject.stopRight();
            clientEndPointSocket.sendMessage(new Message(Actions.STOPRIGHT));
        }
    }

//        public void mousePressed() {
//        if (mouseButton == LEFT)
//            playerObject.shoot();
//        else if (mouseButton == RIGHT)
//            playerObject.shootAlt();
//    }
//    public void mouseReleased() {
//        if (mouseButton == LEFT)
//            playerObject.stopShooting();
//        else if (mouseButton == RIGHT)
//            playerObject.stopShootingAlt();
//    }

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

//    @Override
//    public void setPlayerName(int nr, String name) {
//        if (nr != this.playerNr) {
//            println("ERROR: Wrong player number method setPlayerName()");
//            return;
//        } else {
//            println("player name " + name + " registered");
//        }
//        playerName = name;
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                println(playerName + "'s name has been set");
//            }
//        });
//    }

//    @Override
//    public void setOpponentName(int nr, String name) {
//        if (nr != this.playerNr) {
//            println("ERROR: Wrong player number method setOpponentName()");
//            return;
//        } else {
//            println("Your opponent is " + name);
//        }
//        opponentName = name;
//    }

//    @Override
//    public void jump(int playerNr) {
//        opponentObject.jump();
//    }
//
//    @Override
//    public void startGame() {
//        isInProgress = true;
//    }

    /* PhysicsObj */
// Any object that will need motion integrated will implement this
// these methods allows the Physics class to forward the object's position using its velocity
    public interface PhysicsObj {
        float getX();
        float getY();
        float getVX();
        float getVY();
        void setX(float pX);
        void setY(float pY);
        void setVX(float vX);
        void setVY(float vY);
        void checkConstraints();
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
        void display();
    }

//    private void registerPlayer() throws Exception {
//        playerName = "Jordy + " + random(0,100);
//        if ("".equals(playerName) || playerName == null) {
//            println("Enter your name before continuing");
//        } else {
//            println(playerName + " Has registered!");
//            playerNr = game.registerPlayer(playerName, this, singlePlayerMode);
//            if (playerNr != -1) {
//                println("player " + playerName + " registered, with playernumber: " + playerNr);
//            } else {
//                println("Name already defined");
//            }
//        }
//    }

//    private void notifyWhenReady() {
//        // Notify that the player is ready is startSocket the game.
//        bothReady = game.notifyWhenReady(playerNr);
//        if (bothReady) {
//            startGame();
//        } else {
//            println("Wait for the other player to ready up");
//        }
//    }

    public void opponentMoveLeft(String id){
        opponentObject.moveLeft();
    }

    public void opponentStopLeft(String id){
        opponentObject.stopLeft();
    }

    public void opponentMoveRight(String id){
        opponentObject.moveRight();
    }

    public void opponentStopRight(String id){
        opponentObject.stopRight();
    }

    /* Level */
// Provides methods for determining solid/empty pixels, and for removing/adding solid pixels
//    static public void main(String args[]) {
//        PApplet.main(new String[] { "tankgamegui.TankGameApplication" } ); //, args[0]
//    }
}