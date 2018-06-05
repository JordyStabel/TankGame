package tankgame.tankgameprocessing;

import processing.core.*;
import java.util.ArrayList;
import java.util.List;

public class Physics extends PApplet{
    private long previousTime;
    private long currentTime;
    private final int fixedDeltaTime = 16;
    private float fixedDeltaTimeSeconds = (float)fixedDeltaTime / 1000.0f;
    private int leftOverDeltaTime = 0;

    private List<TestGame.PhysicsObj> objects;

    // Constructor
    Physics() {
        objects = new ArrayList<TestGame.PhysicsObj>();
    }

    public void add(TestGame.PhysicsObj obj) {
        objects.add((int)random(objects.size()),obj);
    }
    public void remove(TestGame.PhysicsObj obj) {
        objects.remove(obj);
    }

    // integrate motion
    public void update() {
        // This game uses fixed-sized timesteps.
        // The amount of time elapsed since last update is split up into units of 16 ms
        // any left over is pushed over to the next update
        // we take those units of 16 ms and update the simulation that many times.
        // a fixed timestep will make collision detection and handling (in the Player class, esp.) a lot simpler
        // A low framerate will not compromise any collision detections, while it'll still run at a consistent speed.

        currentTime = millis();
        long deltaTimeMS = currentTime - previousTime; // how much time has elapsed since the last update

        previousTime = currentTime; // reset previousTime

        // Find out how many timesteps we can fit inside the elapsed time
        int timeStepAmt = (int)((float)(deltaTimeMS + leftOverDeltaTime) / (float)fixedDeltaTime);

        // Limit the timestep amount to prevent freezing
        timeStepAmt = min(timeStepAmt, 1);

        // store left over time for the next frame
        leftOverDeltaTime = (int)deltaTimeMS - (timeStepAmt * fixedDeltaTime);

        for (int iteration = 1; iteration <= timeStepAmt; iteration++) {
            for (int i = 0; i < objects.size(); i++) { // loop through every PhysicsObj

                TestGame.PhysicsObj obj = objects.get(i);
                // get their velocity
                float velX = obj.getVX();
                float velY = obj.getVY();

                // add gravity
                velY += 980 * fixedDeltaTimeSeconds;
                obj.setVY(velY);

                // Always add x velocity
                obj.setX(obj.getX() + velX * fixedDeltaTimeSeconds);

                // if it's a player, only add y velocity if he's not on the ground.
                if (obj instanceof TestGame.Player) {
                    if (!(((TestGame.Player)obj).onGround && velY > 0))
                        obj.setY(obj.getY() + velY * fixedDeltaTimeSeconds);
                }
                else
                    obj.setY(obj.getY() + velY * fixedDeltaTimeSeconds);

                // allow the object to do collision detection and other things
                obj.checkConstraints();
            }
        }
    }
}