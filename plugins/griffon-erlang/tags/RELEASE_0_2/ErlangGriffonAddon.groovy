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

import com.ericsson.otp.erlang.*

/**
 * @author Andres Almiray
 */
class ErlangGriffonAddon {
    def addonInit(app) {
        OtpErlangTuple.metaClass.with {
            getAt = { int index -> delegate.elementAt(index) }
            iterator = {-> [
                array: delegate.elements(),
                index: 0,
                hasNext: {-> index < array.length },
                next: {-> array[index++] },
                remove: {-> throw new UnsupportedOperationException("OtpErlangTuple is immutable.") }
            ] as Iterator}
        }

        Character.metaClass.toErlang = {-> new OtpErlangChar(delegate.charValue()) }
        Character.TYPE.metaClass.toErlang = {-> new OtpErlangChar(delegate) }
        Boolean.metaClass.toErlang = {-> new OtpErlangBoolean(delegate.booleanValue()) }
        Boolean.TYPE.metaClass.toErlang = {-> new OtpErlangBoolean(delegate) }
        Byte.metaClass.toErlang = {-> new OtpErlangByte(delegate.byteValue()) }
        Byte.TYPE.metaClass.toErlang = {-> new OtpErlangByte(delegate) }
        Short.metaClass.toErlang = {-> new OtpErlangShort(delegate.shortValue()) }
        Short.TYPE.metaClass.toErlang = {-> new OtpErlangShort(delegate) }
        Integer.metaClass.toErlang = {-> new OtpErlangInt(delegate.intValue()) }
        Integer.TYPE.metaClass.toErlang = {-> new OtpErlangInt(delegate) }
        Long.metaClass.toErlang = {-> new OtpErlangLong(delegate.longValue()) }
        Long.TYPE.metaClass.toErlang = {-> new OtpErlangLong(delegate) }
        Float.metaClass.toErlang = {-> new OtpErlangFloat(delegate.floatValue()) }
        Float.TYPE.metaClass.toErlang = {-> new OtpErlangFloat(delegate) }
        Double.metaClass.toErlang = {-> new OtpErlangDouble(delegate.doubleValue()) }
        Double.TYPE.metaClass.toErlang = {-> new OtpErlangDouble(delegate) }
        BigDecimal.metaClass.toErlang = {-> new OtpErlangDouble(delegate.doubleValue()) }
        BigInteger.metaClass.toErlang = {-> new OtpErlangDouble(delegate.longValue()) }
        String.metaClass.toErlang = {-> new OtpErlangString(delegate) }
        String.metaClass.toErlangAtom = {-> new OtpErlangAtom(delegate) }

        List.metaClass.with {
            toErlang = {->
                List array = []
                delegate.each { e ->
                    array << (e instanceof OtpErlangObject ? e : e.toErlang())
                }
                array as OtpErlangObject[]
            }
            toErlangTuple = {->
                new OtpErlangTuple(delegate.toErlang())
            }
        }
    }
}
