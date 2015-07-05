package parzenwindow;

import constants.GlobalConstants;
import data.ClassificationData;
import data.SimpleClassificationData;
import kernel.EpanechnikovKernel;
import kernel.RectangleKernel;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParzenWindowProbabilityTest {

    @Test(expected = IllegalArgumentException.class)
    public void testWrongSize() {
        ClassificationData data = new ClassificationData() {
            @Override
            public int getCountFactors() {
                return 5;
            }

            @Override
            public int getCountData() {
                return 1;
            }

            @Override
            public float getFactorValue(int dataIndex, int factorIndex) {
                return 0;
            }

            @Override
            public int getClass(int dataIndex) {
                return 0;
            }

            @Override
            public int[] getClassValues() {
                return new int[0];
            }

            @Override
            public float[] getFactorValueVector(int dataIndex) {
                return new float[0];
            }
        };
        ParzenWindowProbability.probabilityOfClass(data, new float[]{0, 0, 0, 0}, 0,
                new RectangleKernel(), 1);
    }

    @Test
    public void testProbability() {
        float[][] X = new float[][] {
                {1, 5, 3},
                {2, 6, 1},
                {1, 4, 2},
                {3, 0, 1}};
        int[] Y = new int[]{0, 1, 0, 1};
        ClassificationData data = new SimpleClassificationData(X, Y);
        float[] vector = new float[]{2, 2, 2};
        assertEquals(0.3333f, ParzenWindowProbability.probabilityOfClass(
                data, vector, 0, new EpanechnikovKernel(), 3), GlobalConstants.DELTA);
        assertEquals(0.25f, ParzenWindowProbability.probabilityOfClass(
                data, vector, 1, new EpanechnikovKernel(), 3), GlobalConstants.DELTA);
    }
}
