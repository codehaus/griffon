/*
 * Copyright 2011 the original author or authors.
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


package griffon.plugins.weld.util;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Andres Almiray
 *
 * @param <T>
 */
public class ClassCache<T> {
    private final Map<Class, Reference<NonContextual<T>>> CACHE = Collections.synchronizedMap(new WeakHashMap<Class, Reference<NonContextual<T>>>());

    public NonContextual<T> get(Class clazz) {
        Reference<NonContextual<T>> ref = CACHE.get(clazz);
        return ref != null? ref.get() : null;
    }

    public void put(Class clazz, NonContextual<T> nc) {
        CACHE.put(clazz, new WeakReference<NonContextual<T>>(nc));
    }
}
