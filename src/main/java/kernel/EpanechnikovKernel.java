package kernel;

public class EpanechnikovKernel implements Kernel {
    @Override
    public float of(float value) {
        if (Math.abs(value) <= 1) {
            return 0.75f * (1 - value * value);
        } else {
            return 0;
        }
    }
}
