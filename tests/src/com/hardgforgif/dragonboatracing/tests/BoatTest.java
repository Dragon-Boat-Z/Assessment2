package com.hardgforgif.dragonboatracing.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.hardgforgif.dragonboatracing.core.*;
import org.mockito.Mockito;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(GdxTestRunner.class)
public class BoatTest {

    Lane mockLane;
    World mockWorld;
    Boat testBoat;

    float robustness = 120f;
    float speed = 90f;
    float acceleration = 100f;
    float maneuverability = 110f;
    int boatType = 3;

    @Before
    public void init(){
        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        mockLane = Mockito.mock(Lane.class);
        World world;
        world = new World(new Vector2(0f, 0f), true);

        testBoat = new Boat(robustness, speed, acceleration, maneuverability, boatType, mockLane);
        testBoat.createBoatBody(world, 100f, 120f, "Boat1.json");
    }

    @Test
    public void testBoatConstructor(){
        assertEquals(robustness, testBoat.getRobustness());
        assertEquals(speed, testBoat.getSpeed());
        assertEquals(acceleration, testBoat.getAcceleration());
        assertEquals(maneuverability, testBoat.getManeuverability());
        assertEquals(0.25f * (maneuverability / 100), testBoat.getTurningSpeed());
        assertEquals(mockLane, testBoat.getLane());
    }

    @Test
    public void testCreateBoatBody(){
        assertEquals(0.19999999f, testBoat.getBoatSprite().getScaleX());
        assertEquals(0.19999999f, testBoat.getBoatSprite().getScaleY());
        assertEquals(BodyDef.BodyType.DynamicBody, testBoat.getBoatBody().getType());
        assertEquals(new Vector2(100f, 120f), testBoat.getBoatBody().getPosition());
        assertEquals(testBoat, testBoat.getBoatBody().getUserData());
    }

    @Test
    public void testDrawBoat(){
        
    }
}
