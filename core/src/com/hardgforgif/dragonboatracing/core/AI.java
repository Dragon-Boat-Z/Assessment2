package com.hardgforgif.dragonboatracing.core;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.google.gson.JsonObject;
import com.hardgforgif.dragonboatracing.GameData;

public class AI extends Boat{

    private Vector2 laneChecker;
    private Vector2 objectChecker;
    private boolean isDodging = false;
    private boolean isTurning = false;
    private boolean isBraking = false;
    private boolean isAccelerating = false;
    private float detectedObstacleYPos;

    public AI(float robustness, float speed, float acceleration, float maneuverability, int boatType, Lane lane) {
        super(robustness, speed, acceleration, maneuverability, boatType, lane);
        this.setRobustness(this.getRobustness()* GameData.difficulty[GameData.currentLeg]);
        this.setStamina(this.getStamina() * GameData.difficulty[GameData.currentLeg]);
        this.setManeuverability(this.getManeuverability() * GameData.difficulty[GameData.currentLeg]);
        this.setSpeed(this.getSpeed() * GameData.difficulty[GameData.currentLeg]);
        this.setAcceleration(this.getAcceleration() * GameData.difficulty[GameData.currentLeg]);
    }

    /**
     * Get a point at a given distance in front of the boat
     * @param distance The distance to the new point
     * @return
     */
    private Vector2 getAIPredictionVector(float distance) {
        // Get the coordinates of the center of the boat
        float originX = this.getBoatBody().getPosition().x * GameData.METERS_TO_PIXELS;
        float originY = this.getBoatBody().getPosition().y * GameData.METERS_TO_PIXELS;

        // First we need to calculate the position of the boat's head (the front of the boat)
        Vector2 boatHeadPos = new Vector2();
        float radius = (this.getBoatSprite().getHeight() * this.getBoatSprite().getScaleY())/2;
        boatHeadPos.set(originX + radius * MathUtils.cosDeg(this.getBoatSprite().getRotation() + 90),
                originY + radius * MathUtils.sinDeg(this.getBoatSprite().getRotation() + 90));

        // Calculate the x and y positions of the direction vector, based on the rotation of the boat
        double auxAngle = this.getBoatSprite().getRotation() % 90;
        if (this.getBoatSprite().getRotation() < 90 || this.getBoatSprite().getRotation() >= 180 && this.getBoatSprite().getRotation() < 270)
            auxAngle = 90 - auxAngle;
        auxAngle = auxAngle * MathUtils.degRad;
        float x = (float) (Math.cos(auxAngle) * distance);
        float y = (float) (Math.sin(auxAngle) * distance);

        // Build the target vector based on the position of the player's head
        Vector2 target = new Vector2();
        if (this.getBoatSprite().getRotation() < 90)
            target.set(boatHeadPos.x - x, boatHeadPos.y + y);
        else if (this.getBoatSprite().getRotation() < 180)
            target.set(boatHeadPos.x - x, boatHeadPos.y - y);
        else if (this.getBoatSprite().getRotation() < 270)
            target.set(boatHeadPos.x + x, boatHeadPos.y - y);
        else
            target.set(boatHeadPos.x + x, boatHeadPos.y + y);

        return target;
    }

    /**
     * Sets the target angle attribute to keep the boat in lane, based on the limits at the predicted location
     * @param predictLimits the limits of the lane at the predicted location
     */
    private void stayInLane(float[] predictLimits){
        float laneWidth = predictLimits[1] - predictLimits[0];
        float middleOfLane = predictLimits[0] + laneWidth / 2;

        // If the predicted location is outside the lane, rotate the boat
        if (this.laneChecker.x < predictLimits[0] && this.getBoatSprite().getRotation() == 0){
            this.setTargetAngle(-15f);
            isTurning = true;
        }

        else if (this.laneChecker.x > predictLimits[1] && this.getBoatSprite().getRotation() == 0){
            this.setTargetAngle(15f);
            isTurning = true;
        }

        // If the predicted location is far enough into the lane, straighten the boat
        else if (this.laneChecker.x < middleOfLane - laneWidth / 4 && this.getBoatSprite().getRotation() > 0){
            this.setTargetAngle(0f);
            isTurning = false;
        }

        else if (this.laneChecker.x > middleOfLane + laneWidth / 4  && this.getBoatSprite().getRotation() < 0){
            this.setTargetAngle(0f);
            isTurning = false;
        }

        // Apply the rotation
        rotateBoat(this.getTargetAngle());
        this.getBoatSprite().setRotation((float)Math.toDegrees(this.getBoatBody().getAngle()));
    }

    /**
     * Checks for obstacles in range of the AI
     * @return True if there's an obstacle in range, false otherwise
     */
    private boolean obstaclesInRange(){
        for (Obstacle obstacle : this.getLane().getObstacles()){
            //Don't bother dodging if Obstacle is a PowerUp.
            if(!obstacle.isPowerUp()){
                // Get the obstacles attributes
                float width = obstacle.getObstacleSprite().getWidth() * obstacle.getObstacleSprite().getScaleX();
                float height = obstacle.getObstacleSprite().getHeight() * obstacle.getObstacleSprite().getScaleY();
                float posX = obstacle.getObstacleSprite().getX() + obstacle.getObstacleSprite().getWidth() / 2 - width / 2;
                float posY = obstacle.getObstacleSprite().getY() + obstacle.getObstacleSprite().getHeight() / 2 - height / 2;

                // Get the boat  attributes
                float boatLeftX = this.objectChecker.x - this.getBoatSprite().getWidth() / 2 * this.getBoatSprite().getScaleX();
                float boatRightX = this.objectChecker.x + this.getBoatSprite().getWidth() / 2 * this.getBoatSprite().getScaleX();

                // Check for obstacles
                if (boatRightX >= posX && boatLeftX <= posX + width &&
                        this.objectChecker.y >= posY && this.getBoatSprite().getY() + this.getBoatSprite().getHeight() / 2 <= posY){
                    detectedObstacleYPos = posY;
                    return true;
                }
            }
            
        }
        return false;
    }

    /**
     * Sets the target angle attribute to keep the boat from hitting an obstacle
     */
    private void dodgeObstacles(){
        if (obstaclesInRange()){
            float boatPosX = this.getBoatSprite().getX() + this.getBoatSprite().getWidth() / 2;

            // If the boat is turning into an object, stop turning
            if (isTurning){
                this.setTargetAngle(0f);
                isTurning = false;
            }
            // Otherwise check which way is better to dodge, then set the rotation
            else if (this.getRightLimit() - boatPosX < boatPosX - this.getLeftLimit()){
                this.setTargetAngle(15f);
            }
            else
                this.setTargetAngle(-15f);

            // Mark that the AI is currently dodging an obstacle
            isDodging = true;

            //If the AI is braking:
            //isBraking = true;

            //Apply the roation
            rotateBoat(this.getTargetAngle());
            this.getBoatSprite().setRotation((float)Math.toDegrees(this.getBoatBody().getAngle()));
        }
    }

    /**
     * Updates the AI to apply appropriate movement and rotation
     * @param delta Time since last frame
     */
    public void updateAI(float delta) {
        // Start by matching the location of the sprite with the location of the sprite
        this.getBoatSprite().setPosition((this.getBoatBody().getPosition().x * GameData.METERS_TO_PIXELS) - this.getBoatSprite().getWidth() / 2,
                (this.getBoatBody().getPosition().y * GameData.METERS_TO_PIXELS) - this.getBoatSprite().getHeight() / 2);

        // Create the prediction vectors
        this.laneChecker = getAIPredictionVector(400f);
        this.objectChecker = getAIPredictionVector(300f);

        // Store the limits of the lane at the laneChecker prediction vector
        float[] predictLimits = getLimitsAt(this.laneChecker.y);

        // If the Ai is dodging an obstacle
        if (isDodging){
            // Apply rotation
            rotateBoat(this.getTargetAngle());
            this.getBoatSprite().setRotation((float)Math.toDegrees(this.getBoatBody().getAngle()));

            float boatFrontLocation = this.getBoatSprite().getY() + this.getBoatSprite().getHeight() / 2 +
                    this.getBoatSprite().getHeight() / 2 * this.getBoatSprite().getScaleY();

            // If the front of the boat passed the obstacle, stop dodging
            if (boatFrontLocation >= detectedObstacleYPos)
                isDodging = false;

        }
        // Otherwise look for obstacles to dodge and try to stay in th lane
        else{
            dodgeObstacles();
            stayInLane(predictLimits);
        }

        // Apply the movement
        if (this.getStamina() > 50f) {
            // 'hold W'
            isAccelerating = true;
            isBraking = false;
            moveBoat(1);
        }
        else if (this.getStamina() > 30f) {
            // 'do nothing'
            isAccelerating = false;
            isBraking = false;
            moveBoat(0);
        }
        else {
            // 'hold S'
            isAccelerating = false;
            isBraking = true;
            moveBoat(-1);
        }

        //Update stamina
        //stamina -= 1.5 * delta;
        if(this.getStamina() >= 0f) {
            if(isAccelerating) {
                //AI is turning. Should also include accelerating!
                this.setStamina(this.getStamina() - 4 * delta);
            }
            else if(isBraking) {
                //AI is braking.
                this.setStamina(this.getStamina() + 3 * delta);
            }
            else if(isDodging) {
                //Turning left or right should leave stamina as is.
            }
            else {
                //Not accelerating or braking.
                this.setStamina(this.getStamina() + 2 * delta);
            }}
    }

    /**
     * Custom deserializer for AI class.
     * @param obj JsonObject representing a Boat instance.
     * @param map Map needed to get the lane.
     * @param world needed to create the boat body.
     * @return AI instance.
     */
    public static AI from_json(JsonObject obj, Map map, World world) {
        // First initialise the boat with it's stats.
        AI b = new AI(obj.get("robustness").getAsFloat(), obj.get("speed").getAsFloat(),
                obj.get("acceleration").getAsFloat(), obj.get("maneuverability").getAsFloat(),
                obj.get("boat_type").getAsInt(), map.getLanes()[obj.get("lane").getAsInt()]);

        // Then update the in play variables of that boat from the save-state.
        b.setStamina(obj.get("stamina").getAsFloat());
        b.setCurrentSpeed(obj.get("current_speed").getAsFloat());
        b.setTurningSpeed(obj.get("turning_speed").getAsFloat());
        b.setTargetAngle(obj.get("target_angle").getAsFloat());
        b.createBoatBody(world, obj.get("x_position").getAsFloat()*(1/GameData.METERS_TO_PIXELS),obj.get("y_position").getAsFloat()*(1/GameData.METERS_TO_PIXELS), "Boat1.json");

        return b;
    }

    public Vector2 getLaneChecker(){
        return this.laneChecker;
    }

    public Vector2 getObjectChecker(){
        return this.objectChecker;
    }

    public boolean getIsDodging(){ 
        return this.isDodging; 
    }

    public boolean getIsBraking(){ 
        return this.isBraking; 
    }

    public boolean getIsAccelerating(){ 
        return this.isAccelerating; 
    }

    //setters
    public void setIsDodging(boolean isDodging){
        this.isDodging = isDodging;
    }
}
