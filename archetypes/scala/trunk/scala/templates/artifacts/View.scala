@artifact.package@import java.util.{Map => JMap}
import scala.swing.{Frame, Button, TextField, GridPanel, Reactor}
import scala.swing.event.ButtonClicked

import griffon.core.GriffonApplication
import griffon.swing.BindUtils
import griffon.swing.SwingGriffonApplication
import griffon.swing.WindowManager
import org.codehaus.griffon.runtime.core.AbstractGriffonView

class @artifact.name@ extends AbstractGriffonView {
    var controller:@artifact.name.plain@Controller = _
    var model:@artifact.name.plain@Model = _

    def setController(c:@artifact.name.plain@Controller) = controller = c
    def setModel(m:@artifact.name.plain@Model) = model = m

    implicit def funToRunnable(fun: () => Unit) = new Runnable() { def run() = fun() }

    override def mvcGroupInit(args:JMap[String,Object]) = {
        execSync(() => {
            val frame = new MyFrame()
            frame.pack()
            val app = getApp().asInstanceOf[SwingGriffonApplication]
            app.getWindowManager().attach(frame.peer)
        })
    }

    class MyFrame extends Frame with Reactor {
        val button = new Button { text = "Copy" }

        title = "@griffon.project.name@"
        contents = new GridPanel(3, 1) {
            val input = new TextField{ columns = 20 }
            val output = new TextField{ columns = 20 }

            contents += input
            contents += button
            contents += output

            BindUtils.binding()
                      .withSource(input.peer)
                      .withSourceProperty("text")
                      .withTarget(model)
                      .withTargetProperty("input")
                      .make(getBuilder())
            BindUtils.binding()
                      .withSource(model)
                      .withSourceProperty("output")
                      .withTarget(output.peer)
                      .withTargetProperty("text")
                      .make(getBuilder())
        }

        listenTo(button)
        reactions += {
            case ButtonClicked(button) => controller.copy
        }
    }
}
