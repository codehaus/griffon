// Groovy source file
// Wrapper API for 30Boxes functions
package groovy.swingx.demo
import org.jdesktop.http.*
import groovy.util.*
import java.text.SimpleDateFormat

public class _30BoxesService {
  //  def context = Main.context
    def eventsList, todosList
    def dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    def session = new Session()
    def apiKey, authToken 
    public _30BoxesService() {
        if (new File("config.xml").exists()) {
            def xml = new XmlParser().parseText(new File("config.xml").getText())
            apiKey = xml.apiKey.value[0]
            authToken = xml.authToken.value[0]
            if (checkConnection() == false)
                println "Can not connect to Internet."
            else println "Connected."
        }
        else {
            println "Cannot find required application keys."
            System.exit(0)
        }
    }
    private boolean checkConnection() {
        session = new Session();
        def response = session.get("http://30boxes.com/api/api.php?method=test.Ping&apiKey="+apiKey)
        if (response.getBody().size() == 0) 
            return false
        else return true
    }

    public userAuthorize() {
        def url = "http://30boxes.com/api/api.php?method=user.Authorize"+
            "&apiKey="+apiKey+
            "&applicationName=SwingX+30Boxes+Application"+
            "&applicationLogoUrl=http%3A%2F%2Fjameswilliams.be"
         // show dialog and set key
    }

    public lookupTodo(value) {
        return todosList.find { it.id == value }
    }

    public getEvents() {
        def today = new Date()
        def endOfMonth = today.plus(31)

        def response = session.get("http://30boxes.com/api/api.php?method=events.Get"+
            "&apiKey="+apiKey+
            "&authorizedUserToken="+authToken+
            "&start="+dateFormat.format(today)+"&end="+dateFormat.format(endOfMonth))
        //println response.getBody()
        def events = new XmlParser().parseText(response.getBody())
        eventsList = []
        events.eventList.event.each {
            def node = it
            def e = new Event()
            e.id = node.id.value[0]
            e.summary = (String)node.summary.value[0]
            e.notes = node.notes.value
            e.start = (String)node.start.value[0]
            e.end = (String)node.end.value[0]
            e.lastUpdate = node.end.value
            e.tags = node.tags.value
            e.allDay = node.allDay.value
            eventsList.add(e)
        }
        println eventsList
    }

    public getTodos() {
        def response = session.get("http://30boxes.com/api/api.php?method=todos.Get"+
            "&apiKey="+apiKey+
            "&authorizedUserToken="+authToken)
        def todos = new XmlParser().parseText(response.getBody())
        todosList = []
        todos.todoList.todo.each{
            def node = it
            def t = parseTodoNode(node)
            todosList.add(t)
        }
        //println response.getBody()
    }

    public Todo parseTodoNode(Node node) {
        def todo = new Todo()
        todo.id = node.id.value[0]
        todo.done = new NumericBool(node.done.value[0])
        todo.summary = node.summary.value[0]
        todo.tags = node.tags.value
        return todo
    } 

    public Todo updateTodo(todo) {
        todo.done.reverse()
        def response = session.get("http://30boxes.com/api/api.php?method=todos.Update" +
            "&apiKey="+apiKey+"&authorizedUserToken="+authToken+
            "&todoId="+todo.id+"&done="+todo.done.toString())
        def xml = new XmlParser().parseText(response.getBody())
        def updatedTodo = parseTodoNode(xml.todoList.todo)
        return updatedTodo
    }

    private String parseResponse(text) {
        def xml = new XmlParser().parseText(text)
        return xml.attributes.stat
    }
    public void deleteTodo(todo) {
        def response = session.get("http://30boxes.com/api/api.php?method=todos.Delete" +
            "&apiKey="+apiKey+"&authorizedUserToken="+authToken+
            "&todoId="+todo)
        def status = parseResponse(response.getBody())
        if (status == "fail")
            // Make this an option pane later
            println "There was a problem deleting the todo."
    }

    public void deleteEvent(event) {
        def response = session.get("http://30boxes.com/api/api.php?method=events.Delete" +
            "&apiKey="+apiKey+"&authorizedUserToken="+authToken+
            "&eventId="+event)
            println response.getBody()
        //def status = parseResponse(response.getBody())
      //  if (status == "fail")
            // Make this an option pane later
        //    println "There was a problem deleting the todo."
    }
    
    public Event addByOneBox(text) {
        if (text != null) {
            def eventText = text.replace(" ","+")
            def response = session.get("http://30boxes.com/api/api.php?method=events.AddByOneBox"+
                "&apiKey="+apiKey+"&authorizedUserToken="+authToken+
                "&event="+eventText)
         }
    }

    public addTodo(text) {
        if (text != null) {
            def todoText = text.replace(" ","+")
            def response = session.get("http://30boxes.com/api/api.php?method=todos.Add"+
                "&apiKey="+apiKey+"&authorizedUserToken="+authToken+
                "&text="+todoText)
            println response.getBody()
        }
    }
}