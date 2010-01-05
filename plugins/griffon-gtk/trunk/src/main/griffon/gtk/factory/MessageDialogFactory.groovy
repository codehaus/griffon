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

package griffon.gtk.factory

import org.gnome.gtk.MessageDialog
import org.gnome.gtk.MessageType
import org.gnome.gtk.ButtonsType
import org.gnome.gtk.Window

/**
 * @author Andres Almiray
 */
class MessageDialogFactory extends ContainerFactory {
    private static final Class[] PARAMS = [Window, String, String] as Class[]
    private final Class dialogClass

    MessageDialogFactory() {
        this(null)
    }

    MessageDialogFactory(Class dialogClass) {
        super(dialogClass ?: MessageDialog)
        this.dialogClass = dialogClass
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
           throws InstantiationException, IllegalAccessException {
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, beanClass)) {
            return value
        }

        dialogClass ? handleSpecializedMessageDialog(builder, name, value, attributes) : handleStandardMessageDialog(builder, name, value, attributes)
    }

    private Object handleSpecializedMessageDialog(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        Window parent = attributes.remove('parent') ?: null
        String primary = (attributes.remove('primary') ?: '' ).toString()
        String secondary = (attributes.remove('secondary') ?: '' ).toString()
        dialogClass.getDeclaredConstructor(PARAMS).newInstance([parent, primary, secondary] as Object[])
    }

    private Object handleStandardMessageDialog(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        Window parent = attributes.remove('parent') ?: null
        def modal = attributes.remove('modal')
        MessageType messageType = parseMessageType(attributes.remove('messageType') ?: MessageType.OTHER)
        ButtonsType buttonsType = parseButtonsType(attributes.remove('buttonsType') ?: ButtonsType.CLOSE)
        String message = attributes.remove('message').toString()

        new MessageDialog(parent, modal ?: false, messageType, buttonsType, message)
    }

    private MessageType parseMessageType(messageType) {
        if(messageType instanceof MessageType) return messageType
        switch(messageType) {
            case ~/(?i:error)/: return MessageType.ERROR
            case ~/(?i:info)/: return MessageType.INFO
            case ~/(?i:question)/: return MessageType.QUESTION
            case ~/(?i:warning)/: return MessageType.WARNING
            case ~/(?i:other)/: return MessageType.OTHER
        }
        return MessageType.OTHER
    }

    private ButtonsType parseButtonsType(buttonsType) {
        if(buttonsType instanceof ButtonsType) return buttonsType
        switch(buttonsType) {
            case ~/(?i:cancel)/: return ButtonsType.CANCEL
            case ~/(?i:close)/: return ButtonsType.CLOSE
            case ~/(?i:none)/: return ButtonsType.NONE
            case ~/(?i:ok)/: return ButtonsType.OK
            case ~/(?i:ok_cancel)/: return ButtonsType.OK_CANCEL
            case ~/(?i:yes_no)/: return ButtonsType.YES_NO
        }
        return ButtonsType.CLOSE
    }
}
