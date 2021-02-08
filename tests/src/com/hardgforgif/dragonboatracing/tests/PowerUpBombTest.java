package com.hardgforgif.dragonboatracing.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.hardgforgif.dragonboatracing.core.Boat;
import com.hardgforgif.dragonboatracing.core.Lane;
import com.hardgforgif.dragonboatracing.core.PowerUpBomb;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;


@RunWith(GdxTestRunner.class)
public class PowerUpBombTest {
    Lane mockLane;
    Boat testBoat;
    PowerUpBomb testPowerUp;

    @Before
    public void init() {
        // Mock the opengl classes using mockito so that libgdx opengl functions can be
        // used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        mockLane = Mockito.mock(Lane.class);

        testBoat = new Boat(120, 110, 100, 80, 0, mockLane);
        testPowerUp = new PowerUpBomb();
    }
    
    @Test
    public void testPowerUpBombConstructor(){
        Texture texture = testPowerUp.getObstacleTexture();
        assertEquals("PowerUps/ObstacleClearer.png", texture.toString());
        assertEquals(0, testPowerUp.getX());
        assertEquals(0, testPowerUp.getY());
        assertTrue(testPowerUp.isPowerUp());
    }

    @Test
    public void testApplyPowerUpBomb(){
        // do some testing
    }
}
