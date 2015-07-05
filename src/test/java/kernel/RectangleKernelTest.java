package kernel;

import constants.GlobalConstants;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangleKernelTest {

    @Test
    public void testValues() {
        Kernel kernel = new RectangleKernel();
        assertEquals(0, kernel.of(10), GlobalConstants.DELTA);
        assertEquals(0, kernel.of(7), GlobalConstants.DELTA);
        assertEquals(0, kernel.of(10000), GlobalConstants.DELTA);
        assertEquals(0, kernel.of(Float.MAX_VALUE), GlobalConstants.DELTA);
        assertEquals(0, kernel.of(-3), GlobalConstants.DELTA);
        assertEquals(0, kernel.of(-1.000001f), GlobalConstants.DELTA);
        assertEquals(0.5f, kernel.of(0), GlobalConstants.DELTA);
        assertEquals(0.5f, kernel.of(0.5f), GlobalConstants.DELTA);
        assertEquals(0.5f, kernel.of(0.2f), GlobalConstants.DELTA);
        assertEquals(0.5f, kernel.of(0.9999f), GlobalConstants.DELTA);
        assertEquals(0.5f, kernel.of(1), GlobalConstants.DELTA);
        assertEquals(0.5f, kernel.of(-1), GlobalConstants.DELTA);
        assertEquals(0.5f, kernel.of(-0.99f), GlobalConstants.DELTA);
    }
}
