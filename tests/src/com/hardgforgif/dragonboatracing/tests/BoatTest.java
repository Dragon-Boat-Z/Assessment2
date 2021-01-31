package com.hardgforgif.dragonboatracing.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.hardgforgif.dragonboatracing.core.*;
import org.mockito.Mockito;
//import com.badlogic.gdx.physics.box2d.World;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(GdxTestRunner.class)
public class BoatTest {

    Lane mockLane;
    World mockWorld;
    Boat testBoat;

    @Before
    public void init(){
        mockLane = Mockito.mock(Lane.class);
        //mockWorld = Mockito.mock(World.class);

        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        testBoat = new Boat(120f, 90f, 100f, 110f, 3, mockLane);
    }

    @Test
    public void testConstructor(){
        assertEquals(120f, testBoat.getRobustness());
        assertEquals(90f, testBoat.getSpeed());
        assertEquals(100f, testBoat.getAcceleration());
        assertEquals(110f, testBoat.getManeuverability());
        assertEquals(0.275f, testBoat.getTurningSpeed());
        assertEquals(mockLane, testBoat.getLane());
    }

    /**
    @Test
    public void testCreateBoatBody(){
        testBoat.createBoatBody(mockWorld, 100f, 120f, )
    }
     */
    /**
    @Test
    public void testUpdateLimits(){
        testBoat.updateLimits();

    }
     */
}
