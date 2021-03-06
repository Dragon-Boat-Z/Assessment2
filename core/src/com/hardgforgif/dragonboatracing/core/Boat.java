package com.hardgforgif.dragonboatracing.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.hardgforgif.dragonboatracing.GameData;
import com.hardgforgif.dragonboatracing.BodyEditorLoader;

import java.lang.reflect.Type;

public class Boat {
    // Boat specs
    @Expose
    private float robustness,maneuverability,speed,acceleration;
    @Expose
    private float stamina = 120f;
    @Expose
    private float current_speed = 20f;
    @Expose
    private float turningSpeed = 0.25f;
    @Expose
    private float targetAngle = 0f;
    @Expose
    private Lane lane;
    @Expose
    private int boatType;

    private int laneNo;
    private float powerupTimer;
    private boolean invulnerable;

    private Sprite boatSprite;
    private Texture boatTexture;
    private Body boatBody;
    private TextureAtlas textureAtlas;
    private Animation animation;
    private float leftLimit;
    private float rightLimit;

    public Boat(float robustness, float speed, float acceleration, float maneuverability, int boatType, Lane lane) {
        this.robustness = robustness;
        this.speed = speed;
        this.acceleration = acceleration;
        this.maneuverability = maneuverability;
        this.turningSpeed *= this.maneuverability / 100;
        this.boatType = boatType;

        boatTexture = new Texture("Boat" + (boatType + 1) + ".png");
        textureAtlas = new TextureAtlas(Gdx.files.internal("Boats/Boat" + (boatType + 1) +  ".atlas"));
        animation = new Animation(1/15f, textureAtlas.getRegions());

        this.lane = lane;
        this.laneNo = lane.getLaneNo();

        this.powerupTimer = 0;
        this.invulnerable = false;
    }

    /**
     * Creates a boat body
     * @param world World to create the body in
     * @param posX x location of the body, in meters
     * @param posY y location of the body, in meters
     * @param bodyFile the name of the box2D editor json file for the body fixture
     */
    public void createBoatBody(World world, float posX, float posY, String bodyFile){
        boatSprite = new Sprite(boatTexture);
        boatSprite.scale(-0.675f);

        // Define the body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(posX, posY);

        // Create the body
        boatBody = world.createBody(bodyDef);
        // Mark the body as a boat's body
        boatBody.setUserData(this);

        // Load the body fixture from box2D editor
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal(bodyFile));
        FixtureDef fixtureDef = new FixtureDef();

        // Set the physical properties of the body
        fixtureDef.density = 0f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        // Attach the fixture to the body
        float scale = boatSprite.getWidth() / GameData.METERS_TO_PIXELS * boatSprite.getScaleX();
        loader.attachFixture(boatBody, "Name", fixtureDef, scale);
    }

    /**
     * Draws the boat on the batch, with animations
     * @param batch The batch to draw on
     */
    public void drawBoat(Batch batch){
        batch.begin();
        batch.draw((TextureRegion) animation.getKeyFrame(GameData.currentTimer, true), boatSprite.getX(), boatSprite.getY(), boatSprite.getOriginX(),
                boatSprite.getOriginY(),
                boatSprite.getWidth(), boatSprite.getHeight(), boatSprite.getScaleX(), boatSprite.
                        getScaleY(), boatSprite.getRotation());
        batch.end();
    }

    /**
     * Updates the boat's limits in the lane based on it's location
     */
    public void updateLimits(){
        int i;
        for (i = 1; i < lane.getLeftIterator(); i++){
            if (lane.getLeftBoundary()[i][0] > boatSprite.getY() + (boatSprite.getHeight() / 2)) {
                break;
            }
        }
        leftLimit = lane.getLeftBoundary()[i - 1][1];

        for (i = 1; i < lane.getRightIterator(); i++){
            if (lane.getRightBoundary()[i][0] > boatSprite.getY() + (boatSprite.getHeight() / 2)) {
                break;
            }
        }
        rightLimit = lane.getRightBoundary()[i - 1][1];
    }

    /**
     * Gets left and right boundary of the lane at a given y-position
     * @param yPosition Y-position we want the boundaries of.
     * @return float[] [x1,x2] where x1 is the left boundary and x2 is the right boundary.
     */
    public float[] getLimitsAt(float yPosition){
        float[] lst = new float[2];
        int i;
        for (i = 1; i < lane.getLeftIterator(); i++){
            if (lane.getLeftBoundary()[i][0] > yPosition) {
                break;
            }
        }
        lst[0] = lane.getLeftBoundary()[i - 1][1];

        for (i = 1; i < lane.getRightIterator(); i++){
            if (lane.getRightBoundary()[i][0] > yPosition) {
                break;
            }
        }
        lst[1] = lane.getRightBoundary()[i - 1][1];
        return lst;
    }

    /**
     * Checks if the boat finished the race
     * @return True if the boat passed the finish line, false otherwise
     */
    public boolean hasFinished(){
        if (boatSprite.getY() + boatSprite.getHeight() / 2 > 9000f)
            return true;
        return false;
    }

    /**
     * Moves the boat forward, based on its rotation and whether accelerating or decelerating.
     * move_state = 1     means W is being held
     * move_state = 0     means no key is being held
     * move_state = -1    means S is being held.
     */
    public void moveBoat(int move_state){
        /*
        current_speed += 0.15f * (acceleration/90)  * (stamina/100);
        if (current_speed > speed)
            current_speed = speed;
        if (stamina < 70f && current_speed > speed * 0.8f)
            current_speed = speed * 0.8f;
        if (current_speed < 0)
            current_speed = 0;
        */
        if(stamina < 50f) {
            //Stamina is <50%. Acceleration and top speed capped significantly.
            setCurrentSpeed(this.getCurrentSpeed() + move_state * 0.15f * ((acceleration * 0.6f)/90)  * (stamina/100)); 
            if (this.getCurrentSpeed() > this.getSpeed() * 0.8f)
                setCurrentSpeed(this.getSpeed() * 0.8f);
        }
        else if(stamina < 75f) {
            //Stamina is >50% but <75%. Acceleration and top speed capped slightly.
            setCurrentSpeed(this.getCurrentSpeed() + move_state * 0.15f * ((acceleration * 0.8f)/90)  * (stamina/100)); 
            if (this.getCurrentSpeed() > this.getSpeed() * 0.9f)
                setCurrentSpeed(this.getSpeed() * 0.9f);
        }
        else {
            //Stamina is >75%. Acceleration and top speed are not capped.
            setCurrentSpeed(this.getCurrentSpeed() + move_state * 0.15f * (acceleration/90)  * (stamina/100)); 
            if (this.getCurrentSpeed() > this.getSpeed())
                setCurrentSpeed(this.getSpeed());
        }
        if (this.getCurrentSpeed() < 0)
            setCurrentSpeed(0);


        // Get the coordinates of the center of the boat
        float originX = boatBody.getPosition().x * GameData.METERS_TO_PIXELS;
        float originY = boatBody.getPosition().y * GameData.METERS_TO_PIXELS;

        // First we need to calculate the position of the player's head (the front of the boat)
        // So we can move him based on this and not the center of the boat
        Vector2 boatHeadPos = new Vector2();
        float radius = boatSprite.getHeight()/2;
        boatHeadPos.set(originX + radius * MathUtils.cosDeg(boatSprite.getRotation() + 90),
                originY + radius * MathUtils.sinDeg(boatSprite.getRotation() + 90));

        // Create the vector that shows which way we need to move
        Vector2 target = new Vector2();

        // Calculate the x and y positions of the direction vector, based on the rotation of the boat
        double auxAngle = boatSprite.getRotation() % 90.01;
        if (boatSprite.getRotation() < 90 || boatSprite.getRotation() > 180 && boatSprite.getRotation() < 270)
            auxAngle = 90 - auxAngle;
        auxAngle = auxAngle * MathUtils.degRad;
        float x = (float) (Math.cos(auxAngle) * speed);
        float y = (float) (Math.sin(auxAngle) * speed);

        // Build the direction vector based on the position of the player's head
        if(this.boatType == 0) {
            System.out.println(boatSprite.getRotation());
        }

        if (boatSprite.getRotation() == -90f) {
            target.set(boatHeadPos.x - x, boatHeadPos.y);
        }
        else if (boatSprite.getRotation() == 90f) {
            target.set(boatHeadPos.x + x, boatHeadPos.y);
        }
        else if (boatSprite.getRotation() < 90) {
            target.set(boatHeadPos.x - x, boatHeadPos.y + y);
        }
        else {
            target.set(boatHeadPos.x + x, boatHeadPos.y + y);
        }

        Vector2 direction = new Vector2();
        Vector2 velocity = new Vector2();
        Vector2 movement = new Vector2();

        direction.set(target).sub(boatHeadPos).nor();
        velocity.set(direction).scl(current_speed);
        movement.set(velocity).scl(Gdx.graphics.getDeltaTime());

        boatBody.setLinearVelocity(movement);
    }

    /**
     * Rotate the boat until it reaches the given angle, based on it's turning speed and stamina
     * @param angle angle to rotate to
     */
    public void rotateBoat(float angle){
        // Calculate the difference between the target angle and the current rotation of the boat
        float angleDifference = angle - boatBody.getAngle() * MathUtils.radDeg;

        if (Math.abs(angleDifference) < turningSpeed) {
            boatBody.setTransform(boatBody.getPosition(), angle * MathUtils.degRad);
            return;
        }

        // Create the new angle we want the player to be rotated to every frame, based on the turning speed and stamina
        float newAngle = boatSprite.getRotation();

        if (angleDifference < 0)
            newAngle += turningSpeed * (-1) * (this.stamina / 70);
        else if (angleDifference > 0)
            newAngle += turningSpeed * (this.stamina / 70);

        boatBody.setTransform(boatBody.getPosition(), newAngle * MathUtils.degRad);
    }

    /**
     * Custom Json Serializer for Boat class, implementing the Gson library.
     * @return JsonObject obj describing a Boat instance.
     */
    public static class BoatSerializer implements JsonSerializer<Boat> {
        public JsonElement serialize(Boat aBoat, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject obj = new JsonObject();
            obj.add("lane", new JsonPrimitive(aBoat.getLane().getLaneNo()));
            obj.add("robustness", new JsonPrimitive(aBoat.getRobustness()));
            obj.add("maneuverability", new JsonPrimitive(aBoat.getManeuverability()));
            obj.add("speed", new JsonPrimitive(aBoat.getSpeed()));
            obj.add("acceleration", new JsonPrimitive(aBoat.getAcceleration()));
            obj.add("stamina", new JsonPrimitive(aBoat.getStamina()));
            obj.add("current_speed", new JsonPrimitive(aBoat.getCurrentSpeed()));
            obj.add("turning_speed", new JsonPrimitive(aBoat.getTurningSpeed()));
            obj.add("target_angle", new JsonPrimitive(aBoat.getTargetAngle()));
            obj.add("boat_type", new JsonPrimitive(aBoat.getBoatType()));
            obj.add("x_position", new JsonPrimitive(aBoat.getBoatSprite().getX()));
            obj.add("y_position", new JsonPrimitive(aBoat.getBoatSprite().getY()));

            return obj;
        }
    }

    //getters
    public float getRobustness(){
        return this.robustness;
    }

    public float getStamina(){
        return this.stamina;
    }

    public float getManeuverability(){
        return this.maneuverability;
    }

    public float getSpeed(){
        return this.speed;
    }

    public float getAcceleration(){
        return this.acceleration;
    }

    public float getCurrentSpeed(){
        return this.current_speed;
    }

    public float getTurningSpeed(){
        return this.turningSpeed;
    }

    public float getTargetAngle(){
        return this.targetAngle;
    }

    public Sprite getBoatSprite(){
        return this.boatSprite;
    }

    public Texture getBoatTexture(){
        return this.boatTexture;
    }

    public Body getBoatBody(){
        return this.boatBody;
    }

    public TextureAtlas getTextureAtlas(){
        return this.textureAtlas;
    }

    public Animation getAnimation(){
        return this.animation;
    }

    public Lane getLane(){
        return this.lane;
    }

    public int getBoatType() { return this.boatType; }

    public float getLeftLimit(){
        return this.leftLimit;
    }

    public float getRightLimit(){
        return this.rightLimit;
    }
    
    public float getPowerUpTimer() {
        return this.powerupTimer;
    }

    public boolean isInvulnerable() {
        return this.invulnerable;
    }

    public void setRobustness(float f) { this.robustness = f; }

    public void setCurrentSpeed( float f ) { this.current_speed = f; }

    public void setStamina(float stamina) {
        this.stamina = stamina;
    }

    public void setManeuverability(float maneuverability) {
        this.maneuverability = maneuverability;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public void setTurningSpeed(float turningSpeed) {
        this.turningSpeed = turningSpeed;
    }

    public void setTargetAngle(float targetAngle) {
        this.targetAngle = targetAngle;
    }

    public void setBoatType(int type) { 
        this.boatType = type; 
    }

    public void setPowerUpTimer(float time) {
        this.powerupTimer = time;
    }

    public void setInvulnerability(boolean toggle) {
        this.invulnerable = toggle;
    }
    public void setLimits(float left, float right) {
        this.leftLimit = left;
        this.rightLimit = right;
    }

    public void setLaneNo(int num) {
        this.laneNo = num;
    }

    public void setPosition(float x, float y) {
        this.boatSprite.setX(x);
        this.boatSprite.setY(y);
    }
}
