package com.hardgforgif.dragonboatracing.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.hardgforgif.dragonboatracing.core.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(GdxTestRunner.class)
public class AITest {
    
    Lane mockLane;
    Boat testAI;

    float robustness = 120f;
    float stamina = 90f;
    float handling = 100f;
    float speed = 110f;
    int boatType = 3;

    @Before
    public void init(){
        mockLane = Mockito.mock(Lane.class);

        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        testAI = new AI(robustness, stamina, handling, speed, boatType, mockLane);
    }

    @Test
    public void testConstructor(){
        assertEquals(110.4f, Math.round(testAI.getRobustness() * 1000f) / 1000f);
        assertEquals(82.8f, Math.round(testAI.getSpeed() * 1000f) / 1000f);
        assertEquals(92f, Math.round(testAI.getAcceleration() * 1000f) / 1000f);
        assertEquals(101.2f, Math.round(testAI.getManeuverability() * 1000f) / 1000f);
        assertEquals(0.275f, testAI.getTurningSpeed());
        assertEquals(mockLane, testAI.getLane());
    }
}
