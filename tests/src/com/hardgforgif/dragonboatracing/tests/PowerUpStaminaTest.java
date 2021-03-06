package com.hardgforgif.dragonboatracing.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.hardgforgif.dragonboatracing.core.Boat;
import com.hardgforgif.dragonboatracing.core.Lane;
import com.hardgforgif.dragonboatracing.core.PowerUpStamina;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(GdxTestRunner.class)
public class PowerUpStaminaTest {

    Lane mockLane;
    Boat testBoat;
    PowerUpStamina testPowerUp;

    @Before
    public void init() {
        // Mock the opengl classes using mockito so that libgdx opengl functions can be
        // used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        mockLane = Mockito.mock(Lane.class);

        testBoat = new Boat(120, 110, 100, 80, 0, mockLane);
        testPowerUp = new PowerUpStamina();
    }

    @Test
    public void testPowerUpSpeedConstructor() {
        Texture texture = testPowerUp.getObstacleTexture();
        assertEquals("PowerUps/StaminaBoost.png", texture.toString(), "Texture file incorrect");
        assertEquals(0, testPowerUp.getX(), "X position incorrect");
        assertEquals(0, testPowerUp.getY(), "Y position incorrect");
        assertTrue(testPowerUp.isPowerUp(), "Should be a powerup obstacle");
    }

    @Test
    public void testApplyPowerUpStamina() {
        // timer at zero
        testBoat.setStamina(40);
        testPowerUp.applyPowerUp(testBoat);
        assertEquals(60, testBoat.getStamina(), "Stamina increased incorrectly");

        // powerup currently applied
        testBoat.setStamina(40);
        testBoat.setPowerUpTimer(4);
        testPowerUp.applyPowerUp(testBoat);
        assertEquals(40, testBoat.getStamina(), "Stamina shouldn't have changed (powerup shouldn't apply)");
    }
}
