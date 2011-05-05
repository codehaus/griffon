// Groovy source file
package groovy.swingx.demo
import java.awt.Color
import groovy.swing.SwingXBuilder
import javax.swing.WindowConstants
import javax.swing.JOptionPane
import org.jdesktop.swingx.painter.*
import org.jdesktop.swingx.*
import org.jdesktop.swingx.border.DropShadowBorder
import org.jdesktop.http.*
import net.miginfocom.swing.MigLayout
import javax.swing.JCheckBox
import javax.swing.WindowConstants
import javax.swing.UnsupportedLookAndFeelException
import javax.swing.UIManager
import groovy.swingx.demo.Todo

public class ThirtyBoxesFrame {
    def swing,frame, menu, service
    def todoPane, eventPane, settingsFrame

    
    def addTodo = {def text = JOptionPane.showInputDialog(null, "Enter todo:", "Add a Todo", 
	  JOptionPane.QUESTION_MESSAGE)
        service.addTodo(text)
        todoPane.expanded = false
        todoPane.removeAll()
        createTodos()
        todoPane.expanded = true
    }

    def addEvent = {def text = JOptionPane.showInputDialog(null, "Enter event:", "Add an Event", 
            JOptionPane.QUESTION_MESSAGE)
         service.addByOneBox(text)
    }

    public static void main(args) {
        def window = new ThirtyBoxesFrame()
        println window.frame.visible
    }

    public ThirtyBoxesFrame() {
        try {
            UIManager.setLookAndFeel("org.jdesktop.swingx.plaf.nimbus.NimbusLookAndFeel")
        } catch (UnsupportedLookAndFeelException e) { }
        swing = new SwingXBuilder()
        initComponents()
        service = new _30BoxesService()
        createTodos()
        createEvents()
    }
    def todoClosure = { evt ->
        def selected = (JCheckBox)evt.source
        def todo = service.lookupTodo(selected.toolTipText)
        todo = service.updateTodo(todo)
        selected.text = todo.summary
        if (todo.done.value == true) {
            selected.text += " - Done" 
            swing.('btn'+selected.toolTipText).visible = true
        }
        else swing.('btn'+selected.toolTipText).visible = false
    }
    def deleteTodo = { evt ->
        def id = ((JXButton)evt.source).name
        service.deleteTodo(id)
        def comps = evt.source.parent.components
        def checkbox = comps.find{ it.toolTipText == id }
        todoPane.remove(checkbox)
        todoPane.remove((JXButton)evt.source)
    }

    def deleteEvent = { evt ->
        def id = ((JXButton)evt.source).name
        service.deleteEvent(id)
        def comps = evt.source.parent.components
        def label = comps.find{ it.toolTipText == id }
        eventPane.remove(checkbox)
        eventPane.remove((JXButton)evt.source)
    }

    private makeTitlePainter() {
        def comp = swing.compoundPainter() {
            mattePainter(fillPaint:Color.BLACK)
            glossPainter(paint:new Color(1.0f,1.0f,1.0f,0.2f), position:GlossPainter.GlossPosition.TOP)
        }
        return comp
    }
    private createTodos() {
        service.getTodos()
        def todos = service.todosList
        todoPane.add(swing.button(text:"Add Todo",actionPerformed:addTodo), "wrap")
        for(Todo t in todos) {
            def a = swing.checkBox(text:t.summary, selected:t.done.value, toolTipText:t.id, 
                actionPerformed:todoClosure, constraints:"newline" )
            if (t.done.value == true) {
                a.text += " - Done" 
            }
            def b = swing.button(id:'btn'+t.id, name:t.id, text:'Delete',visible:false, actionPerformed:deleteTodo, constraints:"wrap")
            if (t.done.value)
                b.visible = true
            todoPane.add(a)
            todoPane.add(b,"wrap")
        }
        todoPane.expanded = true
    }

    private createEvents(){
        service.getEvents()
        def events = service.eventsList
        eventPane.add(swing.button(text:"Add Event",actionPerformed:addEvent), "wrap")
        for(e in events) {
            def a = swing.label(text:e.toString(), toolTipText:e.id)
            eventPane.add(a,"wrap")
        }
        eventPane.expanded = true
    }

    private initComponents() {
        frame = swing.frame(size:[300,300],defaultCloseOperation:WindowConstants.EXIT_ON_CLOSE) {
            titledPanel(title:"30Boxes Viewer", titlePainter:makeTitlePainter(), 
                border:new DropShadowBorder(Color.BLACK,15), size:[300,400],mousePressed:{menu.setVisible(true)}) {
                    scrollPane() {
                        taskPaneContainer() {
                            todoPane = taskPane(title:"To Do:",layout:new MigLayout()) 
                            eventPane = taskPane(title:"Events:", layout:new MigLayout())
                        }
                    }
            }
        }
        todoPane.expanded = false
        eventPane.expanded = false
        frame.show()
    }
}