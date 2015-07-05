package data.parser;

import data.SimpleClassificationData;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClassificationDataParser {

    public SimpleClassificationData parse(@NotNull String fileName, int classIndex) throws IOException {
        return parse(new FileInputStream(fileName), classIndex);
    }

    public SimpleClassificationData parse(@NotNull InputStream inputStream, int classIndex)
            throws IOException {
        UtilReader reader = new UtilReader(new BufferedReader(new InputStreamReader(inputStream)));
        String firstLine = reader.readNonEmptyLine();
        if (firstLine == null) {
            throw new IllegalArgumentException("Input stream is empty");
        }
        String[] tokens = firstLine.split(",");
        if (tokens.length <= 1) {
            throw new IllegalArgumentException("There are should be class and at least one " +
                    "factor in input stream.");
        }
        if (classIndex >= tokens.length) {
            throw new ArrayIndexOutOfBoundsException("Index of class " + classIndex + " is " +
                    "too big. There are only " + tokens.length + " tokens in one line.");
        }
        if (classIndex < 0) {
            throw new ArrayIndexOutOfBoundsException("Index of class should be >= 0");
        }
        int countFactors = tokens.length - 1;
        List<float[]> listFactors = new ArrayList<>();
        List<Integer> listClasses = new ArrayList<>();
        String line  = firstLine;
        while (line != null && line.length() > 0) {
            tokens = line.split(",");
            if (tokens.length != countFactors + 1) {
                throw new IllegalArgumentException("There should be the same count of factors in" +
                        "each line in input stream.");
            }
            float[] factors = new float[countFactors];
            int clazz = -1;
            int srcIndex = 0;
            int dstIndex = 0;
            while (srcIndex <= countFactors) {
                if (srcIndex == classIndex) {
                    clazz = Integer.parseInt(tokens[srcIndex].trim());
                } else {
                    factors[dstIndex++] = Float.parseFloat(tokens[srcIndex].trim());
                }
                srcIndex++;
            }
            listFactors.add(factors);
            listClasses.add(clazz);
            line = reader.readNonEmptyLine();
        }
        float[][] X = new float[listFactors.size()][];
        int[] Y = new int[listFactors.size()];
        for (int i = 0; i < listFactors.size(); i++) {
            X[i] = listFactors.get(i);
            Y[i] = listClasses.get(i);
        }
        return new SimpleClassificationData(X, Y);
    }

    private static class UtilReader {

        @NotNull
        final BufferedReader reader;

        UtilReader(@NotNull BufferedReader reader) {
            this.reader = reader;
        }

        String readNonEmptyLine() throws IOException {
            String line = "";
            while (line != null && line.length() == 0) {
                line = reader.readLine();
            }
            return line;
        }
    }
}
