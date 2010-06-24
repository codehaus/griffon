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
package griffon.domain.metaclass;

import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Andres Almiray
 */
public abstract class AbstractInstanceDynamicMethodInvocation 
                      extends AbstractInstanceMethodInvocation
                      implements DynamicMethodInvocation {
    private final Pattern pattern;
    
    public AbstractInstanceDynamicMethodInvocation(Pattern pattern) {
        this.pattern = pattern;
    }

    protected Pattern getPattern() {
        return pattern;
    }

    public boolean isMethodMatch(String methodName) {
        return this.pattern.matcher(methodName.subSequence(0, methodName.length())).find();
    }

    public String toString() {
        return new ToStringBuilder(this)
                        .append("Pattern",this.pattern)
                        .toString();
    }
}