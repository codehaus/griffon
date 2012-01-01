/*
 * Copyright 2010-2011 the original author or authors.
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

package griffon.plugins.scaffolding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.*;

import java.util.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.lang.GroovyRuntimeException;
import org.codehaus.griffon.runtime.scaffolding.BeanModelBindingUpdater;
import org.codehaus.griffon.runtime.scaffolding.BindingUpdater;
import org.codehaus.groovy.util.HashCodeHelper;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

import griffon.util.GriffonNameUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andres Almiray
 */
public final class Binder {
    private static final Logger LOG = LoggerFactory.getLogger(Binder.class);

    private static final String[] DEFAULT_PROPERTIES = new String[]{"value", "text"};
    private static final Map<String, Class> DEFAULT_EVENT_LISTENERS = new HashMap<String, Class>();

    static {
        Class[] listenerTypes = new Class[]{
                PropertyChangeListener.class,
                ActionListener.class,
                FocusListener.class,
                MouseListener.class,
                MouseMotionListener.class,
                MouseWheelListener.class,
                AdjustmentListener.class,
                ItemListener.class,
                KeyListener.class,
                TextListener.class
        };

        for (Class listenerType : listenerTypes) {
            try {
                for (Method method : listenerType.getClass().getMethods()) {
                    DEFAULT_EVENT_LISTENERS.put(method.getName(), listenerType);
                }
            } catch (Exception e) {
                // ignore
            }
        }
    }

    private Binder() {
    }

    public static BindingBuilder make() {
        return new BindingBuilder();
    }

    public static <T> void make(Model<T> target, Model<T> source) {
        make(target, source, true);
    }

    public static <T> void make(Model<T> target, Model<T> source, boolean immediate) {
        new BindingBuilder()
                .withSource(source)
                .withTarget(target)
                .immediate(immediate)
                .make();
    }

    public static <T> void make(Map<String, Object> params) {
        if (params == null) {
            throw new IllegalArgumentException("Cannot create binding with empty parameters!");
        }

        Model<T> source = (Model<T>) params.get(Binding.SOURCE);
        Model<T> target = (Model<T>) params.get(Binding.TARGET);

        if (target == null || source == null) return;

        boolean immediate = true;
        Object immediateVal = params.get(Binding.IMMEDIATE);
        if (immediateVal != null) {
            immediate = DefaultTypeTransformation.castToBoolean(immediateVal);
        }

        boolean mutual = false;
        Object mutualVal = params.get(Binding.MUTUAL);
        if (mutualVal != null) {
            mutual = DefaultTypeTransformation.castToBoolean(mutualVal);
        }

        Binding.PropertyReader reader = fetchReader(params, Binding.READER);
        Binding.PropertyWriter writer = fetchWriter(params, Binding.WRITER);
        Binding.PropertyReader reverseReader = fetchReader(params, Binding.REVERSE_READER);
        Binding.PropertyWriter reverseWriter = fetchWriter(params, Binding.REVERSE_WRITER);

        Binding<T> sourceBinding = new Binding(source, reader, reverseWriter);
        Binding<T> targetBinding = new Binding(target, reverseReader, writer);
        BindingUpdater bindingUpdater = target instanceof BeanModel && source instanceof BeanModel ?
                new BeanModelBindingUpdater(sourceBinding, targetBinding, mutual) :
                new BindingUpdater(sourceBinding, targetBinding, mutual);

        bindingUpdater.bind();
        if (immediate) bindingUpdater.update(null);
    }

    private static Binding.PropertyReader fetchReader(Map<String, Object> map, String propertyName) {
        Object reader = map.get(propertyName);
        if (reader instanceof Closure) return new ClosurePropertyReader((Closure) reader);
        if (reader instanceof Binding.PropertyReader) return (Binding.PropertyReader) reader;
        return Binding.DEFAULT_READER;
    }

    private static Binding.PropertyWriter fetchWriter(Map<String, Object> map, String propertyName) {
        Object writer = map.get(propertyName);
        if (writer instanceof Closure) return new ClosurePropertyWriter((Closure) writer);
        if (writer instanceof Binding.PropertyReader) return (Binding.PropertyWriter) writer;
        return Binding.DEFAULT_WRITER;
    }

    public static <T> void beanBind(Map params) {
        if (params == null) {
            throw new IllegalArgumentException("Cannot create binding with empty parameters!");
        }

        Object bean = params.get(Binding.TARGET);
        Model<T> model = (Model<T>) params.get(Binding.SOURCE);
        if (model == null || bean == null) return;

        boolean immediate = true;
        Object immediateVal = params.get(Binding.IMMEDIATE);
        if (immediateVal != null) {
            immediate = DefaultTypeTransformation.castToBoolean(immediateVal);
        }

        boolean mutual = false;
        Object mutualVal = params.get(Binding.MUTUAL);
        if (mutualVal != null) {
            mutual = DefaultTypeTransformation.castToBoolean(mutualVal);
        }

        Binding.PropertyReader beanReader = fetchReader(params, Binding.READER);
        Binding.PropertyWriter beanWriter = fetchWriter(params, Binding.WRITER);

        MetaClass mc = InvokerHelper.getMetaClass(bean);

        String property = (String) params.get(Binding.PROPERTY);
        if (property == null) {
            for (String name : DEFAULT_PROPERTIES) {
                if (mc.hasProperty(bean, name) != null) {
                    property = name;
                    break;
                }
            }
            if (property == null) {
                throw new IllegalArgumentException("Must specify a property name!");
            }
        } else if (mc.hasProperty(bean, property) == null) {
            throw new IllegalArgumentException("Bean " + bean + " does not have a property named " + property);
        }

        List<Class> interfaces = new ArrayList<Class>();
        String events = (String) params.get(Binding.ON);
        if (events == null) {
            interfaces.add(ActionListener.class);
        } else {
            for (String handler : events.split(" ")) {
                handler = GriffonNameUtils.uncapitalize(handler);
                Class intf = DEFAULT_EVENT_LISTENERS.get(handler);
                if (intf == null) {
                    throw new IllegalArgumentException("Don't know how to handle event " + handler + ".");
                }
                if (interfaces.contains(intf)) continue;
                interfaces.add(intf);
            }
        }

        CustomEventHandler eventHandler = new CustomEventHandler(bean, model, property, beanReader, beanWriter);
        Object proxy = Proxy.newProxyInstance(
                CustomEventHandler.class.getClassLoader(),
                interfaces.toArray(new Class[interfaces.size()]),
                eventHandler);
        model.addValueChangeListener(eventHandler);

        for (Class listenerType : interfaces) {
            InvokerHelper.invokeMethod(bean,
                    "add" + GriffonNameUtils.getShortName(listenerType),
                    proxy);
        }

        if (immediate) eventHandler.update();
    }

    private static class CustomEventHandler implements InvocationHandler, PropertyChangeListener {
        private final Object bean;
        private final Model model;
        private final String propertyName;
        private final Binding.PropertyReader reader;
        private final Binding.PropertyWriter writer;

        public CustomEventHandler(Object bean, Model model, String propertyName, Binding.PropertyReader reader, Binding.PropertyWriter writer) {
            this.bean = bean;
            this.model = model;
            this.propertyName = propertyName;
            this.reader = reader;
            this.writer = writer;
        }

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;

            if (obj instanceof Proxy) {
                obj = Proxy.getInvocationHandler(obj);
            }

            if (obj instanceof CustomEventHandler) {
                CustomEventHandler other = (CustomEventHandler) obj;

                return model.equals(other.model) &&
                        bean.equals(other.bean) &&
                        propertyName.equals(other.propertyName);
            } else {
                return false;
            }
        }

        public int hashCode() {
            int hash = HashCodeHelper.updateHash(HashCodeHelper.initHash(), model);
            hash = HashCodeHelper.updateHash(hash, bean);
            return HashCodeHelper.updateHash(hash, propertyName);
        }

        public String toString() {
            return new StringBuilder("CustomEventHandler[")
                    .append(model).append(", ")
                    .append(propertyName).append(", ")
                    .append(bean)
                    .append("]").toString();
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (!checkMethod(method)) {
                try {
                    handleEvent();
                    return null;
                } catch (GroovyRuntimeException gre) {
                    throw ScriptBytecodeAdapter.unwrap(gre);
                }
            }
            try {
                return method.invoke(this, args);
            } catch (InvocationTargetException ite) {
                throw ite.getTargetException();
            }
        }

        public void propertyChange(PropertyChangeEvent e) {
            update();
        }

        public void update() {
            writer.write(bean, propertyName, model.getValue());
        }

        private void handleEvent() {
            model.setValue(reader.read(bean, propertyName));
        }

        private boolean checkMethod(Method method) {
            return Object.class.equals(method.getDeclaringClass()) ||
                    PropertyChangeListener.class.equals(method.getDeclaringClass());
        }
    }

    private static class ClosurePropertyReader implements Binding.PropertyReader {
        private final Closure closure;

        public ClosurePropertyReader(Closure closure) {
            this.closure = closure;
        }

        public Object read(Object object, String propertyName) {
            return closure.call(new Object[]{object, propertyName});
        }
    }

    private static class ClosurePropertyWriter implements Binding.PropertyWriter {
        private final Closure closure;

        public ClosurePropertyWriter(Closure closure) {
            this.closure = closure;
        }

        public void write(Object object, String propertyName, Object value) {
            closure.call(new Object[]{object, propertyName, value});
        }
    }

    /**
     * @author Andres Almiray
     */
    public static class BindingBuilder {
        private Map<String, Object> args = new LinkedHashMap<String, Object>();

        public BindingBuilder withSource(Object source) {
            args.put(Binding.SOURCE, source);
            return this;
        }

        public BindingBuilder withTarget(Object target) {
            args.put(Binding.TARGET, target);
            return this;
        }

        public BindingBuilder withProperty(String property) {
            args.put(Binding.PROPERTY, property);
            return this;
        }

        public BindingBuilder immediate(boolean immediate) {
            args.put(Binding.IMMEDIATE, immediate);
            return this;
        }

        public BindingBuilder mutual(boolean mutual) {
            args.put(Binding.MUTUAL, mutual);
            return this;
        }

        public BindingBuilder withReader(Binding.PropertyReader reader) {
            args.put(Binding.READER, reader);
            return this;
        }

        public BindingBuilder withReader(Closure reader) {
            return withReader(new ClosurePropertyReader(reader));
        }

        public BindingBuilder withWriter(Binding.PropertyWriter writer) {
            args.put(Binding.WRITER, writer);
            return this;
        }

        public BindingBuilder withWriter(Closure writer) {
            return withWriter(new ClosurePropertyWriter(writer));
        }

        public BindingBuilder withReverseReader(Binding.PropertyReader reader) {
            args.put(Binding.REVERSE_READER, reader);
            return this;
        }

        public BindingBuilder withReverseReader(Closure reader) {
            return withReverseReader(new ClosurePropertyReader(reader));
        }

        public BindingBuilder withReverseWriter(Binding.PropertyWriter writer) {
            args.put(Binding.REVERSE_WRITER, writer);
            return this;
        }

        public BindingBuilder withReverseWriter(Closure writer) {
            return withReverseWriter(new ClosurePropertyWriter(writer));
        }

        public BindingBuilder on(String event) {
            return on(new String[]{event});
        }

        public BindingBuilder on(String[] events) {
            args.put(Binding.ON, events);
            return this;
        }

        public void make() {
            Binder.make(args);
        }
    }
}