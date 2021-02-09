package com.hardgforgif.dragonboatracing.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.core.Boat;
import com.hardgforgif.dragonboatracing.core.Lane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/** Tests the Boat class */
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
    public void init() {
        // Mock the opengl classes using mockito so that libgdx opengl functions can be
        // used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        mockLane = Mockito.mock(Lane.class);
        World world;
        world = new World(new Vector2(0f, 0f), true);

        testBoat = new Boat(robustness, speed, acceleration, maneuverability, boatType, mockLane);
        testBoat.createBoatBody(world, 100f, 120f, "Boat1.json");
    }

    @Test
    public void testBoatConstructor() {
        assertEquals(robustness, testBoat.getRobustness(), "Robustness incorrect");
        assertEquals(speed, testBoat.getSpeed(), "Speed incorrect");
        assertEquals(acceleration, testBoat.getAcceleration(), "Acceleration incorrect");
        assertEquals(maneuverability, testBoat.getManeuverability(), "Maneuverability incorrect");
        assertEquals(0.25f * (maneuverability / 100), testBoat.getTurningSpeed(), "Turning speed incorrect");
        assertEquals(mockLane, testBoat.getLane(), "Lane incorrect");
    }

    @Test
    public void testCreateBoatBody() {
        assertEquals(0.325f, testBoat.getBoatSprite().getScaleX(), "Boat sprite x scale wrong");
        assertEquals(0.325f, testBoat.getBoatSprite().getScaleY(), "Boat sprite y scale wrong");
        assertEquals(BodyDef.BodyType.DynamicBody, testBoat.getBoatBody().getType(), "Boat body type not Dynamic Body");
        assertEquals(new Vector2(100f, 120f), testBoat.getBoatBody().getPosition(), "Boat body position incorrect");
        assertEquals(testBoat, testBoat.getBoatBody().getUserData(), "Boat body user data incorrect");
    }

    @Test
    public void testGetLimitsAt() {
        // mock lane
        // left iterator and boundaries
        Mockito.when(mockLane.getLeftIterator()).thenReturn(5);
        float[][] leftBoundaries = { { 0, 0 }, { 15, 5 }, { 30, 10 }, { 45, 15 }, { 60, 20 } };
        Mockito.when(mockLane.getLeftBoundary()).thenReturn(leftBoundaries);

        // right iterator and boundaries
        Mockito.when(mockLane.getRightIterator()).thenReturn(5);
        float[][] rightBoundaries = { { 10, 0 }, { 25, 5 }, { 40, 10 }, { 55, 15 }, { 70, 20 } };
        Mockito.when(mockLane.getRightBoundary()).thenReturn(rightBoundaries);

        float[] expected1 = { 5f, 0f };
        assertTrue(Arrays.equals(expected1, testBoat.getLimitsAt(15f)), "Limits wrong at 15");
        float[] expected2 = { 10f, 5f };
        assertTrue(Arrays.equals(expected2, testBoat.getLimitsAt(30f)), "Limits wrong at 30");
    }

    @Test
    public void testHasFinished() {
        // hasn't moved
        assertFalse(testBoat.hasFinished(), "Shouldn't be finished");

        // level with finish line
        testBoat.getBoatSprite().setPosition(100, 8712);
        assertFalse(testBoat.hasFinished(), "Should be on finish line and not finished");

        // 1 past finish line
        testBoat.getBoatSprite().setPosition(100, 8713);
        assertTrue(testBoat.hasFinished(), "Should be just passed finish line");
    }

    @Test
    public void testMoveBoatSpeedUp() {
        // stamina below 50
        // current speed low enough
        testBoat.setCurrentSpeed(10);
        testBoat.setStamina(40f);
        testBoat.moveBoat(1);
        float actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(10.04f, actualSpeed, "Speed increased incorrectly (stamina below 50)");
        // current speed at limit
        testBoat.setCurrentSpeed(72);
        testBoat.setStamina(40f);
        testBoat.moveBoat(1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(72f, actualSpeed, "Speed didn't stay at limit (stamina below 50)");

        // stamina below 75
        // current speed low enough
        testBoat.setCurrentSpeed(50);
        testBoat.setStamina(60f);
        testBoat.moveBoat(1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(50.08f, actualSpeed, "Speed increased incorrectly (stamina below 75, above 50");
        // current speed at limit
        testBoat.setCurrentSpeed(81);
        testBoat.setStamina(60f);
        testBoat.moveBoat(1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(81f, actualSpeed, "Speed didn't stay at limit (stamina below 75, above 50");

        // stamina above 75
        // current speed low enough
        testBoat.setCurrentSpeed(80);
        testBoat.setStamina(80);
        testBoat.moveBoat(1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(80.13f, actualSpeed, "Speed increased incorrectly (stamina above 75");
        // current speed at limit
        testBoat.setCurrentSpeed(90);
        testBoat.setStamina(80);
        testBoat.moveBoat(1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(90f, actualSpeed, "Speed didn't stay at limit (stamina above 75");
    }

    @Test
    public void testMoveBoatSpeedDown() {
        // stamina below 50
        testBoat.setCurrentSpeed(10);
        testBoat.setStamina(40f);
        testBoat.moveBoat(-1);
        float actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(9.96f, actualSpeed, "Speed decreased incorrectly (stamina below 50)");
        // stamina below 75
        testBoat.setCurrentSpeed(50);
        testBoat.setStamina(60f);
        testBoat.moveBoat(-1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(49.92f, actualSpeed, "Speed decreased incorrectly (stamina below 75, above 50");

        // stamina above 75
        testBoat.setCurrentSpeed(80);
        testBoat.setStamina(80);
        testBoat.moveBoat(-1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(79.87f, actualSpeed, "Speed decreased incorrectly (stamina above 75");

        // current speed at 0
        testBoat.setCurrentSpeed(0);
        testBoat.setStamina(80);
        testBoat.moveBoat(-1);
        actualSpeed = (float) Math.round(testBoat.getCurrentSpeed() * 100) / 100;
        assertEquals(0f, actualSpeed, "Speed didn't stay at minimum");
    }

    @Test
    public void testRotateBoat() {
        // right
        testBoat.rotateBoat(90f);
        assertEquals(0.008227981f, testBoat.getBoatBody().getAngle(), "Right rotate angle incorrect");

        // left
        testBoat.rotateBoat(-90f);
        assertEquals(-0.008227981f, testBoat.getBoatBody().getAngle(), "Left rotate angle incorrect");
    }
}
