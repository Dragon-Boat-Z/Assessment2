package com.hardgforgif.dragonboatracing.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import org.junit.Before;
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
    MapLayer mockLeftLayer;
    MapLayer mockRightLayer;

    int mapHeight = 1200;
    int obstacleCount = 35;

    @Before
    public void init(){
        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        mockLeftLayer = Mockito.mock(MapLayer.class);
        mockRightLayer = Mockito.mock(MapLayer.class);

        testLane = new Lane(mapHeight, mockLeftLayer, mockRightLayer, obstacleCount);
    }

    @Test
    public void testLaneConstructor(){
        //compare left boundary array
        float[][] testLeftBoundary = new float[1200][2];
        for(int i = 0; i < mapHeight; i++){
            for(int j = 0; j < testLeftBoundary[i].length; j++){
                assertEquals(testLeftBoundary[i][j], testLane.getLeftBoundary()[i][j]);
            }
        }

        //compare right boundary array
        float[][] testRightBoundary = new float[1200][2];
        for(int i = 0; i < mapHeight; i++){
            for(int j = 0; j < testRightBoundary[i].length; j++){
                assertEquals(testRightBoundary[i][j], testLane.getRightBoundary()[i][j]);
            }
        }

        assertEquals(mockLeftLayer, testLane.getLeftLayer());
        assertEquals(mockRightLayer, testLane.getRightLayer());
        assertEquals(obstacleCount, testLane.getObstacles().length);
    }
}
