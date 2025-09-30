package io.github.celosia.sys.lib;

import java.util.Objects;

public class ArrayLib {
	public static <T> boolean contains(T[] arr, T target) {
		for (T t : arr) {
			if (Objects.equals(target, t)) {
				return true;
			}
		}
		return false;
	}
}
