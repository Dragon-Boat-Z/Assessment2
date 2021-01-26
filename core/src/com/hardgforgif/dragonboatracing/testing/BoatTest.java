package com.hardgforgif.dragonboatracing.core.testing;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

@DisplayName("Test Boat Class")
public class TestBoat {

    Lane mockLane = Mockito.mock(Lane.class);

    @Before
    public void setUp(){
        Boat testBoat = new Boat(120f, 90f, 100f, 110f, 3, mockLane);
    }

    @Test
    public void testBoatConstructor(){
        assertEquals(120f, testBoat.getRobustness(), "Yo sort out the spaghetti code loser!");
    }
}
