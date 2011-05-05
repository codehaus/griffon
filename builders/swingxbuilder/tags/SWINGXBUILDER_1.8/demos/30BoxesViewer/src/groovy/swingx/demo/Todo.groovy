// Groovy source file
package groovy.swingx.demo
public class Todo {
    def id
    NumericBool done
    def summary
    def tags
    public String toString() {
        return "${id} - ${summary} - ${tags} - ${done}"
    }
}