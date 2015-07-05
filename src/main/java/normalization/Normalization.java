package normalization;

import data.ClassificationData;
import data.SimpleClassificationData;
import org.jetbrains.annotations.NotNull;

public class Normalization {

    /**
     * Use removeConstantFactors before this.
     */
    public static ClassificationData normalize(@NotNull ClassificationData origin) {
        int cf = origin.getCountFactors();
        int cd = origin.getCountData();
        float[][] X = new float[cd][cf];
        for (int f = 0; f < cf; f++) {
            float m = 0, m2 = 0;
            for (int d = 0; d < cd; d++) {
                X[d][f] = origin.getFactorValue(d, f);
                m += X[d][f];
                m2 += X[d][f] * X[d][f];
            }
            m /= cd;
            m2 /= cd;
            float variance = m2 - m * m;
            if (variance == 0) {
                throw new IllegalArgumentException("Looks like constant factors in data." +
                        "Use method removeConstantFactors before this.");
            }
            for (int d = 0; d < cd; d++) {
                X[d][f] = (X[d][f] - m) / variance;
            }
        }

        return new SimpleClassificationData(X, getYFromClassificationData(origin));
    }

    private static int[] getYFromClassificationData(@NotNull ClassificationData origin) {
        int cd = origin.getCountData();
        int[] Y = new int[cd];
        for (int d = 0; d < cd; d++) {
            Y[d] = origin.getClass(d);
        }
        return Y;
    }

    /**
     * Removes columns with constant values. If all columns are constant, returns null.
     */
    public static ClassificationData removeConstantFactors(ClassificationData origin) {
        int cf = origin.getCountFactors();
        int cd = origin.getCountData();
        float[][] X = new float[cd][cf];
        int[] constantFactors = new int[cf];
        int cfc = 0;
        for (int f = 0; f < cf; f++) {
            X[0][f] = origin.getFactorValue(0, f);
            boolean allEqual = true;
            for (int d = 1; d < cd; d++) {
                X[d][f] = origin.getFactorValue(d, f);
                if (X[d][f] != X[0][f]) {
                    allEqual = false;
                }
            }
            if (allEqual) {
                constantFactors[cfc++] = f;
            }
        }
        ClassificationData result;
        if (cfc == 0) {
            result = origin;
        } else if (cfc == cf) {
            result = null;
        } else {
            float[][] newX = new float[cd][cf - cfc];
            int newF = 0;
            int cfi = 0;
            for (int f = 0; f < cf; f++) {
                if (cfi == cfc || constantFactors[cfi] != f) {
                    for (int d = 0; d < cd; d++) {
                        newX[d][newF] = X[d][f];
                    }
                    newF++;
                } else {
                    cfi++;
                }
            }
            result = new SimpleClassificationData(newX, getYFromClassificationData(origin));
        }
        return result;
    }
}
