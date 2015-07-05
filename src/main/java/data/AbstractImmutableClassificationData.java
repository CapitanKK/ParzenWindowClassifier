package data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractImmutableClassificationData implements ClassificationData {

    @Nullable
    private int[] classValues;

    @Override
    public int[] getClassValues() {
        if (classValues == null) {
            classValues = calculateClassValues();
        }
        return classValues;
    }

    private int[] calculateClassValues() {
        Set<Integer> set = new HashSet<>();
        int cd = getCountData();
        for (int i = 0; i < cd; i++) {
            set.add(getClass(i));
        }
        int[] values = new int[set.size()];
        int ind = 0;
        for (Integer i : set) {
            values[ind++] = i;
        }
        return values;
    }
}
