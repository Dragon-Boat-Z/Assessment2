package com.hardgforgif.dragonboatracing.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.hardgforgif.dragonboatracing.core.*;
import org.mockito.Mockito;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.math.Vector2;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Ignore
    @Test
    public void testBoatConstructor(){
        assertEquals(robustness, testBoat.getRobustness());
        assertEquals(speed, testBoat.getSpeed());
        assertEquals(acceleration, testBoat.getAcceleration());
        assertEquals(maneuverability, testBoat.getManeuverability());
        assertEquals(0.25f * (maneuverability / 100), testBoat.getTurningSpeed());
        assertEquals(mockLane, testBoat.getLane());
    }

    @Ignore
    @Test
    public void testCreateBoatBody(){
        assertEquals(0.325f, testBoat.getBoatSprite().getScaleX());
        assertEquals(0.325f, testBoat.getBoatSprite().getScaleY());
        assertEquals(BodyDef.BodyType.DynamicBody, testBoat.getBoatBody().getType());
        assertEquals(new Vector2(100f, 120f), testBoat.getBoatBody().getPosition());
        assertEquals(testBoat, testBoat.getBoatBody().getUserData());
    }

    @Ignore
    @Test
    public void testGetLimitsAt(){
        Mockito.when(mockLane.getLeftIterator()).thenReturn(5);
        float[][] leftBoundaries = {{0,0},
                                {15,5},
                                {30,10},
                                {45,15},
                                {60,20}};
        Mockito.when(mockLane.getLeftBoundary()).thenReturn(leftBoundaries);

        Mockito.when(mockLane.getRightIterator()).thenReturn(5);
        float[][] rightBoundaries = {{10,0},
                                     {25,5},
                                     {40,10},
                                     {55,15},
                                     {70,20}};
        Mockito.when(mockLane.getRightBoundary()).thenReturn(rightBoundaries);

        float[] expected1 = {5f,0f};
        assertTrue(Arrays.equals(expected1, testBoat.getLimitsAt(15f)));
        float[] expected2 = {10f,5f};
        assertTrue(Arrays.equals(expected2, testBoat.getLimitsAt(30)));
    }

    @Ignore
    @Test
    public void testHasFinished(){
        //hasn't moved
        assertFalse(testBoat.hasFinished());

        //level with finish line
        testBoat.getBoatSprite().setPosition(100, 8712);
        assertFalse(testBoat.hasFinished());

        //1 past finish line
        testBoat.getBoatSprite().setPosition(100, 8713);
        assertTrue(testBoat.hasFinished());
    }

    @Ignore
    @Test
    public void testMoveBoatSpeedUp(){
        //stamina below 50
        //current speed low enough
        testBoat.setCurrentSpeed(10);
        testBoat.setStamina(40f);
        testBoat.moveBoat(1);
        float actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(10.04f, actualSpeed);
        //current speed at limit
        testBoat.setCurrentSpeed(72);
        testBoat.setStamina(40f);
        testBoat.moveBoat(1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(72f, actualSpeed);

        //stamina below 75
        //current speed low enough
        testBoat.setCurrentSpeed(50);
        testBoat.setStamina(60f);
        testBoat.moveBoat(1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(50.08f, actualSpeed);
        //current speed at limit
        testBoat.setCurrentSpeed(81);
        testBoat.setStamina(60f);
        testBoat.moveBoat(1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(81f, actualSpeed);

        //stamina above 75
        //current speed low enough
        testBoat.setCurrentSpeed(80);
        testBoat.setStamina(80);
        testBoat.moveBoat(1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(80.13f, actualSpeed);
        //current speed at limit
        testBoat.setCurrentSpeed(90);
        testBoat.setStamina(80);
        testBoat.moveBoat(1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(90f, actualSpeed);
    }

    @Ignore
    @Test
    public void testMoveBoatSpeedDown(){
        //stamina below 50
        testBoat.setCurrentSpeed(10);
        testBoat.setStamina(40f);
        testBoat.moveBoat(-1);
        float actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(9.96f, actualSpeed);
        //stamina below 75
        testBoat.setCurrentSpeed(50);
        testBoat.setStamina(60f);
        testBoat.moveBoat(-1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(49.92f, actualSpeed);

        //stamina above 75
        testBoat.setCurrentSpeed(80);
        testBoat.setStamina(80);
        testBoat.moveBoat(-1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(79.87f, actualSpeed);

        //current speed at 0
        testBoat.setCurrentSpeed(0);
        testBoat.setStamina(80);
        testBoat.moveBoat(-1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(0f, actualSpeed);
    }

    @Ignore
    @Test
    public void testRotateBoat(){
        //right
        testBoat.rotateBoat(90f);
        assertEquals(0.008227981f, testBoat.getBoatBody().getAngle());

        //left
        testBoat.rotateBoat(-90f);
        assertEquals(-0.008227981f, testBoat.getBoatBody().getAngle());
    }
}
