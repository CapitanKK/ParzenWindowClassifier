package kernel;

public class QuarticKernel implements Kernel {
    @Override
    public float of(float value) {
        if (Math.abs(value) <= 1) {
            return (1 - value * value) * (1 - value * value) * 15 / 16;
        } else {
            return 0;
        }
    }
}
