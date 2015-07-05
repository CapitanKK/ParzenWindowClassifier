package data;

import constants.GlobalConstants;
import org.jetbrains.annotations.NotNull;
import utils.ArrayUtils;

import java.util.Arrays;

public class PartialClassificationData extends AbstractImmutableClassificationData {

    @NotNull
    private final ClassificationData classificationData;
    @NotNull
    private final int[] indices;

    /**
     * Splits classification data.
     * @param classificationData original classification data.
     * @param partPercent parts, each in range [0, 1] and their sum should be <= 1.
     * @return parts corresponding partPercent and last part for rest,
     * so result.length = partPercent.length + 1; if part zero-length it is null.
     * Example: 11-sized original and partPercent 0.05, 0.1, 0.2, 0.3
     * result is array of 5 PartialClassificationData
     * first is 11 * 0.05 = 0.55 = 0 - null
     * second 11 * 0.15 - 0 = 1.65  - 0 = 1 - PartialClassificationData of length 1
     * third 11 * 0.35 - 1 = 3.85 - 1 = 2 - PartialClassificationData of length 2
     * 4th 11 * 0.65 - 3 = 7.15 - 3 = 4 - PartialClassificationData of length 4
     * 5th rest 11 - 1 - 2 - 4 = 4 - PartialClassificationData of length 4
     */
    public static PartialClassificationData[] split(
            @NotNull ClassificationData classificationData,
            boolean randomly,
            float... partPercent) {
        float sp = 0;
        for (float p : partPercent) {
            if (p < 0 || p > 1) {
                throw new IllegalArgumentException("Part percent should be in range [0, 1].");
            }
            sp += p;
        }
        if (sp > 1 + GlobalConstants.DELTA) {
            throw new IllegalArgumentException("Sum of percent array should be <= 1.");
        }
        int count = classificationData.getCountData();
        int[] partArray = new int[partPercent.length];
        int used = 0;
        sp = 0;
        for (int i = 0; i < partPercent.length; i++) {
            sp += partPercent[i];
            int c = (int) (count * sp);
            partArray[i] = c - used;
            used = c;
        }
        return split(classificationData, randomly, partArray);
    }

    /**
     * Splits classification data randomly.
     * @param classificationData original classification data.
     * @param parts count of parts.
     * @return "equal" parts
     */
    public static PartialClassificationData[] split(
            @NotNull ClassificationData classificationData, boolean randomly, int parts) {
        int[] partsArray = new int[parts - 1];
        int cd = classificationData.getCountData();
        int used = 0;
        for (int i = 0; i < parts - 1; i++) {
            int c = cd * (i + 1) / parts;
            partsArray[i] = c - used;
            used = c;
        }
        return split(classificationData, randomly, partsArray);
    }

    private static PartialClassificationData[] split(
            @NotNull ClassificationData classificationData, boolean randomly, int[] parts) {
        int sum = 0;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < parts.length; i++) {
            sum += parts[i];
        }
        if (sum > classificationData.getCountData()) {
            throw new IllegalArgumentException("Sum of parts should be less or equal than " +
                    "count of origin data.");
        }
        int count = classificationData.getCountData();
        int[] indices = new int[count];
        for (int i = 0; i < count; i++) {
            indices[i] = i;
        }
        if (randomly) {
            ArrayUtils.shuffleArray(indices);
        }
        int used = 0;
        int length = parts.length;
        sum = 0;
        PartialClassificationData[] result = new PartialClassificationData[length + 1];
        for (int i = 0; i < length; i++) {
            sum += parts[i];
            if (sum > used) {
                result[i] = new PartialClassificationData(classificationData,
                        Arrays.copyOfRange(indices, used, sum));
                used = sum;
            } else {
                result[i] = null;
            }
        }
        if (used < count) {
            result[length] = new PartialClassificationData(classificationData,
                    Arrays.copyOfRange(indices, used, count));
        } else {
            result[length] = null;
        }
        return result;
    }

    private PartialClassificationData(@NotNull ClassificationData classificationData,
                                      @NotNull int[] indices) {
        checkData(classificationData, indices);
        this.classificationData = classificationData;
        this.indices = indices;
    }

    private void checkData(@NotNull ClassificationData classificationData, @NotNull int[] indices) {
        int countOrigin = classificationData.getCountData();
        int[] copyIndices = Arrays.copyOf(indices, indices.length);
        Arrays.sort(copyIndices);
        for (int i = 0; i < copyIndices.length; i++) {
            if (copyIndices[i] < 0 || copyIndices[i] >= countOrigin) {
                throw new ArrayIndexOutOfBoundsException("Indices of partial data should " +
                        "correspond to original data.");
            }
            if (i > 0 && copyIndices[i] == copyIndices[i - 1]) {
                throw new IllegalArgumentException("Indices of partial data should contain " +
                        "each index only once. Error index: " + copyIndices[i]);
            }
        }
    }

    @Override
    public int getCountFactors() {
        return classificationData.getCountFactors();
    }

    @Override
    public int getCountData() {
        return indices.length;
    }

    @Override
    public float getFactorValue(int dataIndex, int factorIndex) {
        return classificationData.getFactorValue(indices[dataIndex], factorIndex);
    }

    @Override
    public int getClass(int dataIndex) {
        return classificationData.getClass(indices[dataIndex]);
    }

    @Override
    public float[] getFactorValueVector(int dataIndex) {
        return classificationData.getFactorValueVector(indices[dataIndex]);
    }
}
