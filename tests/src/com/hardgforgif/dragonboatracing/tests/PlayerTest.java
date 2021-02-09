package com.hardgforgif.dragonboatracing.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.core.Lane;
import com.hardgforgif.dragonboatracing.core.Player;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(GdxTestRunner.class)
public class PlayerTest {

    Lane mockLane;
    Player testPlayer;

    float robustness = 120f;
    float speed = 90f;
    float acceleration = 100f;
    float maneuverability = 110f;
    int boatType = 3;
    float rightTurningAngle = 90f;
    float leftTurningAngle = -90f;
    boolean keysPressed[];
    boolean[] turnRight = new boolean[] { false, true, false, false };
    boolean[] turnLeft = new boolean[] { false, false, false, true };
    boolean[] moveUp = new boolean[] { true, false, false, false };
    boolean[] moveDown = new boolean[] { false, false, true, false };
    boolean[] noMovement = new boolean[] { false, false, false, false };

    @Before
    public void init() {
        // Mock the opengl classes using mockito so that libgdx opengl functions can be
        // used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        mockLane = Mockito.mock(Lane.class);

        testPlayer = new Player(robustness, speed, acceleration, maneuverability, boatType, mockLane);
        World world;
        world = new World(new Vector2(0f, 0f), true);
        testPlayer.createBoatBody(world, 100f, 120f, "Boat1.json");
    }

    @Test
    public void testPlayerConstructor() {
        assertEquals(robustness, testPlayer.getRobustness(), "Robustness incorrect");
        assertEquals(speed, testPlayer.getSpeed(), "Speed incorrect");
        assertEquals(acceleration, testPlayer.getAcceleration(), "Acceleration incorrect");
        assertEquals(maneuverability, testPlayer.getManeuverability(), "Maneuverability incorrect");
        assertEquals(0.25f * (maneuverability / 100), testPlayer.getTurningSpeed(), "Turning speed incorrect");
        assertEquals(mockLane, testPlayer.getLane(), "Lane incorrect");
    }

    @Test
    public void testPlayerUpdatePlayerTurning() {
        // right
        testPlayer.updatePlayer(turnRight, 2f);
        assertEquals(rightTurningAngle, testPlayer.getTargetAngle(), "Target angle incorrect (turning right)");
        // left
        testPlayer.updatePlayer(turnLeft, 2f);
        assertEquals(leftTurningAngle, testPlayer.getTargetAngle(), "Target angle incorrect (turning left)");
        // nothing
        testPlayer.updatePlayer(noMovement, 2f);
        assertEquals(0f, testPlayer.getTargetAngle(), "Shouldn't be turning");
    }

    @Test
    public void testPlayerUpdatePlayerMove() {
        float speedChange = 0.15f * ((acceleration * 0.8f) / 90) * (120f / 100);
        float expectedSpeed;
        float actualSpeed;

        // up
        testPlayer.updatePlayer(moveUp, 1f);
        expectedSpeed = (float) Math.round((20f + speedChange) * 10) / 10;
        actualSpeed = (float) Math.round(testPlayer.getCurrentSpeed() * 10) / 10;
        assertEquals(expectedSpeed, actualSpeed, "Speed increase incorrect");
        // down
        testPlayer.updatePlayer(moveDown, 1f);
        testPlayer.updatePlayer(moveDown, 1f);
        expectedSpeed = (float) Math.round((20f - speedChange) * 10) / 10;
        actualSpeed = (float) Math.round(testPlayer.getCurrentSpeed() * 10) / 10;
        assertEquals(expectedSpeed, actualSpeed, "Speed decrease incorrect");
        // nothing
        expectedSpeed = (float) Math.round(testPlayer.getCurrentSpeed() * 10) / 10;
        testPlayer.updatePlayer(noMovement, 1f);
        actualSpeed = (float) Math.round(testPlayer.getCurrentSpeed() * 10) / 10;
        assertEquals(expectedSpeed, actualSpeed, "Speed shouldn't have changed");
    }

    @Test
    public void testPlayerUpdatePlayerSpriteRotation() {
        float spriteRotation;

        // set boat to aim ahead and reset stamina
        testPlayer.rotateBoat(0f);
        testPlayer.getBoatSprite().setRotation(0f);
        testPlayer.setStamina(120f);

        // up
        testPlayer.updatePlayer(moveUp, 1f);
        spriteRotation = testPlayer.getBoatSprite().getRotation();
        assertEquals(0f, spriteRotation, "Sprite shouldn't be rotated (move up)");

        // down
        testPlayer.updatePlayer(moveDown, 1f);
        spriteRotation = testPlayer.getBoatSprite().getRotation();
        assertEquals(0f, spriteRotation, "Sprite shouldn't be rotated (move down)");

        // nothing
        testPlayer.updatePlayer(noMovement, 1f);
        spriteRotation = testPlayer.getBoatSprite().getRotation();
        assertEquals(0f, spriteRotation, "Sprite shouldn't be rotated (no movement)");

        // left
        testPlayer.setStamina(120f);
        testPlayer.updatePlayer(turnLeft, 1f);
        spriteRotation = testPlayer.getBoatSprite().getRotation();
        assertEquals(-0.4714285731315613, spriteRotation, "Left rotation incorrect");

        // right
        testPlayer.rotateBoat(0f);
        testPlayer.getBoatSprite().setRotation(0f);
        testPlayer.setStamina(120f);
        testPlayer.updatePlayer(turnRight, 1f);
        spriteRotation = testPlayer.getBoatSprite().getRotation();
        assertEquals(0.4714285731315613, spriteRotation, "Right rotation incorrect");
    }

    /**
     * Tests the Player class
     */
    @Test
    public void testPlayerUpdatePlayerStamina() {
        // testPlayer.setStamina(120f); //reset stamina
        float initialStamina;
        float newStamina;
        // up
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(moveUp, 2f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina - 8, newStamina, "Stamina decrease incorrect");

        // up - different delta
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(moveUp, 5f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina - 20, newStamina, "Stamina decrease incorrect (different delta)");

        // left
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(turnLeft, 3f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina, newStamina, "Stamina should be unchanged (turn left)");

        // right
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(turnRight, 4f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina, newStamina, "Stamina should be unchanged (turn right)");

        // down
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(moveDown, 6f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina + 18, newStamina, "Stamina increase incorrect (slow down)");

        // nothing
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(noMovement, 8f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina + 16, newStamina, "Stamina increase incorrect (no movement)");

        // stamina at zero
        testPlayer.setStamina(0f);
        testPlayer.updatePlayer(moveUp, 2f);
        newStamina = testPlayer.getStamina();
        assertEquals(0, newStamina, "Stamina shouldn't change at zero");
    }
}
