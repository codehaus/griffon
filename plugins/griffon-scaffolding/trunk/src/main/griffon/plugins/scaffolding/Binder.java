/*
 * Copyright 2010 the original author or authors.
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

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import groovy.lang.Closure;
import groovy.lang.MetaClass;
import groovy.lang.GroovyRuntimeException;
import org.codehaus.groovy.util.HashCodeHelper;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.MethodClosure;
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
    
    public static final String SOURCE = "source";
    public static final String TARGET = "target";
    public static final String IMMEDIATE = "immediate";
    public static final String READER = "reader";
    public static final String WRITER = "writer";
    public static final String REVERSE_READER = "reverseReader";
    public static final String REVERSE_WRITER = "reverseWriter";
    public static final String MUTUAL = "mutual";
    public static final String PROPERTY = "property";
    public static final String BEAN = "bean";
    public static final String MODEL = "model";
    public static final String ON = "on";
    
    private static final Closure DEFAULT_READER = new MethodClosure(Binder.class, "defaultReader");
    private static final Closure DEFAULT_WRITER = new MethodClosure(Binder.class, "defaultWriter");
    private static final String[] DEFAULT_PROPERTIES = new String[]{"value", "text"};
    private static final Map<String, Class> DEFAULT_EVENT_LISTENERS = new HashMap<String, Class>();
    
    static {
         Class[] listenerTypes = new Class[] {
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

         for(Class listenerType : listenerTypes) {
             try {
                 for(Method method : listenerType.getClass().getMethods()) {
                     DEFAULT_EVENT_LISTENERS.put(method.getName(), listenerType);
                 }
             } catch(Exception e) {
                 // ignore
             }
         }
    }
        
    private Binder() {}

    public static <T> Model<T> bind(Model<T> target, Model<T> source) {
        return bind(target, source, true);
    }

    public static <T> Model<T> bind(Model<T> target, Model<T> source, boolean immediate) {
        Map params = new HashMap();
        params.put(SOURCE, source);
        params.put(TARGET, target);
        params.put(IMMEDIATE, immediate);
        return bind(params);
    }

    public static <T> Model<T> bind(Map params) {
        if(params == null) {
            throw new IllegalArgumentException("Cannot create binding with empty parameters!");
        }
        
        Model<T> source = (Model<T>) params.get(SOURCE);
        Model<T> target = (Model<T>) params.get(TARGET);
        
        if(target == null || source == null) return target;
        
        boolean immediate = true;
        Object immediateVal = params.get(IMMEDIATE);
        if(immediateVal != null) {
            immediate = DefaultTypeTransformation.castToBoolean(immediateVal);
        }

        boolean mutual = false;
        Object mutualVal = params.get(MUTUAL);
        if(mutualVal != null) {
            mutual = DefaultTypeTransformation.castToBoolean(mutualVal);
        }

        Closure reader = (Closure) params.get(READER);
        if(reader == null) reader = DEFAULT_READER;
        Closure writer = (Closure) params.get(WRITER);
        if(writer == null) writer = DEFAULT_WRITER;
        Closure reverseReader = (Closure) params.get(REVERSE_READER);
        if(reverseReader == null) reverseReader = DEFAULT_READER;
        Closure reverseWriter = (Closure) params.get(REVERSE_WRITER);
        if(reverseWriter == null) reverseWriter = DEFAULT_WRITER;

        Binding<T> sourceBinding = new Binding(source, reader, reverseWriter);
        Binding<T> targetBinding = new Binding(target, reverseReader, writer);
        BindingUpdater bindingUpdater = target instanceof BeanModel && source instanceof BeanModel ? 
                                            new BeanModelBindingUpdater(sourceBinding, targetBinding, mutual) : 
                                            new BindingUpdater(sourceBinding, targetBinding, mutual);
       
        bindingUpdater.bind();
        if(immediate) bindingUpdater.update(null);
        
        return target;
    }

    public static <T> void beanBind(Map params) {
        if(params == null) {
            throw new IllegalArgumentException("Cannot create binding with empty parameters!");
        }

        Object bean = params.get(BEAN);
        Model<T> model = (Model<T>) params.get(MODEL);
        if(model == null || bean == null) return;

        boolean immediate = true;
        Object immediateVal = params.get(IMMEDIATE);
        if(immediateVal != null) {
            immediate = DefaultTypeTransformation.castToBoolean(immediateVal);
        }

        boolean mutual = false;
        Object mutualVal = params.get(MUTUAL);
        if(mutualVal != null) {
            mutual = DefaultTypeTransformation.castToBoolean(mutualVal);
        }

        PropertyReader beanReader = new DefaultPropertyReader();
        Closure reader = (Closure) params.get(READER);
        if(reader != null) beanReader = new ClosurePropertyReader(reader);
        PropertyWriter beanWriter = new DefaultPropertyWriter();
        Closure writer = (Closure) params.get(WRITER);
        if(writer != null) beanWriter = new ClosurePropertyWriter(writer);

        MetaClass mc = InvokerHelper.getMetaClass(bean);

        String property = (String) params.get(PROPERTY);
        if(property == null) {
            for(String name : DEFAULT_PROPERTIES) {
                if(mc.hasProperty(bean, name) != null) {
                    property = name;
                    break;
                }
            }
            if(property == null) {
                throw new IllegalArgumentException("Must specify a property name!");
            }
        } else if(mc.hasProperty(bean, property) == null) {
            throw new IllegalArgumentException("Bean "+bean+" does not have a property named "+property);
        }

        List<Class> interfaces = new ArrayList<Class>();
        String events = (String) params.get(ON);
        if(events == null) {
            interfaces.add(ActionListener.class);
        } else {
            for(String handler : events.split(" ")) {
                handler = GriffonNameUtils.uncapitalize(handler);
                Class intf = DEFAULT_EVENT_LISTENERS.get(handler);
                if(intf == null) {
                    throw new IllegalArgumentException("Don't know how to handle event "+handler+".");
                }
                if(interfaces.contains(intf)) continue;
                interfaces.add(intf);
            }
        }

        CustomEventHandler eventHandler = new CustomEventHandler(bean, model, property, beanReader, beanWriter);
        Object proxy = Proxy.newProxyInstance(
                           CustomEventHandler.class.getClassLoader(),
                           interfaces.toArray(new Class[interfaces.size()]),
                           eventHandler);
        model.addValueChangeListener(eventHandler);

        for(Class listenerType : interfaces) {
            InvokerHelper.invokeMethod(bean,
                "add"+ GriffonNameUtils.getShortName(listenerType),
                proxy);
        }

        if(immediate) eventHandler.update();
    }

    private static <T> T defaultReader(Model<T> model) {
        return model.getValue();
    }

    private static <T> void defaultWriter(Model<T> model, T value) {
        model.setValue(value);
    }

    private static class CustomEventHandler implements InvocationHandler, PropertyChangeListener {
        private final Object bean;
        private final Model model;
        private final String propertyName;
        private final PropertyReader reader;
        private final PropertyWriter writer;

        public CustomEventHandler(Object bean, Model model, String propertyName, PropertyReader reader, PropertyWriter writer) {
            this.bean = bean;
            this.model = model;
            this.propertyName = propertyName;
            this.reader = reader;
            this.writer = writer;
        }

        public boolean equals(Object obj) {
            if(this == obj) return true;
            if(obj == null) return false;
            
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
System.err.println(e);
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

    public static interface PropertyReader {
        Object read(Object object, String propertyName);
    }

    public static interface PropertyWriter {
        void write(Object object, String propertyName, Object value);
    }
    
    private static class DefaultPropertyReader implements PropertyReader {
        public Object read(Object object, String propertyName) {
            return InvokerHelper.getProperty(object, propertyName);
        }
    }

    private static class ClosurePropertyReader implements PropertyReader {
        private final Closure closure;

        public ClosurePropertyReader(Closure closure) {
            this.closure = closure;
        }

        public Object read(Object object, String propertyName) {
            return closure.call(new Object[]{object, propertyName});
        }  
    }

    private static class DefaultPropertyWriter implements PropertyWriter {
        public void write(Object object, String propertyName, Object value) {
            InvokerHelper.setProperty(object, propertyName, value);
        }
    }

    private static class ClosurePropertyWriter implements PropertyWriter {
        private final Closure closure;

        public ClosurePropertyWriter(Closure closure) {
            this.closure = closure;
        }

        public void write(Object object, String propertyName, Object value) {
            closure.call(new Object[]{object, propertyName, value});
        }
    }

    private static class BasicModel<T> extends ValueObject<T> implements Model<T> {
        private final ModelDescriptor<T> modelDescriptor;
    
        public BasicModel() {
            modelDescriptor = new ModelDescriptor<T>() {
                public Model<T> getModel() { return BasicModel.this; }
                public String getName() { return ""; }
                public String getDisplayName() { return ""; }
                public String getDescription() { return ""; }
            };
        }
    
        public ModelDescriptor<T> getModelDescriptor() {
            return modelDescriptor;
        }

        public final boolean isSameValue(T value) {
            return DefaultTypeTransformation.compareEqual(getValue(), value);
        }
    }

    private static class Binding<T> {
        private static final Logger LOG = LoggerFactory.getLogger(Binding.class);
        protected final Model<T> model;
        protected final Closure reader;
        protected final Closure writer;
        protected final Model<T> test;
        
        public Binding(Model<T> model, Closure reader, Closure writer) {
            this.model = model;
            this.reader = reader;
            this.writer = writer;
            test = new BasicModel();
        }

        public boolean equals(Object obj) {
            if(this == obj) return true;
            if(obj == null) return false;

            if (obj instanceof Binding) {
                Binding other = (Binding) obj;
                
                return model.equals(other.model) &&
                       reader.equals(other.reader) &&
                       writer.equals(other.writer);
            } else {
                return false;
            }
        }

        public int hashCode() {
            int hash = HashCodeHelper.updateHash(HashCodeHelper.initHash(), model);
            hash = HashCodeHelper.updateHash(hash, reader);
            return HashCodeHelper.updateHash(hash, writer);
        }

        public Model<T> getModel() {
            return model;
        }

        public Closure getReader() {
            return reader;
        }
        
        public Closure getWriter() {
            return writer;
        }

        public T read() {
            return (T) reader.call(model);
        }
        
        public void write(T value) {
            writer.call(new Object[]{model, value});
        }

        public T testRead() {
            return (T) reader.call(test);
        }
        
        public void testWrite(T value) {
            writer.call(new Object[]{test, value});
        }

        public boolean isSameValue(T value) {
            testWrite(value);
            return model.isSameValue(testRead());
        }

        public void bind(PropertyChangeListener listener) {
            for(PropertyChangeListener l : model.getValueChangeListeners()) {
                if(listener == l) return;
            }
            model.addValueChangeListener(listener);
        }

        public void unbind(PropertyChangeListener listener) {
            model.removeValueChangeListener(listener);
        }
    }

    private static class BindingUpdater<T> implements PropertyChangeListener {
        private static final Logger LOG = LoggerFactory.getLogger(BindingUpdater.class);

        protected final Binding<T> sourceBinding;
        protected final Binding<T> targetBinding;
        protected final boolean mutual;

        public BindingUpdater(Binding<T> sourceBinding, Binding<T> targetBinding, boolean mutual) {
            this.sourceBinding = sourceBinding;
            this.targetBinding = targetBinding;
            this.mutual = mutual;
        }

        public void bind() {
            sourceBinding.bind(this);
            if(mutual) targetBinding.bind(this);
        }

        public void unbind() {
            sourceBinding.unbind(this);
            if(mutual) targetBinding.unbind(this);
        }

        public Binding<T> getTargetBinding() {
            return targetBinding;
        }

        public Binding<T> getSourceBinding() {
            return sourceBinding;
        }

        public boolean isMutual() {
            return mutual;
        }

        public void propertyChange(PropertyChangeEvent e) {
            handleEvent(e);
        }

        protected void handleEvent(PropertyChangeEvent e) {
            if(e.getSource() == sourceBinding.getModel()) {
                update((T) e.getNewValue());
            } else if(mutual && e.getSource() == targetBinding.getModel()) {
                reverseUpdate((T) e.getNewValue());
            }
        }

        public void update(T value) {
            T unfilteredValue = targetBinding.getModel().getValue();
            T sourceValue = sourceBinding.read();
            if(DefaultTypeTransformation.compareEqual(value, unfilteredValue) || targetBinding.isSameValue(sourceValue)) return;
            if(LOG.isTraceEnabled()) LOG.trace("Updating target with value '"+ sourceValue +"'");
            targetBinding.write(sourceValue);
        }

        public void reverseUpdate(T value) {
            T unfilteredValue = sourceBinding.getModel().getValue();
            T targetValue = targetBinding.read();
            if(DefaultTypeTransformation.compareEqual(value, unfilteredValue) || sourceBinding.isSameValue(targetValue)) return;
            if(LOG.isTraceEnabled()) LOG.trace("Updating source with value '"+ targetValue +"'");
            sourceBinding.write(targetValue);
        }

        public boolean equals(Object obj) {
            if(this == obj) return true;
            if(obj == null) return false;
            if(!(obj instanceof BindingUpdater)) return false;

            BindingUpdater other = (BindingUpdater) obj;
            return targetBinding.equals(other.targetBinding) && 
                   sourceBinding.equals(other.sourceBinding) &&
                   mutual == other.mutual;
        }

        public int hashCode() {
            return targetBinding.hashCode() + sourceBinding.hashCode();
        }
    }

    private static class BeanModelBindingUpdater<T> extends BindingUpdater<T> {
        private static final Logger LOG = LoggerFactory.getLogger(BeanModelBindingUpdater.class);
        private static final Object REF = new Object();

        public BeanModelBindingUpdater(Binding<T> sourceBinding, Binding<T> targetBinding, boolean mutual) {
            super(sourceBinding, targetBinding, mutual);
        }

        public BeanModel<T> getTargetModel() {
            return (BeanModel<T>) targetBinding.getModel();
        }

        public BeanModel<T> getSourceModel() {
            return (BeanModel<T>) sourceBinding.getModel();
        }

        public void bind() {
            super.bind();

            bindToModel(getSourceModel());
            if(mutual) bindToModel(getTargetModel());
        }

        private void bindToModel(BeanModel<T> model) {
            boolean bound = false;
            for(PropertyChangeListener listener : model.getAttributesChangeListeners()) {
                if(listener == this) {
                    bound = true;
                    break;
                }
            }          
            if(!bound) model.addAttributesChangeListener(this);   
        }

        public void unbind() {
            super.unbind();

            getSourceModel().removeAttributesChangeListener(this);
            if(mutual) getTargetModel().removeAttributesChangeListener(this);
        }

        public void propertyChange(PropertyChangeEvent e) {
            if(e instanceof AttributeChangeEvent) {
                handleAttributeEvent((AttributeChangeEvent) e);
            } else {
                handleEvent(e);
            }
        }
        
        public void update(T value) {
            for(String attributeName : getSourceModel().getModelAttributes().keySet()) {
                updateAttribute(attributeName, REF);
            }
        }

        public void reverseUpdate(T value) {
            for(String attributeName : getTargetModel().getModelAttributes().keySet()) {
                reverseUpdateAttribute(attributeName, REF);
            }
        }

        protected void handleAttributeEvent(AttributeChangeEvent e) {
            if(e.getBeanModel() == sourceBinding.getModel()) {
                updateAttribute(e.getPropertyName(), e.getNewValue());
            } else if(mutual && e.getBeanModel() == targetBinding.getModel()) {
                reverseUpdateAttribute(e.getPropertyName(), e.getNewValue());
            }
        }

        public void updateAttribute(String attributeName, Object value) {
            Model targetAttribute = getSourceModel().getModelAttributes().get(attributeName);
            Model sourceAttribute = getTargetModel().getModelAttributes().get(attributeName);

            // Object unfilteredValue = targetAttribute.getValue();
            // if(value == REF) value = sourceAttribute.getValue();
            // if(DefaultTypeTransformation.compareEqual(value, unfilteredValue)) return;

            Object sourceValue = sourceBinding.getReader().call(sourceAttribute);
            Object targetValue = targetBinding.getReader().call(targetAttribute);
            if(DefaultTypeTransformation.compareEqual(sourceValue, targetValue)) return;
            targetBinding.getWriter().call(new Object[]{targetAttribute, sourceValue});
        }
        
        public void reverseUpdateAttribute(String attributeName, Object value) {
            Model targetAttribute = getSourceModel().getModelAttributes().get(attributeName);
            Model sourceAttribute = getTargetModel().getModelAttributes().get(attributeName);

            // Object unfilteredValue = sourceAttribute.getValue();
            // if(value == REF) value = targetAttribute.getValue();
            // if(DefaultTypeTransformation.compareEqual(value, unfilteredValue)) return;

            Object sourceValue = sourceBinding.getReader().call(sourceAttribute);
            Object targetValue = targetBinding.getReader().call(targetAttribute);
            if(DefaultTypeTransformation.compareEqual(sourceValue, targetValue)) return;
            sourceBinding.getWriter().call(new Object[]{sourceAttribute, targetValue});
        }
    }
}
