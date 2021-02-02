package com.hardgforgif.dragonboatracing.core;

public class PowerUpStamina extends PowerUp {

    public PowerUpStamina() {
        super("PowerUps/StaminaBoost.png");
    }

    @Override
    public void applyPowerUp(Boat user) {
        //Take the Boat using this PowerUp and give it a stamina boost.
        if(user.getPowerUpTimer() <= 0) {
            user.setStamina(user.getStamina() * 1.5f);
        }
    }
    
}

