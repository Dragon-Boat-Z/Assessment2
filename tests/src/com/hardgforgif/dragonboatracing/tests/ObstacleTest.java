package com.hardgforgif.dragonboatracing.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.hardgforgif.dragonboatracing.core.*;
import org.mockito.Mockito;
import com.badlogic.gdx.physics.box2d.World;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.badlogic.gdx.graphics.Texture;

@RunWith(GdxTestRunner.class)
public class ObstacleTest {
    
    Obstacle testObstacle;
    World mockWorld;

    String textureName = "Obstacles/Obstacle1.png";

    @Before
    public void init(){
        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        //mockWorld = Mockito.mock(World.class);

        testObstacle = new Obstacle(textureName);
    }

    @Test
    public void testConstructor(){
        Texture texture = testObstacle.getObstaclTexture();
        assertTrue((texture instanceof Texture) && texture != null);
    }
}
