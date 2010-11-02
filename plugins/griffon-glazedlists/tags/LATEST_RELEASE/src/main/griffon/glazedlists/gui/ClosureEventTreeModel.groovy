package griffon.glazedlists.gui

import ca.odell.glazedlists.swing.EventTreeModel
import javax.swing.event.TreeModelEvent
import javax.swing.event.TreeModelListener
import javax.swing.tree.TreePath

/**
 * Created by IntelliJ IDEA.
 * User: aklein
 * Date: 13.10.2010
 * Time: 14:52:49
 * To change this template use File | Settings | File Templates.
 */
class ClosureEventTreeModel extends EventTreeModel {
  Closure update

  ClosureEventTreeModel(def source, Closure c = null) {
    super(source)
    update = c
  }

  def void valueForPathChanged(TreePath path, Object newValue) {
    if (update)
      update.call(path, path.lastPathComponent, newValue)
    else
      super.valueForPathChanged(path, newValue)
    listenerList.each{listener ->
      listener.treeNodesChanged(new TreeModelEvent(this, path))
    }
  }
}
