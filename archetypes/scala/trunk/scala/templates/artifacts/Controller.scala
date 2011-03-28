@artifact.package@import org.codehaus.griffon.runtime.core.AbstractGriffonController

class @artifact.name@ extends AbstractGriffonController {
    var model:@artifact.name.plain@Model = _

    def setModel(m:@artifact.name.plain@Model) = model = m

    def copy():Unit = model.setOutput(model.input)
}
