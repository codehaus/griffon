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

package griffon.pivot.adapters
 
import griffon.pivot.impl.BuilderDelegate
import org.apache.pivot.wtk.ImageView
import org.apache.pivot.wtk.media.Image
import org.apache.pivot.wtk.ImageViewListener

/**
 * @author Andres Almiray
 */
class ImageViewListenerAdapter extends BuilderDelegate implements ImageViewListener {
    private Closure onImageChanged
    private Closure onAsynchronousChanged
    private Closure onImageKeyChanged
 
    ImageViewListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onImageChanged(Closure callback) {
        onImageChanged = callback
        onImageChanged.delegate = this
    }

    void onAsynchronousChanged(Closure callback) {
        onAsynchronousChanged = callback
        onAsynchronousChanged.delegate = this
    }

    void onImageKeyChanged(Closure callback) {
        onImageKeyChanged = callback
        onImageKeyChanged.delegate = this
    }

    void imageChanged(ImageView arg0, Image arg1) {
        if(onImageChanged) onImageChanged(arg0, arg1)
    }

    void asynchronousChanged(ImageView arg0) {
        if(onAsynchronousChanged) onAsynchronousChanged(arg0)
    }

    void imageKeyChanged(ImageView arg0, String arg1) {
        if(onImageKeyChanged) onImageKeyChanged(arg0, arg1)
    }
}