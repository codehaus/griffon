package griffon.presentation

import java.beans.Introspector
import java.beans.BeanDescriptor
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import groovy.beans.Bindable

import griffon.util.ApplicationHolder
import griffon.util.GriffonClassUtils

class BeanModelReference<T> extends BeanModel<T> {
    private final Class<BeanModel<T>> beanModelClass
    private final Map<String, Model<?>> attributes = new LinkedHashMap<String, Model<?>>()
    private final BeanModelResolver beanModelResolver = new BeanModelResolver()

    private BeanModel<T> beanModelDelegate
    private boolean resolved = false

    BeanModelReference(Class<T> wrappedClass, Class<BeanModel<T>> beanModelClass) {
        super(wrappedClass)
        this.beanModelClass = beanModelClass
    }

    synchronized boolean isReferenceResolved() {
        resolved
    }

    protected void setupListeners() {
        addValueChangeListener(new BeanModelResolver())
        ApplicationHolder.application?.locale?.addPropertyChangeListener(new I18nUpdater())
    }

    protected class BeanModelResolver implements PropertyChangeListener {
        void propertyChange(PropertyChangeEvent evt) {
            if(evt.newValue != null) {
                resolveBeanModel()
            }
        }
    }

    protected synchronized void resolveBeanModel() {
        beanModelDelegate = beanModelClass.newInstance()

        removeValueChangeListener(beanModelResolver)
        addValueChangeListener(new BeanChangedUpdater())

        resolved = true
    }

    String toString() {
        String desc = [
            'name=', modelName,
            ', description=', modelDescription,
            ', lazy=true'
         ].join('')
        '[' + desc + ']'
    }
}
