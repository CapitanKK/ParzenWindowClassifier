package data;

public interface ClassificationData {

    int getCountFactors();
    int getCountData();
    float getFactorValue(int dataIndex, int factorIndex);
    int getClass(int dataIndex);
    int[] getClassValues();
    float[] getFactorValueVector(int dataIndex);
}
