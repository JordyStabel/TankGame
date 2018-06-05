package tankgame.tankgameprocessing;

public interface IPhysicsObject {

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