package io.github.celosia.sys.util;

import com.badlogic.gdx.utils.Array;

// Fixes the following problems with LibGDX's Array<>:
// -Lack of (ordered) and (array[], ordered) constructors
// -Odd naming (first -> getFirst, peek -> getLast)
// -Lack of removeFirst/Last (pop is like removeLast, but oddly named and returns the value)
public class ArrayX<T> extends Array<T> {

    public ArrayX() {
        super();
    }

    public ArrayX(boolean ordered) {
        super(ordered, 6);
    }

    public ArrayX(int capacity) {
        super(capacity);
    }

    public ArrayX(boolean ordered, int capacity) {
        super(ordered, capacity);
    }

    public ArrayX(T[] array) {
        super(array);
    }

    public ArrayX(T[] array, boolean ordered) {
        super(ordered, array, 0, array.length);
    }

    public ArrayX(Array<? extends T> array) {
        super(array);
    }

    public T getFirst() {
        return super.first();
    }

    public T getLast() {
        return super.peek();
    }

    public void removeFirst() {
        super.removeIndex(0);
    }

    public void removeLast() {
        super.removeIndex(size - 1);
    }
}
