package com.superman.home;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class DeepClone {
    private DeepClone() {
    }

    public static <T> T deepClone(final T input) {
        if (input == null) return null;

        if (input instanceof Map<?, ?>) {
            return (T) deepCloneMap((Map<?, ?>) input);
        } else if (input instanceof Collection<?>) {
            return (T) deepCloneCollection((Collection<?>) input);
        } else if (input instanceof Object[]) {
            return (T) deepCloneObjectArray((Object[]) input);
        } else if (input.getClass().isArray()) {
            return (T) clonePrimitiveArray((Object) input);
        }

        return input;
    }

    private static Object clonePrimitiveArray(final Object input) {
        final int length = Array.getLength(input);
        final Object output = Array.newInstance(input.getClass().getComponentType(), length);
        System.arraycopy(input, 0, output, 0, length);
        return output;
    }

    private static <E> E[] deepCloneObjectArray(final E[] input) {
        final E[] clone = (E[]) Array.newInstance(input.getClass().getComponentType(), input.length);
        for (int i = 0; i < input.length; i++) {
            clone[i] = deepClone(input[i]);
        }

        return clone;
    }

    private static <E> Collection<E> deepCloneCollection(final Collection<E> input) {
        Collection<E> clone;
        if (input instanceof LinkedList<?>) {
            clone = new LinkedList<>();
        } else if (input instanceof SortedSet<?>) {
            clone = new TreeSet<>();
        } else if (input instanceof Set) {
            clone = new HashSet<>();
        } else {
            clone = new ArrayList<>();
        }

        for (E item : input) {
            clone.add(deepClone(item));
        }

        return clone;
    }

    private static <K, V> Map<K, V> deepCloneMap(final Map<K, V> map) {
        Map<K, V> clone;
        if (map instanceof LinkedHashMap<?, ?>) {
            clone = new LinkedHashMap<>();
        } else if (map instanceof TreeMap<?, ?>) {
            clone = new TreeMap<>();
        } else {
            clone = new HashMap<>();
        }

        for (Map.Entry<K, V> entry : map.entrySet()) {
            clone.put(deepClone(entry.getKey()), deepClone(entry.getValue()));
        }

        return clone;
    }
}