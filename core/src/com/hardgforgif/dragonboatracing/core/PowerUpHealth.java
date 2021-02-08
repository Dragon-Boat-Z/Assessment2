package com.hardgforgif.dragonboatracing.core;

import com.hardgforgif.dragonboatracing.GameData;

public class PowerUpHealth extends PowerUp {

    public PowerUpHealth() {
        super("PowerUps/HealthBoost.png");
        setObstacleType(8);
    }

    @Override
    public void applyPowerUp(Boat user) {
        //Take the Boat using this PowerUp and give it a health boost.
        if(user.getPowerUpTimer() <= 0) {
            int type = user.getBoatType();
            float maxRobustness = GameData.boatsStats[type][0];
            user.setRobustness(user.getRobustness() * 1.5f);
            if(user.getRobustness() > maxRobustness) {
                //If the newly assigned robustness exceeds this Boat type's max robustness, cap it.
                user.setRobustness(maxRobustness);
            };
        }
    }
    
}
