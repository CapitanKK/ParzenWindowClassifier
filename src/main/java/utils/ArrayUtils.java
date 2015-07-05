package utils;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class ArrayUtils {

    public static void shuffleArray(@NotNull int[] array) {
        Random rand = new Random();
        for (int i = 0; i < array.length - 1; i++) {
            int k = rand.nextInt(array.length - i);
            int t = array[i];
            array[i] = array[k + i];
            array[k + i] = t;
        }
    }

    public static Object[] excludeFromArray(@NotNull Object[] array, int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("end should be >= start");
        }
        Object[] newArray = new Object[array.length - (end - start)];
        System.arraycopy(array, 0, newArray, 0, start);
        System.arraycopy(array, end, newArray, start, array.length - end);
        return newArray;
    }
}
