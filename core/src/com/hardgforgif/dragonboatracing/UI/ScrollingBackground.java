package com.hardgforgif.dragonboatracing.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ScrollingBackground {

    private static final int DEFAULT_SPEED = 80;
    private static final int ACCELERATION = 50;
    private static final int TARGET_ACQUIRED_ACCELERATION = 200;

    private Texture image;
    private float y1, y2;
    private int speed; // in pixels/sec
    private int targetSpeed;
    private boolean speedFixed;
    private float imageScale;
    private float scaledHeight;

    public ScrollingBackground() {
        image = new Texture("square.png");

        y1 = 0;
        y2 = image.getHeight();
        speed = 0;
        targetSpeed = DEFAULT_SPEED;
        imageScale = 0;
        speedFixed = true;
    }

    /**
     * Updates and renders the Scrolling background object on a batch
     * @param deltaTime The time passes since the last frame
     * @param batch The batch to render to
     */
    public void updateAndRender (float deltaTime, Batch batch) {
        // speed adjustment to reach goal
        if (speed < targetSpeed) {
            speed += TARGET_ACQUIRED_ACCELERATION * deltaTime;
            if (speed > targetSpeed) speed = targetSpeed;
        }
        else if (speed > targetSpeed) {
            speed -= TARGET_ACQUIRED_ACCELERATION * deltaTime;
            if (speed < targetSpeed) speed = targetSpeed;
        }

        if (!speedFixed) speed += ACCELERATION * deltaTime;

        y1 -= speed * deltaTime;
        y2 -= speed * deltaTime;

        // if image reaches the bottom of screen and is not visible,
        // put it back on top

        scaledHeight = image.getHeight() * imageScale;

        if (y1 + scaledHeight <= 0) y1 = y2 + scaledHeight;
        if (y2 + scaledHeight <= 0) y2 = y1 + scaledHeight;

        // render
        batch.draw(image, 0, y1, Gdx.graphics.getWidth(), scaledHeight);
        batch.draw(image, 0, y2, Gdx.graphics.getWidth(), scaledHeight);
    }

    /**
     * Resize the object to a given size
     * @param width Width of the object
     * @param height Height of the object
     */
    public void resize(int width, int height) {
        imageScale = width / image.getWidth();
    }

    /**
     * Set the scrolling speed in pixels/sec
     * @param targetSpeed
     */
    public void setSpeed(int targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    public void setSpeedFixed(boolean speedFixed) {
        this.speedFixed = speedFixed;
    }

    //getters
    public Texture getImage(){
        return this.image;
    }

    public float getY1(){
        return this.y1;
    }

    public float getY2(){
        return this.y2;
    }

    public int getSpeed(){
        return this.speed;
    }

    public int getTargetSpeed(){
        return this.targetSpeed;
    }

    public boolean getSpeedFixed(){
        return this.speedFixed;
    }

    public float getImageScale(){
        return this.imageScale;
    }

    public float getScaledHeight(){
        return this.scaledHeight;
    }

    public static int getDefaultSpeed() {
        return DEFAULT_SPEED;
    }

    public static int getAcceleration() {
        return ACCELERATION;
    }

    public static int getTargetAcquiredAcceleration() {
        return TARGET_ACQUIRED_ACCELERATION;
    }

    public void setY1(float y1){
        this.y1 = y1;
    }

    public void setY2(float y2){
        this.y2 = y2;
    }
}
