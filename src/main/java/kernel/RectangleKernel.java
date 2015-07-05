package kernel;

public class RectangleKernel implements Kernel {

    @Override
    public float of(float value) {
        return Math.abs(value) <= 1 ? 0.5f : 0;
    }
}
