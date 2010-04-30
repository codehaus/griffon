package griffon.oxbow.factory

import com.ezware.dialog.task.TaskDialog

class TaskDialogFactory extends AbstractFactory {

	public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {

		FactoryBuilderSupport.checkValueIsType(value, name, TaskDialog)
        	String title = attributes.remove("title")
		if( value )
			value.title = title
		else
			value = new TaskDialog(title)
		return value
	}

}
