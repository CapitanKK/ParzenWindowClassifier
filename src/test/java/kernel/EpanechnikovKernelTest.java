package kernel;

import constants.GlobalConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EpanechnikovKernelTest {

    @Test
    public void testValues() {
        Kernel kernel = new EpanechnikovKernel();
        assertEquals(0, kernel.of(10), GlobalConstants.DELTA);
        assertEquals(0, kernel.of(7), GlobalConstants.DELTA);
        assertEquals(0, kernel.of(10000), GlobalConstants.DELTA);
        assertEquals(0, kernel.of(Float.MAX_VALUE), GlobalConstants.DELTA);
        assertEquals(0, kernel.of(-3), GlobalConstants.DELTA);
        assertEquals(0, kernel.of(-1.000001f), GlobalConstants.DELTA);
        assertEquals(0, kernel.of(1), GlobalConstants.DELTA);
        assertEquals(0, kernel.of(-1), GlobalConstants.DELTA);
        assertEquals(0.75f, kernel.of(0), GlobalConstants.DELTA);
        assertEquals(0.5625f, kernel.of(0.5f), GlobalConstants.DELTA);
        assertEquals(0.5625f, kernel.of(-0.5f), GlobalConstants.DELTA);
    }
}
