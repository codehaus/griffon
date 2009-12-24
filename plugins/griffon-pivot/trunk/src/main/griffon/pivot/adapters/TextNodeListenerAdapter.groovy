/*
 * Copyright 2009 the original author or authors.
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

package griffon.pivot.adapters
 
import griffon.pivot.impl.BuilderDelegate
import org.apache.pivot.wtk.text.TextNode
import org.apache.pivot.wtk.text.TextNodeListener

/**
 * @author Andres Almiray
 */
class TextNodeListenerAdapter extends BuilderDelegate implements TextNodeListener {
    private Closure onCharactersInserted
    private Closure onCharactersRemoved
 
    TextNodeListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onCharactersInserted(Closure callback) {
        onCharactersInserted = callback
        onCharactersInserted.delegate = this
    }

    void onCharactersRemoved(Closure callback) {
        onCharactersRemoved = callback
        onCharactersRemoved.delegate = this
    }

    void charactersInserted(TextNode arg0, int arg1, int arg2) {
        if(onCharactersInserted) onCharactersInserted(arg0, arg1, arg2)
    }

    void charactersRemoved(TextNode arg0, int arg1, String arg2) {
        if(onCharactersRemoved) onCharactersRemoved(arg0, arg1, arg2)
    }
}