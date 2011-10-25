import org.viewaframework.swing.*
import org.viewaframework.swing.groovy.*

class ViewaswingGriffonAddon {
 /* New available factories for your views */
	def factories = [
		"masterTable" : new MasterTableFactory(),
		"masterColumn" : new DynamicTableColumnFactory(),
		"masterPaginationController" : new MasterPaginationControllerFactory(),
		"dynamicTable": new DynamicTableFactory(),
		"dynamicTreeTable" : new DynamicTreeTableFactory(),
		"dynamicColumn" : new DynamicTableColumnFactory(),
		"paginationPanel" : new DummyFactory(PaginationPanel),
		"customDatePicker": new NiceDatePickerFactory(),
		"simpleLoginPanel" : new DummyFactory(LoginPanel),
		"simpleLoginTitledPanel" : new DummyFactory(LoginTitledPanel)
	]
}
