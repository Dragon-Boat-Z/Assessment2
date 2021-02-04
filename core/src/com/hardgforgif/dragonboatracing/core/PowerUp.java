package com.hardgforgif.dragonboatracing.core;

public class PowerUp extends Obstacle {

    public PowerUp(String textureName) {
        super(textureName);
    }

    public void applyPowerUp(Boat user) {
        //Upon collision, the effet of the power up is applied to the boat that used it.
    }

    public boolean isPowerUp() {
        return true;
    }

}

