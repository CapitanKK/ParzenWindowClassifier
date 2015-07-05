import data.ClassificationData;
import data.PartialClassificationData;
import data.parser.ClassificationDataParser;
import kernel.EpanechnikovKernel;
import kernel.Kernel;
import normalization.Normalization;
import parzenwindow.ParzenWindowProbability;

import java.io.IOException;

public class Main {

    private static final int COUNT_TESTS = 100;
    private static final float LEARN_DATA = 0.7f;
    private static final int COUNT_LEAVE_ONE_OUT = 10;
    private static final float WINDOW_START = 3;
    private static final float WINDOW_END = 15;
    private static final float WINDOW_STEP = 0.1f;

    public static void p(String s) {
        System.out.println(s);
    }

    //todo: try exclude some attributes
    //todo: compare with other methods, and with ML libs
    public static void main(String[] args) throws IOException {
        p("Loading wine data...");
        ClassificationData data = new ClassificationDataParser().parse("wine.txt", 0);
        p("Data is loaded. Factors: " + data.getCountFactors());
        p("Normalization...");
        data = Normalization.removeConstantFactors(data);
        data = Normalization.normalize(data);
        p("Factors after normalization: " + data.getCountFactors());
        Kernel kernel = new EpanechnikovKernel();
        float error = 0;
        for (int i = 0; i < COUNT_TESTS; i++) {
            p("Test " + i + " start");
            ClassificationData[] parts = PartialClassificationData.split(data, true, LEARN_DATA);
            ClassificationData learnData = parts[0];
            ClassificationData testData = parts[1];
            float windowWidth = ParzenWindowProbability.calculateWindowWidth(learnData, kernel,
                    COUNT_LEAVE_ONE_OUT, WINDOW_START, WINDOW_END, WINDOW_STEP);
            p("Optimal window width is " + windowWidth);
            int countErrors = 0;
            for (int d = 0; d < testData.getCountData(); d++) {
                float[] X = testData.getFactorValueVector(d);
                if (ParzenWindowProbability.solveClass(learnData, X, kernel, windowWidth) !=
                        testData.getClass(d)) {
                    countErrors++;
                }
            }
            float e = countErrors;
            e /= testData.getCountData();
            p("Error: " + (e * 100) + "%");
            error += e;
        }
        error /= COUNT_TESTS;
        p("Mean error: " + (error * 100) + "%");
    }
}
