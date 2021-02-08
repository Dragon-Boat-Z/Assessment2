package com.hardgforgif.dragonboatracing.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.hardgforgif.dragonboatracing.core.*;
import com.badlogic.gdx.physics.box2d.*;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(GdxTestRunner.class)
public class AITest {
    
    Lane mockLane;
    Obstacle mockObstacle;
    Sprite mockObstacleSprite;
    AI testAI;

    float robustness = 120f;
    float stamina = 90f;
    float handling = 100f;
    float speed = 110f;
    int boatType = 3;

    @Before
    public void init(){
        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        mockLane = Mockito.mock(Lane.class);
        mockObstacle = Mockito.mock(Obstacle.class);
        mockObstacleSprite = Mockito.mock(Sprite.class);

        setupMockObstacleSprite();
        setupMockLane();

        World world;
        world = new World(new Vector2(0f, 0f), true);

        testAI = new AI(robustness, stamina, handling, speed, boatType, mockLane);
        testAI.createBoatBody(world, 100f, 120f, "Boat1.json");
    }

    @Test
    public void testAIConstructor(){
        assertEquals(110.4f, Math.round(testAI.getRobustness() * 1000f) / 1000f);
        assertEquals(82.8f, Math.round(testAI.getSpeed() * 1000f) / 1000f);
        assertEquals(92f, Math.round(testAI.getAcceleration() * 1000f) / 1000f);
        assertEquals(101.2f, Math.round(testAI.getManeuverability() * 1000f) / 1000f);
        assertEquals(0.275f, testAI.getTurningSpeed());
        assertEquals(mockLane, testAI.getLane());
    }

    @Test
    public void testUpdateAI(){
        testAI.updateAI(2f);
        assertEquals(new Vector2(10000f,12493.6f), testAI.getLaneChecker());
        assertEquals(new Vector2(10000f,12393.6f), testAI.getObjectChecker());
        assertFalse(testAI.getIsDodging()); 
    }

    @Test
    public void testUpdateAIMovement(){
        // test when stamina is above 50
        testAI.setStamina(51);
        testAI.updateAI(2f);
        assertTrue(testAI.getIsAccelerating());
        assertFalse(testAI.getIsBraking());
        
        // test when stamina is above 30
        testAI.setStamina(31);
        testAI.updateAI(2f);
        assertFalse(testAI.getIsAccelerating());
        assertFalse(testAI.getIsBraking());
        
        // test when stamina is below 30
        testAI.setStamina(29);
        testAI.updateAI(2f);
        assertFalse(testAI.getIsAccelerating());
        assertTrue(testAI.getIsBraking());
    }

    private void setupMockLane(){
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

        setupMockObstacle();
        Obstacle[] obstacles = {mockObstacle};
        Mockito.when(mockLane.getObstacles()).thenReturn(obstacles);
    }

    private void setupMockObstacle(){
        Mockito.doReturn(mockObstacleSprite).when(mockObstacle).getObstacleSprite();
    }

    public void setupMockObstacleSprite(){
        Mockito.when(mockObstacleSprite.getWidth()).thenReturn(20f);
        Mockito.when(mockObstacleSprite.getHeight()).thenReturn(20f);
        Mockito.when(mockObstacleSprite.getX()).thenReturn(45f);
        Mockito.when(mockObstacleSprite.getY()).thenReturn(45f);
    }
}
