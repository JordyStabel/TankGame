package tankgame.tankgameprocessing;

import processing.core.*;

public class Level extends PApplet {

    // Background for the level
    private PImage backgroundImage;

    // How wide the destructed pixels are
    private int destructionRes;

    public Level(PImage pic, int destructionRes) {

        this.destructionRes = destructionRes;

        // Copy pic over to backgroundImage, replacing all pink (RGB: 255,0,255) pixels with transparent pixels
        backgroundImage = createImage(pic.width, pic.height, ARGB);
        backgroundImage.loadPixels();
        pic.loadPixels();
        for (int i = 0; i < backgroundImage.width * backgroundImage.height; i++) {
            if (red(pic.pixels[i]) == 255 && green(pic.pixels[i]) == 0 && blue(pic.pixels[i]) == 255)
                backgroundImage.pixels[i] = color(0,0);
            else
                backgroundImage.pixels[i] = pic.pixels[i];
        }
        backgroundImage.updatePixels();
    }

    // Render terrain onto the main screen
    public void draw(float x, float y) {
        image(backgroundImage, x,y);
    }

    // Return the terrain's width and height

    public int width() {
        return backgroundImage.width;
    }
    public int height() {
        return backgroundImage.height;
    }

    // Update - apply pixels[]'s changes onto the image
    public void update() {
        backgroundImage.updatePixels();
    }

    // Determine if a pixel is solid based on whether or not it's transparent
    public boolean isPixelSolid(int x, int y) {
        if (x > 0 && x < backgroundImage.width && y > 0 && y < backgroundImage.height)
            return backgroundImage.pixels[x + y * backgroundImage.width] != color(0,0);
        return false; // border is not solid
    }

    // Color in a pixel, making it solid
    public void addPixel(int c, int x, int y) {
        if (x > 0 && x < backgroundImage.width && y > 0 && y < backgroundImage.height)
            backgroundImage.pixels[x + y * backgroundImage.width] = c;
    }
    // Make a pixel solid
    public void removePixel(int x, int y) {
        if (x > 0 && x < backgroundImage.width && y > 0 && y < backgroundImage.height)
            backgroundImage.pixels[x + y * backgroundImage.width] = color(0,0);
    }
    // Get a pixel's color
    public int getColor(int x, int y) {
        if (x > 0 && x < backgroundImage.width && y > 0 && y < backgroundImage.height)
            return backgroundImage.pixels[x + y * backgroundImage.width];
        return 0;
    }

    // Find a normal at a position
    public float[] getNormal(int x, int y) {
        // First find all nearby solid pixels, and create a vector to the average solid pixel from (x,y)
        float avgX = 0;
        float avgY = 0;
        for (int w = -3; w <= 3; w++) {
            for (int h = -3; h <= 3; h++) {
                if (isPixelSolid(x + w, y + h)) {
                    avgX -= w;
                    avgY -= h;
                }
            }
        }
        float len = sqrt(avgX * avgX + avgY * avgY); // get the distance from (x,y)
        return new float[]{avgX/len, avgY/len}; // normalize the vector by dividing by that distance
    }
}