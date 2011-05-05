/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.builder.trident.impl

import org.pushingpixels.trident.Timeline
import org.pushingpixels.trident.TimelinePropertyBuilder
import org.pushingpixels.trident.TimelinePropertyBuilder.PropertyGetter
import org.pushingpixels.trident.TimelinePropertyBuilder.PropertySetter
import org.pushingpixels.trident.TimelinePropertyBuilder.PropertyAccessor
import org.pushingpixels.trident.interpolator.PropertyInterpolator
import org.pushingpixels.trident.interpolator.KeyFrames

/**
 * @author Andres Almiray
 */
class InterpolatedProperty {
	private final TimelinePropertyBuilder propertyBuilder

    InterpolatedProperty(String property) {
        propertyBuilder = new TimelinePropertyBuilder(property)
    }

    void setFrom(from) {
	    if(from != null) propertyBuilder.from(from)
    }

    void setFromCurrent(boolean fromCurrent) {
	    if(fromCurrent) propertyBuilder.fromCurrent()
    }

    void setTo(to) {
	     if(to != null) propertyBuilder.to(to)
    }

    void setOn(target) {
	    if(on) propertyBuilder.on(target)
    }

    void setInterpolator(PropertyInterpolator interpolator) {
	    if(interpolator) propertyBuilder.interpolateWith(interpolator)
    }

    void setSet(setter) {
	    if(setter instanceof PropertySetter) {
		    propertyBuilder.setWith(setter)
		} else if(setter instanceof Closure) {
			propertyBuilder.setWith(setter as PropertySetter)
		}
    }
	
    void setGet(getter) {
	    if(getter instanceof PropertyGetter) {
		    propertyBuilder.getWith(getter)
		} else if(getter instanceof Closure) {
			propertyBuilder.getWith(getter as PropertySetter)
		}
    }
	
    void setAccess(access) {
	    if(access instanceof PropertyAccessor) {
		    propertyBuilder.setWith(access)
		} else if(access instanceof Map) {
			propertyBuilder.accessWith(access as PropertyAccessor)
		}
    }

    void setKeyFrames(KeyFrames keyFrames) {
	    if(keyFrames) propertyBuilder.goingThrough(keyFrames)
    }

    void addToTimeline(Timeline timeline) {
        timeline.addPropertyToInterpolate(propertyBuilder)
    }
}