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
class ListSequence implements List, Sequence {
   private final List delegate = []
   private final TypeInfo typeInfo

   ListSequence( List init = [], TypeInfo typeInfo = TypeInfo.Object ) {
      delegate.addAll(init)
      this.typeInfo = typeInfo
   }

   public int size() { delegate.size() }

   public boolean isEmpty() { delegate.empty }

   public TypeInfo getElementType() { typeInfo }

   public void toArray( int a, int b, Object[] c, int d) { }

   public Object get( int index ) { delegate[index] }

   public Sequence getSlice( int from, int to) { new ListSequence(delegate[from..to]) }

   public Sequence get( SequencePredicate pred ) { null }

   public int getDepth() { 1 }

   public BitSet getBits( SequencePredicate pred ) { null }

   public Iterator iterator() { delegate.iterator() }

   public Object getDefaultValue() { delegate.size() ? delegate[0] : null }

   public Sequence getEmptySequence() { new ListSequence() }

   public void add( int index, Object element ) { delegate.add( index, element ) }

   public boolean add( Object e ) { delegate.add( e ) }

   public boolean addAll( Collection c ) { delegate.addAll( c ) }

   public boolean addAll( int index, Collection c ) { delegate.addAll( index, c ) }

   public void clear() { delegate.clear() }

   public boolean contains( Object o ) { delegate.contains( o ) }

   public boolean containsAll( Collection c ) { delegate.containsAll( c ) }

   public boolean equals( Object o ) { delegate.equals( o ) }

   public int hashCode() { delegate.hashCode() }

   public int indexOf( Object o ) { delegate.indexOf( o ) }

   public int lastIndexOf( Object o ) { delegate.lastIndexOf( o ) }

   public ListIterator listIterator() { delegate.listIterator() }

   public ListIterator listIterator( int index ) { delegate.listIterator( index ) }

   public Object remove( int index ) { elegate.remove( index ) }

   public boolean remove( Object o ) { delegate.remove( o ) }

   public boolean removeAll( Collection c ) { delegate.removeAll( c ) }

   public boolean retainAll( Collection c ) { delegate.retainAll( c ) }

   public Object set( int index, Object element ) {delegate.set( index, element ) }

   public List subList( int fromIndex, int toIndex ) {delegate.subList( fromIndex, toIndex ) }

   public Object[] toArray() { delegate.toArray() }

   public Object[] toArray( Object[] a ) { delegate.toArray( a ) }
}