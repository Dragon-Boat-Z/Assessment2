package com.hardgforgif.dragonboatracing.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.google.gson.JsonObject;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.BodyEditorLoader;

public class Obstacle {
    private Sprite obstacleSprite;
    private Texture obstacleTexture;
    private Body obstacleBody;
    private int obstacleType;
    private float positionX;
    private float positionY;

    public Obstacle(String textureName){
        obstacleTexture = new Texture(textureName);
        positionX = 0;
        positionY = 0;
    }

    public Obstacle(String textureName, float x, float y){
        obstacleTexture = new Texture(textureName);
        positionX = x;
        positionY = y;
    }

    /**
     * Creates a new obstacle body/
     * @param world World to create the body in
     * @param posX x location of the body, in meters
     * @param posY y location of the body, in meters
     * @param bodyFile the name of the box2D editor json file for the body fixture
     * @param scale float preserving the scale of the obstacle sprite
     */
    public void createObstacleBody(World world, float posX, float posY, String bodyFile, float scale){
        this.positionX = posX;
        this.positionY = posY;

        obstacleSprite = new Sprite(obstacleTexture);
        obstacleSprite.scale(scale);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set(posX, posY);
        obstacleBody = world.createBody(bodyDef);

        obstacleBody.setUserData(this);

        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(bodyFile));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        scale = obstacleSprite.getWidth() / GameData.METERS_TO_PIXELS * obstacleSprite.getScaleX();
        loader.attachFixture(obstacleBody, "Name", fixtureDef, scale);

        obstacleSprite.setPosition((obstacleBody.getPosition().x * GameData.METERS_TO_PIXELS) - obstacleSprite.getWidth() / 2,
                (obstacleBody.getPosition().y * GameData.METERS_TO_PIXELS) - obstacleSprite.getHeight() / 2);
    }

    /**
     * Draw the obstacle
     * @param batch Batch to draw on
     */
    public void drawObstacle(Batch batch){
        obstacleSprite.setPosition((obstacleBody.getPosition().x * GameData.METERS_TO_PIXELS) - obstacleSprite.getWidth() / 2,
                (obstacleBody.getPosition().y * GameData.METERS_TO_PIXELS) - obstacleSprite.getHeight() / 2);
        batch.begin();
        batch.draw(obstacleSprite, obstacleSprite.getX(), obstacleSprite.getY(), obstacleSprite.getOriginX(),
                obstacleSprite.getOriginY(),
                obstacleSprite.getWidth(), obstacleSprite.getHeight(), obstacleSprite.getScaleX(),
                obstacleSprite.getScaleY(), obstacleSprite.getRotation());
        batch.end();
    }

    public boolean isPowerUp() {
        return false;
    }

    //getters
    public Sprite getObstacleSprite(){
        return this.obstacleSprite;
    }

    public Texture getObstacleTexture(){
        return this.obstacleTexture;
    }

    public Body getObstacleBody(){
        return this.obstacleBody;
    }

    public void setObstacleBody( Body b ) { this.obstacleBody = b; }

    public void setObstacleType( int i ) { this.obstacleType = i; }

    public int getObstacleType(){ return this.obstacleType; }

    public float getX() {
        return this.positionX;
    }
    public float getY() {
        return this.positionY;
    }
}
