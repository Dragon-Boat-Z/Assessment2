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
/** Issue with tiledMapRenderer
@RunWith(GdxTestRunner.class)
public class MapTest {
    
    Map testMap;

    float width = 1920;

    @Before
    public void init(){
        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        testMap = new Map("Map1/Map2.tmx", width);
    }

    @Test
    public void testMapConstructor(){
        assertEquals(width, testMap.getScreenWidth());
        assertEquals(100, testMap.getMapWidth());
        assertEquals(1440, testMap.getMapHeight());
        assertEquals(width / 100 / 32f, testMap.getUnitScale());
    }
}
 */