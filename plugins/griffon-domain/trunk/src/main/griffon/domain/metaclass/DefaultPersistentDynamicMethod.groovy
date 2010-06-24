/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.domain.metaclass

import griffon.domain.orm.Criterion

/**
 * @author Andres Almiray
 */
enum DefaultPersistentDynamicMethod {
    COUNT([new MethodSignature(true, Integer.TYPE, 'count')]),

    MAKE([new MethodSignature(true, Object, 'make'),
         new MethodSignature(true, Object, 'make', Map)]),

    SAVE([new MethodSignature(Object, 'save')]),

    DELETE([new MethodSignature(Object, 'delete')]),

    FETCH([new MethodSignature(true, Object, 'fetch', Object)]),

    LIST([new MethodSignature(true, Collection, 'list'),
         new MethodSignature(true, Collection, 'list', Map)]),

    FIND([new MethodSignature(true, Object, 'find', Object),
         new MethodSignature(true, Object, 'find', Criterion),
         new MethodSignature(true, Object, 'find', Criterion, Map),
         new MethodSignature(true, Object, 'find', Closure),
         new MethodSignature(true, Object, 'find', Map, Closure)]),

    FIND_ALL([new MethodSignature(true, Collection, 'findAll'),
         new MethodSignature(true, Collection, 'findAll', Object),
         new MethodSignature(true, Collection, 'findAll', Criterion),
         new MethodSignature(true, Collection, 'findAll', Criterion, Map),
         new MethodSignature(true, Collection, 'findAll', Closure),
         new MethodSignature(true, Collection, 'findAll', Map, Closure)]),

    FIND_WHERE('findWhere', [new MethodSignature(true, Object, 'findWhere', Map),
         new MethodSignature(true, Collection, 'findWhere', Closure)]),

    FIND_ALL_WHERE('findAllWhere', [new MethodSignature(true, Collection, 'findAllWhere', Map),
         new MethodSignature(true, Collection, 'findAllWhere', Closure)]);

    // COUNT_BY('countBy'),
    // FIND_BY('findBy'),
    // FIND_ALL_BY('findAllBy');

    final String methodName
    final MethodSignature[] methodSignatures

    DefaultPersistentDynamicMethod(String methodName = null, List methodSignatures) {
        this.methodName = methodName ?: name().toLowerCase()
        this.methodSignatures = methodSignatures as MethodSignature[]
    }

    String toString() {
        this.methodName
    }

    static DefaultPersistentDynamicMethod[] allMethods() {
        [COUNT, MAKE, SAVE, DELETE, FETCH, LIST,
         FIND, FIND_ALL, FIND_WHERE, FIND_ALL_WHERE] as DefaultPersistentDynamicMethod[]
    }
}
