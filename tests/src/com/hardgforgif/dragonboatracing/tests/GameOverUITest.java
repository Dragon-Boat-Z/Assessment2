package com.hardgforgif.dragonboatracing.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.hardgforgif.dragonboatracing.UI.*;
import com.hardgforgif.dragonboatracing.GameData;
import org.mockito.Mockito;
import com.badlogic.gdx.math.Vector2; 

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(GdxTestRunner.class)
public class GameOverUITest {
    
    GameOverUI testGameOverUI;

    @Before
    public void init(){
        //Mock the opengl classes using mockito so that libgdx opengl functions can be used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        testGameOverUI = new GameOverUI();
    }

    @Test
    public void testGetInput(){
        Vector2 mousePosNotZero = new Vector2(1f,1f);
        testGameOverUI.getInput(1920, mousePosNotZero);
        assertTrue(GameData.resetGameState);
        assertFalse(GameData.GameOverState);
    }
}
