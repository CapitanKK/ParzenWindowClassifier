package data;

import constants.GlobalConstants;
import org.fest.reflect.core.Reflection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import utils.ArrayUtils;

import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Random.class, ArrayUtils.class })
public class PartialClassificationDataTest {

    private ClassificationData origin = new ClassificationData() {
        @Override
        public int getCountFactors() {
            return 10;
        }

        @Override
        public int getCountData() {
            return 13;
        }

        @Override
        public float getFactorValue(int dataIndex, int factorIndex) {
            return dataIndex * 100 + factorIndex;
        }

        @Override
        public int getClass(int dataIndex) {
            return dataIndex;
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

    @Test
    public void testConstructor() {
        ClassificationData classificationData = Reflection.constructor()
                .withParameterTypes(ClassificationData.class, int[].class)
                .in(PartialClassificationData.class)
                .newInstance(origin, new int[]{12, 3, 2, 0, 7});
        assertNotNull(classificationData);
        assertEquals(5, classificationData.getCountData());
        assertEquals(10, classificationData.getCountFactors());
        assertEquals(12, classificationData.getClass(0));
        assertEquals(3, classificationData.getClass(1));
        assertEquals(2, classificationData.getClass(2));
        assertEquals(0, classificationData.getClass(3));
        assertEquals(7, classificationData.getClass(4));
        Assert.assertEquals(1200, classificationData.getFactorValue(0, 0), GlobalConstants.DELTA);
        Assert.assertEquals(301, classificationData.getFactorValue(1, 1), GlobalConstants.DELTA);
        Assert.assertEquals(202, classificationData.getFactorValue(2, 2), GlobalConstants.DELTA);
        Assert.assertEquals(3, classificationData.getFactorValue(3, 3), GlobalConstants.DELTA);
        Assert.assertEquals(704, classificationData.getFactorValue(4, 4), GlobalConstants.DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongFloat1() {
        PartialClassificationData.split(origin, false, -1, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongFloat2() {
        PartialClassificationData.split(origin, true, 0.3f, 1.1f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSumMoreThanOne() {
        PartialClassificationData.split(origin, true, 0.1f, 0.5f, 0.3f, 0.2f);
    }

    @Test
    public void testStaticSplitRandom() throws Exception {
        Random mockedRandom = PowerMockito.mock(Random.class);
        PowerMockito.whenNew(Random.class).withNoArguments().thenReturn(mockedRandom);
        // from 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
        // to 5, 11, 3, 2, 4, 9, 10, 0, 1, 6, 7, 12, 8
        when(mockedRandom.nextInt(anyInt()))
                .thenReturn(5)
                .thenReturn(10)
                .thenReturn(1)
                .thenReturn(0)
                .thenReturn(0)
                .thenReturn(4)
                .thenReturn(4)
                .thenReturn(2)
                .thenReturn(3)
                .thenReturn(1)
                .thenReturn(0)
                .thenReturn(1);
        ClassificationData[] data = PartialClassificationData.split(origin, true, 0.3f, 0.05f, 0.1f);
        assertNotNull(data);
        assertEquals(4, data.length);
        assertNotNull(data[0]);
        assertEquals(3, data[0].getCountData());
        assertEquals(10, data[0].getCountFactors());
        assertEquals(5, data[0].getClass(0));
        assertEquals(11, data[0].getClass(1));
        assertEquals(3, data[0].getClass(2));
        Assert.assertEquals(1105, data[0].getFactorValue(1, 5), GlobalConstants.DELTA);
        assertNotNull(data[1]);
        assertEquals(1, data[1].getCountData());
        assertEquals(2, data[1].getClass(0));
        assertNotNull(data[2]);
        assertEquals(1, data[2].getCountData());
        assertEquals(4, data[2].getClass(0));
        assertNotNull(data[3]);
        assertEquals(8, data[3].getCountData());
        assertEquals(9, data[3].getClass(0));
        assertEquals(10, data[3].getClass(1));
        assertEquals(0, data[3].getClass(2));
        assertEquals(1, data[3].getClass(3));
        assertEquals(6, data[3].getClass(4));
        assertEquals(7, data[3].getClass(5));
        assertEquals(12, data[3].getClass(6));
        assertEquals(8, data[3].getClass(7));
    }

    @Test
    public void testNotRandom() {
        ClassificationData[] data = PartialClassificationData.split(origin, false, 0.3f, 0.05f, 0.1f);
        assertNotNull(data);
        assertEquals(4, data.length);
        assertNotNull(data[0]);
        assertEquals(3, data[0].getCountData());
        assertEquals(10, data[0].getCountFactors());
        assertEquals(0, data[0].getClass(0));
        assertEquals(1, data[0].getClass(1));
        assertEquals(2, data[0].getClass(2));
        assertNotNull(data[1]);
        assertEquals(1, data[1].getCountData());
        assertEquals(3, data[1].getClass(0));
        assertNotNull(data[2]);
        assertEquals(1, data[2].getCountData());
        assertEquals(4, data[2].getClass(0));
        assertNotNull(data[3]);
        assertEquals(8, data[3].getCountData());
        assertEquals(5, data[3].getClass(0));
        assertEquals(6, data[3].getClass(1));
        assertEquals(7, data[3].getClass(2));
        assertEquals(8, data[3].getClass(3));
        assertEquals(9, data[3].getClass(4));
        assertEquals(10, data[3].getClass(5));
        assertEquals(11, data[3].getClass(6));
        assertEquals(12, data[3].getClass(7));
    }

    @Test
    public void testArrays() {
        PartialClassificationData.split(origin, true, 0.3f);
        PartialClassificationData.split(origin, true, 0f);
        PartialClassificationData.split(origin, true, 1f);
        PartialClassificationData.split(origin, true, 0, 1);
        PartialClassificationData.split(origin, true, 0.1f);
        PartialClassificationData.split(origin, true, 0, 1, 0);
        PartialClassificationData.split(origin, true, 0.3f, 0.3f, 0.3f);
        PartialClassificationData.split(origin, true, 0.1f, 0.2f, 0.003f, 0.3f);
        PartialClassificationData.split(origin, true, 0.0001f, 0.0002f, 0.5f);
    }

    @Test
    public void testSplitPart() {
        for (int i = 1; i < 100; i++) {
            testSplitPart(i);
        }
    }

    private void testSplitPart(final int part) {
        ClassificationData data = new ClassificationData() {
            @Override
            public int getCountFactors() {
                return 10;
            }

            @Override
            public int getCountData() {
                return part;
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
        PartialClassificationData[] array = PartialClassificationData.split(data, true, part);
        assertNotNull(array);
        assertEquals(part, array.length);
        for (int i = 0; i < part; i++) {
            assertNotNull(array[i]);
            assertEquals(1, array[i].getCountData());
        }
    }

    @Test
    public void testSplitPart2() {
        ClassificationData data = new AbstractImmutableClassificationData() {
            @Override
            public int getCountFactors() {
                return 10;
            }

            @Override
            public int getCountData() {
                return 24;
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
            public float[] getFactorValueVector(int dataIndex) {
                return new float[0];
            }
        };
        ClassificationData[] dataArray = PartialClassificationData.split(data, false, 6);
        assertEquals(6, dataArray.length);
        for (int i = 0; i < 6; i++) {
            assertEquals(4, dataArray[i].getCountData());
        }
    }

    @Test
    public void testClassValues() {
        ClassificationData data = new AbstractImmutableClassificationData() {
            @Override
            public int getCountFactors() {
                return 10;
            }

            @Override
            public int getCountData() {
                return 24;
            }

            @Override
            public float getFactorValue(int dataIndex, int factorIndex) {
                return 0;
            }

            @Override
            public int getClass(int dataIndex) {
                return dataIndex / 4;
            }

            @Override
            public float[] getFactorValueVector(int dataIndex) {
                return new float[0];
            }
        };
        ClassificationData[] dataArray = PartialClassificationData.split(data, false, 6);
        assertEquals(6, dataArray.length);
        for (int i = 0; i < 6; i++) {
            assertEquals(1, dataArray[i].getClassValues().length);
        }
        dataArray = PartialClassificationData.split(data, false, 3);
        assertEquals(3, dataArray.length);
        for (int i = 0; i < 3; i++) {
            assertEquals(2, dataArray[i].getClassValues().length);
        }
    }
}
