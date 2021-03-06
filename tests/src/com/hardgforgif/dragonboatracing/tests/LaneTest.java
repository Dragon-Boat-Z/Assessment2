package com.hardgforgif.dragonboatracing.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.core.Lane;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/** Tests the Lane class */
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
    public void init() {
        // Mock the opengl classes using mockito so that libgdx opengl functions can be
        // used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);
        tiledMap = new TmxMapLoader().load("Map1/Map2.tmx");
        leftLayer = tiledMap.getLayers().get("CollisionLayerLeft");
        rightLayer = tiledMap.getLayers().get("Lane1");
        testLane = new Lane(mapHeight, leftLayer, rightLayer, obstacleCount, laneNo);

        world = new World(new Vector2(0f, 0f), true);
    }

    @Test
    public void testLaneConstructor() {
        // compare left boundary array
        float[][] testLeftBoundary = new float[mapHeight][2];

        for (int i = 0; i < mapHeight; i++) {
            assertTrue(Arrays.equals(testLeftBoundary[i], testLane.getLeftBoundary()[i]), "Left boundary is incorrect");
        }

        // compare right boundary array
        float[][] testRightBoundary = new float[mapHeight][2];
        for (int i = 0; i < mapHeight; i++) {
            assertTrue(Arrays.equals(testRightBoundary[i], testLane.getRightBoundary()[i]),
                    "Right boundary is incorrect");
        }
        assertEquals(leftLayer, testLane.getLeftLayer(), "Left layer incorrect");
        assertEquals(rightLayer, testLane.getRightLayer(), "Right layer incorrect");
        assertEquals(obstacleCount, testLane.getObstacles().length, "Obstacle count incorrect");
        assertEquals(laneNo, testLane.getLaneNo(), "Lane number incorrect");
    }

    @Test
    public void testConstructBoundaries() {
        // left boundaries
        testLane.constructBoundaries(2f);
        float[][] testLeftBoundary = { { 2.7890625f, 257.2726f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f },
                { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f } };
        for (int i = 0; i < mapHeight; i++) {
            assertTrue(Arrays.equals(testLeftBoundary[i], testLane.getLeftBoundary()[i]), "Left boundaries incorrect");
        }

        // right boundaries
        testLane.constructBoundaries(2f);
        float[][] testRightBoundary = { { 4.3671875f, 1092.364f }, { 4.3671875f, 1092.364f }, { 0.0f, 0.0f },
                { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f }, { 0.0f, 0.0f },
                { 0.0f, 0.0f } };
        for (int i = 0; i < mapHeight; i++) {
            assertTrue(Arrays.equals(testRightBoundary[i], testLane.getRightBoundary()[i]),
                    "Right boundaries incorrect");
        }
    }

    @Test
    public void testGetLimitsAt() {
        testLane.constructBoundaries(2f);

        float[] actualList = testLane.getLimitsAt(4.5f);
        float[] expectedList = { 257.2726f, 1092.364f };
        assertTrue(Arrays.equals(expectedList, actualList), "Limits wrong at 4.5");
    }

    @Test
    // Can't test objects
    public void testSpawnObstacles() {
        testLane.spawnObstacles(world, mapHeight);
        for (int i = 0; i < obstacleCount; i++) {
            assertTrue(
                    testLane.getObstacles()[i].getObstacleTexture().toString()
                            .matches("Obstacles/Obstacle[123456789].png|PowerUps/.*.png"),
                    "Obstacle texture doesn't meet regex for Obstacle or power up");
        }

    }

}
