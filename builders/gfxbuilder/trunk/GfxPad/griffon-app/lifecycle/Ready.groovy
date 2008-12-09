def m = app.models.root
def v = app.builders.root
def c = app.controllers.root

v.editor.textEditor.requestFocus()
v.doLater {
   def script = Thread.currentThread().contextClassLoader.
                   getResourceAsStream("sample-script.txt").text
   v.editor.textEditor.text = script
   c.runScript()
}