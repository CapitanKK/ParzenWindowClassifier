package utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Random;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Random.class, ArrayUtils.class })
public class ArrayUtilsTest {

    @Test
    public void testShuffleArray() throws Exception {
        Random mockedRandom = PowerMockito.mock(Random.class);
        PowerMockito.whenNew(Random.class).withNoArguments().thenReturn(mockedRandom);
        when(mockedRandom.nextInt(anyInt())).thenReturn(1).thenReturn(2)
                .thenReturn(0).thenReturn(2).thenReturn(1);
        int[] array = new int[]{1, 2, 3, 4, 5, 6};
        ArrayUtils.shuffleArray(array);
        assertArrayEquals(new int[]{2, 4, 3, 6, 1, 5}, array);
    }

    @Test
    public void testExcludeArray() {
        String[] array = new String[10];
        for (int i = 0; i < 10; i++) {
            array[i] = "" + i;
        }
        assertArrayEquals(new String[0], ArrayUtils.excludeFromArray(array, 0, 10));
        assertArrayEquals(new String[]{"0"}, ArrayUtils.excludeFromArray(array, 1, 10));
        assertArrayEquals(new String[]{"0", "1"}, ArrayUtils.excludeFromArray(array, 2, 10));
        assertArrayEquals(new String[]{"9"}, ArrayUtils.excludeFromArray(array, 0, 9));
        assertArrayEquals(new String[]{"8", "9"}, ArrayUtils.excludeFromArray(array, 0, 8));
        assertArrayEquals(array, ArrayUtils.excludeFromArray(array, 5, 5));
        assertArrayEquals(array, ArrayUtils.excludeFromArray(array, 0, 0));
        assertArrayEquals(array, ArrayUtils.excludeFromArray(array, 9, 9));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExcludeArrayException() {
        String[] array = new String[10];
        for (int i = 0; i < 10; i++) {
            array[i] = "" + i;
        }
        assertArrayEquals(array, ArrayUtils.excludeFromArray(array, 5, 4));
    }
}
