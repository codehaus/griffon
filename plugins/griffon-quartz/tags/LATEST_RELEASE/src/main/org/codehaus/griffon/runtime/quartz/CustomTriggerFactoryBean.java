/* 
 * Copyright 2004-2010 the original author or authors.
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
package org.codehaus.griffon.runtime.quartz;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.util.Map;
import java.util.Date;
import java.text.ParseException;
import java.beans.PropertyEditorSupport;

/**
 * @author Sergey Nebolsin (Grails 0.3)
 */
public class CustomTriggerFactoryBean implements FactoryBean, InitializingBean  {
  private Class<Trigger> triggerClass;
  private Trigger customTrigger;
  private JobDetail jobDetail;

  private Map triggerAttributes;

  public void afterPropertiesSet() throws ParseException {
      customTrigger = BeanUtils.instantiateClass(triggerClass);

      if(triggerAttributes.containsKey(QuartzConstants.START_DELAY)) {
          Number startDelay = (Number) triggerAttributes.remove(QuartzConstants.START_DELAY);
          customTrigger.setStartTime(new Date(System.currentTimeMillis() + startDelay.longValue()));
      }

      if (jobDetail != null) {
          customTrigger.setJobName(jobDetail.getName());
          customTrigger.setJobGroup(jobDetail.getGroup());
      }

      BeanWrapper customTriggerWrapper = PropertyAccessorFactory.forBeanPropertyAccess(customTrigger);
      customTriggerWrapper.registerCustomEditor(String.class, new StringEditor());
      customTriggerWrapper.setPropertyValues(triggerAttributes);
  }

  public Object getObject() throws Exception {
      return customTrigger;
  }

  public Class getObjectType() {
      return triggerClass;
  }

  public boolean isSingleton() {
      return true;
  }

  public void setJobDetail(JobDetail jobDetail) {
      this.jobDetail = jobDetail;
  }

  public void setTriggerClass(Class<Trigger> triggerClass) {
    this.triggerClass = triggerClass;
  }

  public void setTriggerAttributes(Map triggerAttributes) {
    this.triggerAttributes = triggerAttributes;
  }
}

// We need this additional editor to support GString -> String convertion for trigger's properties.
class StringEditor extends PropertyEditorSupport {
    @Override
    public void setValue(Object value) {
        super.setValue(value == null ? null : value.toString());
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text);
    }
}
