package data.parser;

import constants.GlobalConstants;
import data.SimpleClassificationData;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class ClassificationDataParserTest {

    private final ClassificationDataParser parser = new ClassificationDataParser();

    @Test
    public void testCorrect() throws IOException {
        String dataString = "1, 2, 3.5, 1\n1, 5, 6, 2\n1, 7, 8.88, 2";
        SimpleClassificationData data = parser.parse(new ByteArrayInputStream(dataString.getBytes()), 3);
        assertNotNull(data);
        assertEquals(3, data.getCountFactors());
        assertEquals(3, data.getCountData());
        Assert.assertEquals(1, data.getFactorValue(0, 0), GlobalConstants.DELTA);
        Assert.assertEquals(2, data.getFactorValue(0, 1), GlobalConstants.DELTA);
        Assert.assertEquals(3.5, data.getFactorValue(0, 2), GlobalConstants.DELTA);
        Assert.assertEquals(5, data.getFactorValue(1, 1), GlobalConstants.DELTA);
        Assert.assertEquals(8.88, data.getFactorValue(2, 2), GlobalConstants.DELTA);
        assertEquals(1, data.getClass(0));
        assertEquals(2, data.getClass(1));
        assertEquals(2, data.getClass(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoFactors() throws IOException {
        String dataString = "1\n2\n3";
        parser.parse(new ByteArrayInputStream(dataString.getBytes()), 0);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testClassIndexBig() throws IOException {
        String dataString = "1, 2, 3.5, 1\n1, 5, 6, 2\n1, 7, 8.88, 2";
        parser.parse(new ByteArrayInputStream(dataString.getBytes()), 4);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testClassIndexNegative() throws IOException {
        String dataString = "1, 2, 3.5, 1\n1, 5, 6, 2\n1, 7, 8.88, 2";
        parser.parse(new ByteArrayInputStream(dataString.getBytes()), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferentCountFactors() throws IOException {
        String dataString = "1, 2, 3.5, 1\n1, 5, 6, 2\n1, 7, 8.88";
        parser.parse(new ByteArrayInputStream(dataString.getBytes()), 0);
    }

    @Test
    public void testWithEmptyLines() throws IOException {
        String dataString = "\n\n1, 2, 3.5, 1\n1, 5, 6, 2\n\n\n\n1, 7, 8.88, 2\n\n\n\n";
        SimpleClassificationData data = parser.parse(new ByteArrayInputStream(dataString.getBytes()), 3);
        assertNotNull(data);
        assertEquals(3, data.getCountFactors());
        assertEquals(3, data.getCountData());
        Assert.assertEquals(1, data.getFactorValue(0, 0), GlobalConstants.DELTA);
        Assert.assertEquals(2, data.getFactorValue(0, 1), GlobalConstants.DELTA);
        Assert.assertEquals(3.5, data.getFactorValue(0, 2), GlobalConstants.DELTA);
        Assert.assertEquals(5, data.getFactorValue(1, 1), GlobalConstants.DELTA);
        Assert.assertEquals(8.88, data.getFactorValue(2, 2), GlobalConstants.DELTA);
        assertEquals(1, data.getClass(0));
        assertEquals(2, data.getClass(1));
        assertEquals(2, data.getClass(2));
    }
}
