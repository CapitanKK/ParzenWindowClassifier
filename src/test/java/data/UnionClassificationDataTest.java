package data;

import constants.GlobalConstants;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class UnionClassificationDataTest {

    @Test
    public void testUnion() {
        float[][] X = new float[][]{{1, 3, 8}};
        int[] Y = new int[]{0};
        ClassificationData classificationData1 = new SimpleClassificationData(X, Y);
        X = new float[][]{{6, 4, 1}, {3, 3, 4}, {2, 2, 2}};
        Y = new int[]{3, 4, 5};
        ClassificationData classificationData2 = new SimpleClassificationData(X, Y);
        X = new float[][]{{0, 0, 0}, {12, 13, 14}};
        Y = new int[]{8, 9};
        ClassificationData classificationData3 = new SimpleClassificationData(X, Y);
        UnionClassificationData union = new UnionClassificationData(classificationData1,
                classificationData2, classificationData3);
        assertNotNull(union);
        assertEquals(6, union.getCountData());
        assertEquals(3, union.getCountFactors());
        assertEquals(0, union.getClass(0));
        assertEquals(3, union.getClass(1));
        assertEquals(4, union.getClass(2));
        assertEquals(5, union.getClass(3));
        assertEquals(8, union.getClass(4));
        assertEquals(9, union.getClass(5));
        assertEquals(1, union.getFactorValue(0, 0), GlobalConstants.DELTA);
        assertEquals(8, union.getFactorValue(0, 2), GlobalConstants.DELTA);
        assertEquals(6, union.getFactorValue(1, 0), GlobalConstants.DELTA);
        assertEquals(3, union.getFactorValue(2, 0), GlobalConstants.DELTA);
        assertEquals(2, union.getFactorValue(3, 0), GlobalConstants.DELTA);
        assertEquals(0, union.getFactorValue(4, 0), GlobalConstants.DELTA);
        assertEquals(12, union.getFactorValue(5, 0), GlobalConstants.DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongCountFactors() {
        float[][] X = new float[][]{{1, 3, 8}};
        int[] Y = new int[]{0};
        ClassificationData classificationData1 = new SimpleClassificationData(X, Y);
        X = new float[][]{{6, 4}, {3, 3}, {2, 2}};
        Y = new int[]{3, 4, 5};
        ClassificationData classificationData2 = new SimpleClassificationData(X, Y);
        new UnionClassificationData(classificationData1, classificationData2);
    }

    @Test
    public void testClassValues() {
        float[][] X = new float[][]{{1, 3, 8}};
        int[] Y = new int[]{0};
        ClassificationData classificationData1 = new SimpleClassificationData(X, Y);
        X = new float[][]{{6, 4, 1}, {3, 3, 4}, {2, 2, 2}};
        Y = new int[]{0, 9, 8};
        ClassificationData classificationData2 = new SimpleClassificationData(X, Y);
        X = new float[][]{{0, 0, 0}, {12, 13, 14}};
        Y = new int[]{8, 9};
        ClassificationData classificationData3 = new SimpleClassificationData(X, Y);
        int[] values = new UnionClassificationData(classificationData1,
                classificationData2, classificationData3).getClassValues();
        assertEquals(3, values.length);
        Arrays.sort(values);
        assertArrayEquals(new int[]{0, 8, 9}, values);
    }
}
