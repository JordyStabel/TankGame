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

    /**
     * Set the coordinates of an object
     *
     * @param x the horizontal coordinate to be set
     * @param y the vertical vertical coordinate to be set
     */
    public void setCoordinates(int x, int y) {
        setX(x);
        setY(y);
    }

    /**
     * Constructor for coordinates
     * @param x the horizontal coordinate to be set
     * @param y the vertical vertical coordinate to be set
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }


}