package griffon.builder.abeilleform.factory

import com.jeta.forms.gui.form.FormAccessor
import com.jeta.forms.components.panel.FormPanel

/**
 * @author Jim Shingler <shinglerjim@gmail.com>
 */

public class FormPanelFactory extends AbstractFactory {

  public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
  throws InstantiationException, IllegalAccessException {
    def fp = new FormPanel(value)

    // Index the form fields
    //fp.getFormAccessor(formName).beanIterator(true).each {c ->
    fp.getFormAccessor().beanIterator(true).each {c ->
      if (c.name) {
        builder.noparent {
          builder.bean(c, id: c.name)
        }
      }
    }

    return fp
  }

  public boolean isLeaf() {
    return true
  }
}
