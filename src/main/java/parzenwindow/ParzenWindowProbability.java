package parzenwindow;

import data.ClassificationData;
import data.PartialClassificationData;
import data.UnionClassificationData;
import kernel.Kernel;
import org.jetbrains.annotations.NotNull;
import utils.ArrayUtils;

import java.util.Arrays;

// todo: try not fixed window, but k-nearest
public class ParzenWindowProbability {

    // it's probability multiplied by constant, so it's not in range [0, 1]
    public static float probabilityOfClass(@NotNull ClassificationData data,
                                           float[] X,
                                           int classValue,
                                           Kernel kernel,
                                           float windowWidth) {
        int cd = data.getCountData();
        int cf = data.getCountFactors();
        if (X.length != cf) {
            throw new IllegalArgumentException("X length and count factors in data don't match.");
        }
        float sum = 0;
        for (int d = 0; d < cd; d++) {
            if (data.getClass(d) != classValue) {
                continue;
            }
            float dist = 0;
            for (int f = 0; f < cf; f++) {
                float dd = X[f] - data.getFactorValue(d, f);
                dist += dd * dd;
            }
            dist = (float) Math.sqrt(dist);
            sum += kernel.of(dist / windowWidth);
        }
        return sum;
    }

    public static int solveClass(@NotNull ClassificationData data,
                                 float[] X,
                                 Kernel kernel,
                                 float windowWidth) {
        int[] classValues = data.getClassValues();
        float max = -1;
        int maxClass = 0;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < classValues.length; i++) {
            float p = probabilityOfClass(data, X, classValues[i], kernel, windowWidth);
            if (p > max) {
                max = p;
                maxClass = classValues[i];
            }
        }
        return maxClass;
    }

    public static float calculateWindowWidth(@NotNull ClassificationData data,
                                             Kernel kernel,
                                             int countForLOO,
                                             float start,
                                             float end,
                                             float step) {
        float minError = Float.MAX_VALUE;
        float widthForMinError = start;
        for (float width = start; width < end; width += step) {
            float sumError = 0;
            ClassificationData[] dataArray = PartialClassificationData.split(
                    data, false, countForLOO);
            for (int i = 0; i < countForLOO; i++) {
                Object[] excludeArray = ArrayUtils.excludeFromArray(dataArray, i, i + 1);
                ClassificationData learnData = new UnionClassificationData(Arrays.copyOf(
                        excludeArray, excludeArray.length, ClassificationData[].class));
                ClassificationData testData = dataArray[i];
                int countError = 0;
                for (int d = 0; d < testData.getCountData(); d++) {
                    float[] X = testData.getFactorValueVector(d);
                    if (solveClass(learnData, X, kernel, width) != testData.getClass(d)) {
                        countError++;
                    }
                }
                float e = countError;
                e /= testData.getCountData();
                sumError += e;
            }
            sumError /= countForLOO;
            if (sumError < minError) {
                minError = sumError;
                widthForMinError = width;
            }
        }
        return widthForMinError;
    }
}
