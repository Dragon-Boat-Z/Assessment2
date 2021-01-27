package com.hardgforgif.dragonboatracing;

import com.hardgforgif.dragonboatracing.core.Lane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;

import com.hardgforgif.dragonboatracing.core.*;

public class BoatTest {
    @Mock
    Lane mockLane;

    @BeforeEach
    void setup(){
        mockLane = mock(Lane.class);
    }

    @Test
    void boatConstructor() {
        Boat testBoat = new Boat(120f, 90f, 100f, 110f, 2, mockLane);
        Assertions.assertEquals(120f, testBoat.getRobustness());
    }
}
