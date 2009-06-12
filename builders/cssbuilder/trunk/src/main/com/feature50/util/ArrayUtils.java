/*
 * Copyright 2007 Ben Galbraith.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
