/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.jgoodies

/**
 * @author Andres.Almiray
 */
class ChainedDelegate {
    private final List delegates = []

    ChainedDelegate(List delegates) {
        this.delegates.addAll(delegates)
    }

    Object invokeMethod(String name, args) {
        for(target in delegates) {
           try {
               return target.invokeMethod(name, args)
           } catch(MissingMethodException mme) {
               // try next delegate
           }
        }
        // throw MME if we reach this place
        throw new MissingMethodException(name, Object, args)
    }

    Object methodMissing(String name, args) {
        for(target in delegates) {
           try {
               return target.methodMissing(name, args)
           } catch(MissingMethodException mme) {
               // try next delegate
           }
        }
        throw new IllegalArgumentException(new MissingMethodException(name, Object, args).toString())
    }

    Object getProperty(String name) {
        for(target in delegates) {
           try {
               return target.getProperty(name)
           } catch(MissingPropertyException mpe) {
               // try next delegate
           }
        }
        // throw MPE if we reach this place
        throw new MissingPropertyException(name, Object)
    }

    void setProperty(String name, arg) {
        for(target in delegates) {
           try {
               target.setProperty(name, arg)
           } catch(MissingPropertyException mpe) {
               // try next delegate
           }
        }
        // throw MPE if we reach this place
        throw new MissingPropertyException(name, arg? arg.getClass() : Object)
    }

    Object propertyMissing(String name) {
        for(target in delegates) {
           try {
               return target.propertyMissing(name)
           } catch(MissingPropertyException mpe) {
               // try next delegate
           }
        }
        throw new IllegalArgumentException(new MissingPropertyException(name, Object))
    }

    void propertyMissing(String name, arg) {
        for(target in delegates) {
           try {
               target.propertyMissing(name, arg)
           } catch(MissingPropertyException mpe) {
               // try next delegate
           }
        }
        throw new IllegalArgumentException(new MissingPropertyException(name, arg? arg.getClass() : Object))
    }
}
