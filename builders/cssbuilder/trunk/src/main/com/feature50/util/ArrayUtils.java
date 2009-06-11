package com.feature50.util;

import java.util.Collection;
import java.util.Iterator;

public class ArrayUtils {
    public static boolean isNullOrEmpty(Collection collection) {
        return ((collection == null) || (collection.isEmpty()));
    }

    public static boolean isNullOrEmpty(Object[] array) {
        return ((array == null) || (array.length == 0));
    }

    public static Object getIndex(Object collectionOrArray, int index) {
        if (collectionOrArray instanceof Collection) {
            return getIndex((Collection) collectionOrArray, index);
        } else if (collectionOrArray.getClass().isArray()) {
            return ((Object[]) collectionOrArray)[index];
        } else {
            throw new IllegalArgumentException(String.format("Don't know how to treat object of type '%1$s' as an array", collectionOrArray.getClass()));
        }
    }

    private static Object getIndex(Collection collection, int index) {
        int counter = 0;
        for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
            Object o = iterator.next();
            if (counter++ == index) return o;
        }
        throw new IndexOutOfBoundsException(String.format("Index %1$d is out of bounds of the passed collection of size %2$d", index, counter));
    }
}
