package com.hardgforgif.dragonboatracing.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.hardgforgif.dragonboatracing.core.*;
import org.mockito.Mockito;
import com.badlogic.gdx.maps.MapLayer;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(GdxTestRunner.class)
public class LaneTest {

    Lane testLane;

    int mapHeight = 10;
    int obstacleCount = 35;
    int laneNo = 1;
    TiledMap tiledMap;
    MapLayer leftLayer;
    MapLayer rightLayer;
    World world;
    @Before
    public void init(){
        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);
        tiledMap = new TmxMapLoader().load("Map1/Map2.tmx");
        leftLayer = tiledMap.getLayers().get("CollisionLayerLeft");
        rightLayer = tiledMap.getLayers().get("Lane1");
        testLane = new Lane(mapHeight, leftLayer, rightLayer, obstacleCount, laneNo);

        world = new World(new Vector2(0f, 0f), true);
    }

    @Ignore
    @Test
    public void testLaneConstructor(){
        //compare left boundary array
        float[][] testLeftBoundary = new float[mapHeight][2];

        for(int i = 0; i < mapHeight; i++){
            assertTrue(Arrays.equals(testLeftBoundary[i], testLane.getLeftBoundary()[i]));
        }

        //compare right boundary array
        float[][] testRightBoundary = new float[mapHeight][2];
        for(int i = 0; i < mapHeight; i++){
            assertTrue(Arrays.equals(testRightBoundary[i], testLane.getRightBoundary()[i]));
        }
        assertEquals(leftLayer, testLane.getLeftLayer());
        assertEquals(rightLayer, testLane.getRightLayer());
        assertEquals(obstacleCount, testLane.getObstacles().length);
    }

    @Ignore
    @Test
    public void testConstructBoundaries(){
        testLane.constructBoundaries(2f);
        float[][] testLeftBoundary = {{2.7890625f, 257.2726f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f}};
        for(int i = 0; i < mapHeight; i++) {
            assertTrue(Arrays.equals(testLeftBoundary[i], testLane.getLeftBoundary()[i]));
        }

        testLane.constructBoundaries(2f);
        float[][] testRightBoundary = {{4.3671875f, 1092.364f},
                                        {4.3671875f, 1092.364f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f},
                                        {0.0f, 0.0f}};
        for(int i = 0; i < mapHeight; i++) {
            assertTrue(Arrays.equals(testRightBoundary[i], testLane.getRightBoundary()[i]));
        }
    }

    @Ignore
    @Test
    public void testGetLimitsAt(){
        testLane.constructBoundaries(2f);

        float[] actualList = testLane.getLimitsAt(4.5f);
        float[] expectedList = {257.2726f, 1092.364f};
        assertTrue(Arrays.equals(expectedList, actualList));
    }

    @Ignore
    @Test
    //Can't test objects
    public void testSpawnObstacles(){
        testLane.spawnObstacles(world, mapHeight);
        for (int i = 0;i < obstacleCount; i++){
            assertTrue(testLane.getObstacles()[i].getObstacleTexture().toString().matches("Obstacles/Obstacle[123456].png"));
            }

    }

}
