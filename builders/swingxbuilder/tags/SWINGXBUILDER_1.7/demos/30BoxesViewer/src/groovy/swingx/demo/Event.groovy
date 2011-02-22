// Groovy source file
package groovy.swingx.demo
public class Event {
    def id
    def summary
    def notes
    def start
    def end
    def lastUpdate
    def tags
    boolean allDay

    public String toString() {
        return "<html><h3>${summary}</h3>Starts: ${start} <br>Ends:&nbsp;  ${end}</html>" 
    }
}