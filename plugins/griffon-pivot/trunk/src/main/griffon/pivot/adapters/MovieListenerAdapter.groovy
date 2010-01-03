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
import org.apache.pivot.wtk.media.Movie
import org.apache.pivot.wtk.media.MovieListener

/**
 * @author Andres Almiray
 */
class MovieListenerAdapter extends BuilderDelegate implements MovieListener {
    private Closure onBaselineChanged
    private Closure onCurrentFrameChanged
    private Closure onLoopingChanged
    private Closure onMovieStarted
    private Closure onMovieStopped
    private Closure onRegionUpdated
    private Closure onSizeChanged
 
    MovieListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onBaselineChanged(Closure callback) {
        onBaselineChanged = callback
        onBaselineChanged.delegate = this
    }

    void onCurrentFrameChanged(Closure callback) {
        onCurrentFrameChanged = callback
        onCurrentFrameChanged.delegate = this
    }

    void onLoopingChanged(Closure callback) {
        onLoopingChanged = callback
        onLoopingChanged.delegate = this
    }

    void onMovieStarted(Closure callback) {
        onMovieStarted = callback
        onMovieStarted.delegate = this
    }

    void onMovieStopped(Closure callback) {
        onMovieStopped = callback
        onMovieStopped.delegate = this
    }

    void onRegionUpdated(Closure callback) {
        onRegionUpdated = callback
        onRegionUpdated.delegate = this
    }

    void onSizeChanged(Closure callback) {
        onSizeChanged = callback
        onSizeChanged.delegate = this
    }

    void baselineChanged(Movie arg0, int arg1) {
        if(onBaselineChanged) onBaselineChanged(arg0, arg1)
    }

    void currentFrameChanged(Movie arg0, int arg1) {
        if(onCurrentFrameChanged) onCurrentFrameChanged(arg0, arg1)
    }

    void loopingChanged(Movie arg0) {
        if(onLoopingChanged) onLoopingChanged(arg0)
    }

    void movieStarted(Movie arg0) {
        if(onMovieStarted) onMovieStarted(arg0)
    }

    void movieStopped(Movie arg0) {
        if(onMovieStopped) onMovieStopped(arg0)
    }

    void regionUpdated(Movie arg0, int arg1, int arg2, int arg3, int arg4) {
        if(onRegionUpdated) onRegionUpdated(arg0, arg1, arg2, arg3, arg4)
    }

    void sizeChanged(Movie arg0, int arg1, int arg2) {
        if(onSizeChanged) onSizeChanged(arg0, arg1, arg2)
    }
}