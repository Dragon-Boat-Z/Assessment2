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
    boolean[] turnRight = new boolean[]{true, true, false, false};
    boolean[] turnLeft = new boolean[]{false, false, true, true};
    boolean[] moveUp = new boolean[]{true, false, false, false};
    boolean[] moveDown = new boolean[]{false,false,false,true};
    boolean[] noMovement = new boolean[]{false,false,false,false};


    @Before
    public void init(){
        mockLane = Mockito.mock(Lane.class);

        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        testPlayer = new Player(robustness, speed, acceleration, maneuverability, boatType, mockLane);
        World world;
        world = new World(new Vector2(0f, 0f), true);
        //world.setContactListener(new ContactListener());
        //testPlayer.createBoatBody(world, 100f, 120f, "Boat1.json");
    }

    @Test
    public void testConstructor(){
        assertEquals(robustness, testPlayer.getRobustness());
        assertEquals(speed, testPlayer.getSpeed());
        assertEquals(acceleration, testPlayer.getAcceleration());
        assertEquals(maneuverability, testPlayer.getManeuverability());
        assertEquals(0.25f * (maneuverability / 100), testPlayer.getTurningSpeed());
        assertEquals(mockLane, testPlayer.getLane());
    }

    // @Test
    //Struggling to pass through the keys pressed to the instance method updatePlayer
    // public void testTargetingAngle(){
        // testPlayer.updatePlayer(turnRight, 2f);
        // assertEquals(rightTurningAngle, testPlayer.getTargetAngle());
        // testPlayer.updatePlayer(turnLeft, 2f);
        // assertEquals(leftTurningAngle, testPlayer.getTargetAngle());
        // testPlayer.updatePlayer(noMovement,2f);
        // assertEquals(0f, testPlayer.getTargetAngle());
    // }

    // @Test
    // public void testVerticalBoatMovement(){
        //testPlayer.updatePlayer(moveUp, 1f);
        //assertEquals();
        //testPlayer.updatePlayer(moveDown, 1f);
    // }
}
