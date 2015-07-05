package data;

import constants.GlobalConstants;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class SimpleClassificationDataTest {

    @Test
    public void testCorrect() {
        float[][] X = new float[][]{{1, 2.01f, 3}, {4, 5.5f, 6}, {7, 8.99f, 9}};
        int[] Y = new int[]{0, 10, 20};
        SimpleClassificationData data = new SimpleClassificationData(X, Y);
        assertEquals(3, data.getCountData());
        assertEquals(3, data.getCountFactors());
        Assert.assertEquals(1, data.getFactorValue(0, 0), GlobalConstants.DELTA);
        Assert.assertEquals(2.01f, data.getFactorValue(0, 1), GlobalConstants.DELTA);
        Assert.assertEquals(6, data.getFactorValue(1, 2), GlobalConstants.DELTA);
        assertEquals(10, data.getClass(1));
        assertEquals(20, data.getClass(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectSize() {
        float[][] X = new float[][]{{1, 2.01f, 3}, {4, 5.5f, 6}, {7, 8.99f, 9}};
        int[] Y = new int[]{0, 10};
        new SimpleClassificationData(X, Y);
    }

    @Test
    public void testClassValues() {
        float[][] X = new float[][]{{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}};
        int[] Y = new int[]{3, 5, 6, 6, 5, 3, 3, 3, 5, 5, 6};
        int[] values = new SimpleClassificationData(X, Y).getClassValues();
        assertNotNull(values);
        assertEquals(3, values.length);
        Arrays.sort(values);
        assertArrayEquals(new int[]{3, 5, 6}, values);
    }
}
