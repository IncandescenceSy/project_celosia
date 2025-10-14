package io.github.celosia.sys.util;

import com.badlogic.gdx.graphics.Color;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.IntStream;

public class ArrayLib {

    public static <T> boolean add(T[] arr, T item) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                arr[i] = item;
                return true;
            }
        }
        return false;
    }

    public static <T> boolean contains(T[] arr, T target) {
        for (T t : arr) {
            if (Objects.equals(target, t)) {
                return true;
            }
        }
        return false;
    }

    // Sorts float[] and then sorts Color[] with the same order
    public static void sortParallel(float[] floats, Color[] colors) {
        if (floats.length != colors.length) {
            throw new IllegalArgumentException("Arrays must have the same length");
        }

        Integer[] indices = IntStream.range(0, floats.length)
                .boxed()
                .toArray(Integer[]::new);

        Arrays.sort(indices, Comparator.comparingDouble(i -> floats[i]));

        float[] sortedFloats = new float[floats.length];
        Color[] sortedColors = new Color[colors.length];

        for (int i = 0; i < indices.length; i++) {
            sortedFloats[i] = floats[indices[i]];
            sortedColors[i] = colors[indices[i]];
        }

        System.arraycopy(sortedFloats, 0, floats, 0, floats.length);
        System.arraycopy(sortedColors, 0, colors, 0, colors.length);
    }
}
