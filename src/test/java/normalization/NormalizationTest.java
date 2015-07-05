package normalization;

import constants.GlobalConstants;
import data.ClassificationData;
import data.SimpleClassificationData;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalizationTest {

    @Test
    public void testNormalize() {
        float[][] X = { {2, 7, 4},
                        {3, 0, 2},
                        {4, 3, -1}};
        int[] Y = new int[]{3, 1, 1};
        SimpleClassificationData origin = new SimpleClassificationData(X, Y);
        ClassificationData data = Normalization.normalize(origin);
        assertNotNull(data);
        assertEquals(3, data.getCountData());
        assertEquals(3, data.getCountFactors());
        assertEquals(3, data.getClass(0));
        assertEquals(1, data.getClass(1));
        assertEquals(1, data.getClass(2));
        assertEquals(-1.5f, data.getFactorValue(0, 0), GlobalConstants.DELTA);
        assertEquals(0, data.getFactorValue(1, 0), GlobalConstants.DELTA);
        assertEquals(1.5f, data.getFactorValue(2, 0), GlobalConstants.DELTA);
        assertEquals(0.445945946f, data.getFactorValue(0, 1), GlobalConstants.DELTA);
        assertEquals(-0.405405405f, data.getFactorValue(1, 1), GlobalConstants.DELTA);
        assertEquals(-0.040540541f, data.getFactorValue(2, 1), GlobalConstants.DELTA);
        assertEquals(0.552631579f, data.getFactorValue(0, 2), GlobalConstants.DELTA);
        assertEquals(0.078947368f, data.getFactorValue(1, 2), GlobalConstants.DELTA);
        assertEquals(-0.631578947f, data.getFactorValue(2, 2), GlobalConstants.DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormalizeWithConstantFactors() {
        float[][] X = { {2, 7, 4},
                        {3, 7, 2},
                        {4, 7, -1}};
        int[] Y = new int[]{3, 1, 1};
        Normalization.normalize(new SimpleClassificationData(X, Y));
    }

    @Test
    public void testRemoveConstantFactors1() {
        float[][] X = { {2, 7, 4},
                        {3, 7, 2},
                        {4, 7, -1}};
        int[] Y = new int[]{3, 1, 1};
        ClassificationData data = Normalization.removeConstantFactors(
                new SimpleClassificationData(X, Y));
        assertNotNull(data);
        assertEquals(3, data.getCountData());
        assertEquals(2, data.getCountFactors());
        assertEquals(2, data.getFactorValue(0, 0), GlobalConstants.DELTA);
        assertEquals(4, data.getFactorValue(0, 1), GlobalConstants.DELTA);
        assertEquals(3, data.getFactorValue(1, 0), GlobalConstants.DELTA);
        assertEquals(2, data.getFactorValue(1, 1), GlobalConstants.DELTA);
        assertEquals(4, data.getFactorValue(2, 0), GlobalConstants.DELTA);
        assertEquals(-1, data.getFactorValue(2, 1), GlobalConstants.DELTA);
    }

    @Test
    public void testRemoveConstantFactors2() {
        float[][] X = { {2, 7, 4, 4, 3, 5},
                        {3, 7, 2, 4, 3, 6},
                        {4, 7, -1, 3.9f, 3, 5}};
        int[] Y = new int[]{3, 1, 1};
        ClassificationData data = Normalization.removeConstantFactors(
                new SimpleClassificationData(X, Y));
        assertNotNull(data);
        assertEquals(3, data.getCountData());
        assertEquals(4, data.getCountFactors());
        assertEquals(2, data.getFactorValue(0, 0), GlobalConstants.DELTA);
        assertEquals(4, data.getFactorValue(0, 1), GlobalConstants.DELTA);
        assertEquals(4, data.getFactorValue(0, 2), GlobalConstants.DELTA);
        assertEquals(5, data.getFactorValue(0, 3), GlobalConstants.DELTA);
    }

    @Test
    public void testRemoveConstantFactorsAll() {
        float[][] X = { {2, 7, 4},
                        {2, 7, 4},
                        {2, 7, 4}};
        int[] Y = new int[]{3, 1, 1};
        ClassificationData data = Normalization.removeConstantFactors(
                new SimpleClassificationData(X, Y));
        assertNull(data);
    }
}
