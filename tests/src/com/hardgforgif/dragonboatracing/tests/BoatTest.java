package com.hardgforgif.dragonboatracing.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.hardgforgif.dragonboatracing.core.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(GdxTestRunner.class)
public class BoatTest {

    Lane mockLane;

    @Before
    public void init(){
        mockLane = Mockito.mock(Lane.class);

        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);
    }

    @Test
    public void testConstructor(){
        Boat testBoat = new Boat(120f, 90f, 100f, 110f, 3, mockLane);
        assertEquals(120f, 1, "Hello world");
    }
}
