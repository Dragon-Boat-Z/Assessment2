package com.hardgforgif.dragonboatracing.core;

import com.hardgforgif.dragonboatracing.GameData;

public class PowerUpHealth extends PowerUp {

    public PowerUpHealth() {
        super("PowerUps/HealthBoost.png");
    }

    @Override
    public void applyPowerUp(Boat user) {
        //Take the Boat using this PowerUp and give it a health boost.
        int type = user.getBoatType();
        user.setRobustness(user.getRobustness() * 1.5f);
        if(user.getRobustness() > GameData.boatsStats[type][0]);
    }
    
}
