package com.hardgforgif.dragonboatracing.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.hardgforgif.dragonboatracing.core.Boat;
import com.hardgforgif.dragonboatracing.core.Lane;
import com.hardgforgif.dragonboatracing.core.PowerUpSpeed;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(GdxTestRunner.class)
public class PowerUpSpeedTest {

    Lane mockLane;
    Boat testBoat;
    PowerUpSpeed testPowerUp;

    @Before
    public void init() {
        // Mock the opengl classes using mockito so that libgdx opengl functions can be
        // used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        mockLane = Mockito.mock(Lane.class);

        testBoat = new Boat(120, 110, 100, 80, 0, mockLane);
        testPowerUp = new PowerUpSpeed();
    }

    @Test
    public void testPowerUpSpeedConstructor() {
        Texture texture = testPowerUp.getObstacleTexture();
        assertEquals("PowerUps/SpeedBoost.png", texture.toString(), "Texture file incorrect");
        assertEquals(0, testPowerUp.getX(), "X position incorrect");
        assertEquals(0, testPowerUp.getY(), "Y position incorrect");
        assertTrue(testPowerUp.isPowerUp(), "Should be a powerup obstacle");
    }

    @Test
    public void testApplyPowerUpSpeed() {
        // boat at lower speed
        testBoat.setCurrentSpeed(50);
        testPowerUp.applyPowerUp(testBoat);
        assertEquals(75, testBoat.getCurrentSpeed(), "Speed increase incorrect (below limit)");

        // boat at top speed
        testBoat.setCurrentSpeed(110);
        testPowerUp.applyPowerUp(testBoat);
        assertEquals(165, testBoat.getCurrentSpeed(), "Speed increase incorrect (above limit)");
        assertEquals(2f, testBoat.getPowerUpTimer(), "Shorter powerup timer set incorrect");

        testBoat.setPowerUpTimer(8);
        testBoat.setCurrentSpeed(50);
        testPowerUp.applyPowerUp(testBoat);
        assertEquals(50, testBoat.getCurrentSpeed(), "Speed shouldn't be changed");
    }
}
