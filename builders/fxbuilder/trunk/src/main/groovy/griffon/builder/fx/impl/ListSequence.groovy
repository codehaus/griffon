/*
 * Copyright 2008 the original author or authors.
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

package griffon.builder.fx.impl

import com.sun.javafx.runtime.sequence.Sequence
import com.sun.javafx.runtime.sequence.SequencePredicate
import com.sun.javafx.runtime.TypeInfo

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class ListSequence implements Sequence {
    private final List delegate = []

    ListSequence( List init = [] ) {
        delegate.addAll(init)
    } 

    public int size() { delegate.size() }

    public boolean isEmpty() { delegate.empty }

    public TypeInfo getElementType() { null }

    public void toArray( int a, int b, Object[] c, int d) { }

    public Object get( int index ) { delegate[index] }

    public Sequence getSlice( int from, int to) { new ListSequence(delegate[from..to]) }

    public Sequence get( SequencePredicate pred ) { null }

    public int getDepth() { 1 }

    public BitSet getBits( SequencePredicate pred ) { null }

    public Iterator iterator() { delegate.iterator() }

    public Object getDefaultValue() { delegate.size() ? delegate[0] : null }

    public Sequence getEmptySequence() { new ListSequence() }
}
