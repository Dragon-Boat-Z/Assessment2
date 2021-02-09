package com.hardgforgif.dragonboatracing.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.hardgforgif.dragonboatracing.core.Obstacle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * Tests the Obstacle class
 */
@RunWith(GdxTestRunner.class)
public class ObstacleTest {

    Obstacle testObstacle;
    Obstacle testObstacle2;
    World world;

    String textureName = "Obstacles/Obstacle1.png";

    @Before
    public void init() {
        // Mock the opengl classes using mockito so that libgdx opengl functions can be
        // used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        world = new World(new Vector2(0f, 0f), true);

        // Two constructor methods for the class
        testObstacle = new Obstacle(textureName);
        testObstacle2 = new Obstacle(textureName, 120, 40);
    }

    @Test
    public void testObstacleConstructor() {
        Texture texture = testObstacle.getObstacleTexture();
        assertEquals("Obstacles/Obstacle1.png", texture.toString(), "Texture file is incorrect");
        assertEquals(0, testObstacle.getX(), "X position incorrect");
        assertEquals(0, testObstacle.getY(), "Y position incorrect");
        assertFalse(testObstacle.isPowerUp(), "Shouldn't be a powerup");
    }

    @Test
    public void testObstacleConstructorSetPositions() {
        Texture texture = testObstacle.getObstacleTexture();
        assertEquals("Obstacles/Obstacle1.png", texture.toString(), "Texture file is incorrect");
        assertEquals(120, testObstacle2.getX(), "X position incorrect");
        assertEquals(40, testObstacle2.getY(), "Y position incorrect");
        assertFalse(testObstacle.isPowerUp(), "Shouldn't be a powerup");
    }

    @Test
    public void testCreateObstacleBody() {
        testObstacle.createObstacleBody(world, 100f, 120f, "Obstacles/Obstacle1.json", -0.8f);
        assertEquals(0.19999999f, testObstacle.getObstacleSprite().getScaleX(), "Body x scale incorrect");
        assertEquals(0.19999999f, testObstacle.getObstacleSprite().getScaleY(), "Bodyn y scale incorrect");
        assertEquals(new Vector2(100f, 120f), testObstacle.getObstacleBody().getPosition(), "Body position incorrect");
        assertEquals(9980f, testObstacle.getObstacleSprite().getX(), "Sprite x position incorrect");
        assertEquals(11948.5f, testObstacle.getObstacleSprite().getY(), "Sprite y position incorrect");
    }
}
