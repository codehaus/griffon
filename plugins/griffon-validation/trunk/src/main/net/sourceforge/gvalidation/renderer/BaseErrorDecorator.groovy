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

package net.sourceforge.gvalidation.renderer

import org.springframework.context.MessageSource
import net.sourceforge.gvalidation.Errors
import net.sourceforge.gvalidation.FieldError
import net.sourceforge.gvalidation.ErrorListener

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
abstract class BaseErrorDecorator implements ErrorDecorator, ErrorListener {

    protected Object model
    protected MessageSource messageSource
    protected String errorField
    protected Object targetComponent

    public void register(model, node, errorField, messageSource){
        this.model = model
        this.targetComponent = node
        this.errorField = errorField
        this.messageSource = messageSource

        this.model.errors.addListener(this)
    }

    public boolean isRegistered() {
        return model != null
    }

    public String getErrorField() {
        return errorField
    }

    public Object getModel() {
        return model
    }

    public MessageSource getMessageSource() {
        return messageSource
    }

    public Object getTargetComponent() {
        return targetComponent
    }

    abstract protected void decorate(Errors errors, FieldError fieldError)

    abstract protected void undecorate()

    def onFieldErrorAdded(FieldError error) {
        if(isNotRelatedError(error))
            return null

        return decorate(model.errors, error)
    }

    def onFieldErrorRemoved(FieldError error) {
        if(isNotRelatedError(error))
            return null

        return undecorate()
    }

    private boolean isNotRelatedError(FieldError error) {
        return error.field != errorField
    }
}
