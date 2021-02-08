package com.hardgforgif.dragonboatracing.core;

public class PowerUpSpeed extends PowerUp {

    public PowerUpSpeed() {
        super("PowerUps/SpeedBoost.png");
        setObstacleType(10);
    }

    @Override
    public void applyPowerUp(Boat user) {
        //Take the Boat using this PowerUp and give it a speed boost.
        //Speed boost consists of increasing the boat's current speed. If this goes over it's max speed, this speed is only allowed for 2 seconds.
        if(user.getPowerUpTimer() <= 0) {
            user.setCurrentSpeed(user.getCurrentSpeed() * 1.5f);
            if(user.getCurrentSpeed() > user.getSpeed()) {
                //If the newly assigned speed exceeds this Boat type's max speed, don't cap it until 2 seconds have passed.
                user.setSpeed(user.getCurrentSpeed());
                user.setPowerUpTimer(2f);
            }
        }
    }
    
}
