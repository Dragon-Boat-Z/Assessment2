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
import com.badlogic.gdx.math.Vector2;

@RunWith(GdxTestRunner.class)
public class ObstacleTest {
    
    Obstacle testObstacle;
    World world;

    String textureName = "Obstacles/Obstacle1.png";

    @Before
    public void init(){
        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        
        world = new World(new Vector2(0f, 0f), true);

        testObstacle = new Obstacle(textureName);
        testObstacle.createObstacleBody(world, 100f, 120f, "Obstacles/Obstacle1.json", -0.8f);
    }

    @Test
    public void testObstacleConstructor(){
        Texture texture = testObstacle.getObstaclTexture();
        assertEquals("Obstacles/Obstacle1.png", texture.toString());
    }

    @Test
    public void testCreateObstacleBody(){
        assertEquals(0.19999999f, testObstacle.getObstacleSprite().getScaleX());
        assertEquals(0.19999999f, testObstacle.getObstacleSprite().getScaleY());
        assertEquals(new Vector2(100f, 120f), testObstacle.getObstacleBody().getPosition());
        assertEquals(9866f, testObstacle.getObstacleSprite().getX());
        assertEquals(11892, testObstacle.getObstacleSprite().getY());
    }

    @Test
    public void createObstacleBody(){

        //assertTrue();

    }
}
