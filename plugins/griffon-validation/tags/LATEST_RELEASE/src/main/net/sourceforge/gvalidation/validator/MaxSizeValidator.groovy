/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class MaxSizeValidator extends Closure {

    def MaxSizeValidator(owner) {
        super(owner);
    }

    def doCall(property, bean, maxSize) {
        if(!property)
            return true
        
        def valid = false

        if (maxSize) {
            def actualSize = 0

            property.each { actualSize++ }

            valid = maxSize >= actualSize
        }

        return valid
    }

}