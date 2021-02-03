package com.hardgforgif.dragonboatracing.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.hardgforgif.dragonboatracing.UI.*;
import org.mockito.Mockito;
import com.badlogic.gdx.math.Vector2; 
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(GdxTestRunner.class)
public class ChoosingUITest {
    
    Batch mockBatch;
    ChoosingUI testChoosingUI; 
    float[] boat1 = new float[] {120, 110, 100, 80};

    @Before
    public void init(){
        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        testChoosingUI = new ChoosingUI();
        mockBatch = Mockito.mock(Batch.class);
    }

    // @Test
    // public void testDrawUI(){
    //     Vector2 mousePos = new Vector2(151, -199);
        
    //     testChoosingUI.drawUI(mockBatch, mousePos, 1920, 1);
    //     assertEquals(boat1, testChoosingUI.getCurrentStats());
    //     assertTrue(java.util.Arrays.equals(boat1, testChoosingUI.getCurrentStats()));
    // }
}
