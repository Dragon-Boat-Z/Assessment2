package com.hardgforgif.dragonboatracing.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.hardgforgif.dragonboatracing.UI.ScrollingBackground;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

@RunWith(GdxTestRunner.class)
public class ScrollingBackgroundTest {

    Batch mockBatch;
    ScrollingBackground testScrollingBackground;

    @Before
    public void init() {

        // Mock the opengl classes using mockito so that libgdx opengl functions can be
        // used
        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl30 = Mockito.mock(GL30.class);

        testScrollingBackground = new ScrollingBackground();
        mockBatch = Mockito.mock(Batch.class);
    }

    @Test
    public void testScrollingBackgroundConstructor() {
        assertEquals(0, testScrollingBackground.getY1());
        assertEquals(1024, testScrollingBackground.getY2());
        assertEquals(0, testScrollingBackground.getSpeed());
        assertEquals(80, testScrollingBackground.getTargetSpeed());
        assertEquals(0, testScrollingBackground.getImageScale());
        assertEquals(true, testScrollingBackground.getSpeedFixed());
    }

    @Test
    public void testScrollingBackgroundUpdateAndRender() {
        // speed < targetSpeed, speedFixed = true;
        setup(0, true);
        assertEquals(864, testScrollingBackground.getY1());
        assertEquals(864, testScrollingBackground.getY2());

        // speed > targetSpeed, speedFixed = true;
        setup(81, true);
        assertEquals(864, testScrollingBackground.getY1());
        assertEquals(864, testScrollingBackground.getY2());

        // speed < targetSpeed, speedFixed = false;
        setup(0, false);
        assertEquals(664, testScrollingBackground.getY1());
        assertEquals(664, testScrollingBackground.getY2());

        // speed > targetSpeed, speedFixed = false;
        setup(81, false);
        assertEquals(664, testScrollingBackground.getY1());
        assertEquals(664, testScrollingBackground.getY2());
    }

    @Test
    public void testResize() {
        testScrollingBackground.resize(2048, 200);
        assertEquals(2, testScrollingBackground.getImageScale());
    }

    /**
     * Setup method for testing UpdateAndRender
     * 
     * @param speed
     * @param speedFixed
     */
    void setup(int speed, boolean speedFixed) {
        testScrollingBackground.setSpeed(speed);
        testScrollingBackground.setSpeedFixed(speedFixed);
        testScrollingBackground.setY1(0);
        testScrollingBackground.setY2(1024);
        testScrollingBackground.updateAndRender(2f, mockBatch);
    }
}