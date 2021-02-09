package com.hardgforgif.dragonboatracing.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.hardgforgif.dragonboatracing.core.Boat;
import com.hardgforgif.dragonboatracing.core.Lane;
import com.hardgforgif.dragonboatracing.core.PowerUpHealth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * Tests the PowerUpHealth class
 */
@RunWith(GdxTestRunner.class)
public class PowerUpHealthTest {

    Lane mockLane;
    Boat testBoat;
    PowerUpHealth testPowerUp;

    @Before
    public void init() {
        // Mock the opengl classes using mockito so that libgdx opengl functions can be
        // used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        mockLane = Mockito.mock(Lane.class);

        testBoat = new Boat(120, 110, 100, 80, 0, mockLane);
        testPowerUp = new PowerUpHealth();
    }

    @Test
    public void testPowerUpHealthConstructor() {
        Texture texture = testPowerUp.getObstacleTexture();
        assertEquals("PowerUps/HealthBoost.png", texture.toString(), "Texture file incorrect");
        assertEquals(0, testPowerUp.getX(), "X position incorrect");
        assertEquals(0, testPowerUp.getY(), "Y position incorrect");
        assertTrue(testPowerUp.isPowerUp(), "Should be a powerup obstacle");
    }

    @Test
    public void testApplyPowerUpHealth() {
        // test for when boat has lost health
        // set testBoat robustness to half
        testBoat.setRobustness(60);
        // apply power up to testBoat
        testPowerUp.applyPowerUp(testBoat);
        assertEquals(90, testBoat.getRobustness(), "Robustness increase incorrect");

        // test for when testBoat already has max robustness
        // set testBoat robustness to max robustness
        testBoat.setRobustness(120);
        // apply power up to testBoat
        testPowerUp.applyPowerUp(testBoat);
        assertEquals(120, testBoat.getRobustness(), "Robustness at max, shouldn't change");
    }
}
