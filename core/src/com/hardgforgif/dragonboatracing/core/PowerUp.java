package com.hardgforgif.dragonboatracing.core;

public class PowerUp extends Obstacle {

    public PowerUp(String textureName) {
        super(textureName);
    }

    /**
     * Applies the effect of the power-up child instance to the Boat passed in.
     * @param user Boat instance for the power-up to be applied to.
     */
    public void applyPowerUp(Boat user) {
        //Upon collision, the effect of the power up is applied to the boat that used it.
    }

    public boolean isPowerUp() {
        return true;
    }

}

