package tankgame;

public class Coordinates {
    private int x;
    private int y;

    // Return X int value of coordinate
    public int getX() {
        return x;
    }

    // Set the X int value of coordinate
    private void setX(int x) {
        this.x = x;
    }

    // Return Y int value of coordinate
    public int getY() {
        return y;
    }

    // Set the Y int value of coordinate
    private void setY(int y) {
        this.y = y;
    }

    // Set both X & Y values for a coordinate
    public void setCoordinates(int x, int y) {
        setX(x);
        setY(y);
    }

    // Constructor for coordinates
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
}