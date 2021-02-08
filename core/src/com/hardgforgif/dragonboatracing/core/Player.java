package com.hardgforgif.dragonboatracing.core;

import com.badlogic.gdx.physics.box2d.World;
import com.google.gson.JsonObject;
import com.hardgforgif.dragonboatracing.GameData;


public class Player extends Boat{

    public Player(float robustness, float speed, float acceleration, float maneuverability, int boatType, Lane lane) {
        super(robustness, speed, acceleration, maneuverability, boatType, lane);
    }

    /**
     * Updates the player based on the input given
     * @param pressedKeys W, A, S, D pressed status
     * @param delta time since last frame
     */
    public void updatePlayer(boolean[] pressedKeys, float delta) {
        // Check which angle you need to rotate to, then apply the roation
        if (pressedKeys[1])
            this.setTargetAngle(90f);
        else if (pressedKeys[3])
            this.setTargetAngle(-90f);
        else
            this.setTargetAngle(0f);
        rotateBoat(this.getTargetAngle());

        // Move the boat
        if (pressedKeys[0])
            moveBoat(1);
        else if (pressedKeys[2])
            moveBoat(-1);
        else
            moveBoat(0);

        // Update the sprite location to match the body
        this.getBoatSprite().setRotation((float)Math.toDegrees(this.getBoatBody().getAngle()));
        this.getBoatSprite().setPosition((this.getBoatBody().getPosition().x * GameData.METERS_TO_PIXELS) - this.getBoatSprite().getWidth() / 2,
                (this.getBoatBody().getPosition().y * GameData.METERS_TO_PIXELS) - this.getBoatSprite().getHeight() / 2);

        //Update stamina
        if (this.getStamina() > 30f) //Did this mean it was impossible to reach <30% stamina before?
            //stamina -= 1.5 * delta;
            if(pressedKeys[0] || pressedKeys[1] || pressedKeys[3]) {
                //Holding W, A, or D.
                this.setStamina(this.getStamina() - 4 * delta);
            }
            else if(pressedKeys[2]) {
                //Holding S.
                this.setStamina(this.getStamina() + 3 * delta);
            }
            else {
                //Not pressing any buttons.
                this.setStamina(this.getStamina() + 2 * delta);
            }

    }

    /**
     * Custom deserializer for Boat class.
     * @param obj JsonObject describing a Boat instance.
     * @param map Map needed for lanes.
     * @param world World needed to create a boat body.
     * @return Player instance.
     */
    public static Player from_json(JsonObject obj, Map map, World world) {
        // First initialise the boat with it's stats.
        Player b = new Player(obj.get("robustness").getAsFloat(), obj.get("speed").getAsFloat(),
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
}
