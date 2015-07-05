package data;

import org.jetbrains.annotations.NotNull;

public class SimpleClassificationData extends AbstractImmutableClassificationData {

    @NotNull
    private final float[][] X;
    @NotNull
    private final int[] Y;

    public SimpleClassificationData(@NotNull float[][] X, @NotNull int[] Y){
        if (X.length != Y.length) {
            throw new IllegalArgumentException("Data and classification sizes not equal");
        }
        this.X = X;
        this.Y = Y;
    }

    public int getCountFactors() {
        return X[0].length;
    }

    public int getCountData() {
        return X.length;
    }

    public float getFactorValue(int dataIndex, int factorIndex) {
        return X[dataIndex][factorIndex];
    }

    public int getClass(int dataIndex) {
        return Y[dataIndex];
    }

    @Override
    public float[] getFactorValueVector(int dataIndex) {
        return X[dataIndex];
    }
}
