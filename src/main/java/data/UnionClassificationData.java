package data;

import org.jetbrains.annotations.NotNull;

public class UnionClassificationData extends AbstractImmutableClassificationData {

    private final ClassificationData[] dataArray;
    private final int countData;
    private final int[] indexInDataArray;
    private final int[] sumInDataArray;

    public UnionClassificationData(@NotNull ClassificationData... dataArray) {
        this.dataArray = dataArray;
        sumInDataArray = new int[dataArray.length];
        sumInDataArray[0] = 0;
        int cf = dataArray[0].getCountFactors();
        int sum = dataArray[0].getCountData();
        for (int i = 1; i < dataArray.length; i++) {
            sumInDataArray[i] = sum;
            sum += dataArray[i].getCountData();
            if (dataArray[i].getCountFactors() != cf) {
                throw new IllegalArgumentException("Count factors in all data should be equal.");
            }
        }
        countData = sum;
        indexInDataArray = new int[countData];
        int ind = 0;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < dataArray.length; i++) {
            int cd = dataArray[i].getCountData();
            for (int j = 0; j < cd; j++) {
                indexInDataArray[ind++] = i;
            }
        }
    }

    @Override
    public int getCountFactors() {
        return dataArray[0].getCountFactors();
    }

    @Override
    public int getCountData() {
        return countData;
    }

    @Override
    public float getFactorValue(int dataIndex, int factorIndex) {
        return dataArray[indexInDataArray[dataIndex]].getFactorValue(
                dataIndex - sumInDataArray[indexInDataArray[dataIndex]], factorIndex);
    }

    @Override
    public int getClass(int dataIndex) {
        return dataArray[indexInDataArray[dataIndex]].getClass(
                dataIndex - sumInDataArray[indexInDataArray[dataIndex]]);
    }

    @Override
    public float[] getFactorValueVector(int dataIndex) {
        return dataArray[indexInDataArray[dataIndex]].getFactorValueVector(
                dataIndex - sumInDataArray[indexInDataArray[dataIndex]]);
    }
}
