package io.github.celosia.sys.util;

import java.util.Objects;

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
}
