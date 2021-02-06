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

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    boolean[] turnRight = new boolean[]{false, true, false, false};
    boolean[] turnLeft = new boolean[]{false, false, false, true};
    boolean[] moveUp = new boolean[]{true, false, false, false};
    boolean[] moveDown = new boolean[]{false,false,true,false};
    boolean[] noMovement = new boolean[]{false,false,false,false};


    @Before
    public void init(){
        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        mockLane = Mockito.mock(Lane.class);

        testPlayer = new Player(robustness, speed, acceleration, maneuverability, boatType, mockLane);
        World world;
        world = new World(new Vector2(0f, 0f), true);
        testPlayer.createBoatBody(world, 100f, 120f, "Boat1.json");
    }

    @Test
    public void testPlayerConstructor(){
        assertEquals(robustness, testPlayer.getRobustness());
        assertEquals(speed, testPlayer.getSpeed());
        assertEquals(acceleration, testPlayer.getAcceleration());
        assertEquals(maneuverability, testPlayer.getManeuverability());
        assertEquals(0.25f * (maneuverability / 100), testPlayer.getTurningSpeed());
        assertEquals(mockLane, testPlayer.getLane());
    }

    @Test
    //Struggling to pass through the keys pressed to the instance method updatePlayer
    public void testPlayerUpdatePlayerTurning(){
        //TEST ROTATEBOAT
        //right
        testPlayer.updatePlayer(turnRight, 2f);
        assertEquals(rightTurningAngle, testPlayer.getTargetAngle());
        //left
        testPlayer.updatePlayer(turnLeft, 2f);
        assertEquals(leftTurningAngle, testPlayer.getTargetAngle());
        //nothing
        testPlayer.updatePlayer(noMovement,2f);
        assertEquals(0f, testPlayer.getTargetAngle());
    }

    @Test
    public void testPlayerUpdatePlayerMove(){
        float speedChange = 0.15f * ((acceleration * 0.8f)/90)  * (120f / 100);
        float expectedSpeed;
        float actualSpeed;

        //up
        testPlayer.updatePlayer(moveUp, 1f);
        expectedSpeed = (float) Math.round((20f + speedChange) * 10) / 10;
        actualSpeed = (float) Math.round(testPlayer.getCurrentSpeed() * 10) / 10;
        assertEquals(expectedSpeed, actualSpeed);
        assertEquals(2, 2);
        //down
        testPlayer.updatePlayer(moveDown, 1f);
        testPlayer.updatePlayer(moveDown, 1f);
        expectedSpeed = (float) Math.round((20f - speedChange) * 10) / 10;
        actualSpeed = (float) Math.round(testPlayer.getCurrentSpeed() * 10) / 10;
        assertEquals(expectedSpeed, actualSpeed);
        //nothing
        testPlayer.updatePlayer(noMovement,1f);
        actualSpeed = (float) Math.round(testPlayer.getCurrentSpeed() * 10) / 10;
        assertEquals(expectedSpeed, actualSpeed);
    }

    @Test
    public void testPlayerUpdatePlayerSpriteRotation(){
        float spriteRotation;

        //set boat to aim ahead and reset stamina
        testPlayer.rotateBoat(0f);
        testPlayer.getBoatSprite().setRotation(0f);
        testPlayer.setStamina(120f);

        //up
        testPlayer.updatePlayer(moveUp, 1f);
        spriteRotation = testPlayer.getBoatSprite().getRotation();
        assertEquals(0f, spriteRotation);
        
        //down
        testPlayer.updatePlayer(moveDown, 1f);
        spriteRotation = testPlayer.getBoatSprite().getRotation();
        assertEquals(0f, spriteRotation);

        //nothing
        testPlayer.updatePlayer(noMovement, 1f);
        spriteRotation = testPlayer.getBoatSprite().getRotation();
        assertEquals(0f, spriteRotation);

        //left
        testPlayer.setStamina(120f);
        testPlayer.updatePlayer(turnLeft, 1f);
        spriteRotation = testPlayer.getBoatSprite().getRotation();
        assertEquals(-0.4714285731315613, spriteRotation);

        //right
        testPlayer.rotateBoat(0f);
        testPlayer.getBoatSprite().setRotation(0f);
        testPlayer.setStamina(120f);
        testPlayer.updatePlayer(turnRight, 1f);
        spriteRotation = testPlayer.getBoatSprite().getRotation();
        assertEquals(0.4714285731315613, spriteRotation);
    }

    @Ignore
    @Test
    public void testPlayerUpdatePlayerStamina(){
        testPlayer.setStamina(120f); //reset stamina
        float initialStamina;
        float newStamina;
        //up
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(moveUp, 2f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina - 4, newStamina);

        //up - different delta
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(moveUp, 5f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina - 10, newStamina);

        //left
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(turnLeft, 3f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina - 6, newStamina);

        //right
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(turnRight, 4f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina - 8, newStamina);

        //down
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(moveDown, 6f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina - 6, newStamina);

        //nothing
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(noMovement, 8f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina - 12, newStamina);

        //stamina below 30
        testPlayer.setStamina(25f);
        initialStamina = testPlayer.getStamina();
        testPlayer.updatePlayer(moveUp, 2f);
        newStamina = testPlayer.getStamina();
        assertEquals(initialStamina, newStamina);
    }
}
