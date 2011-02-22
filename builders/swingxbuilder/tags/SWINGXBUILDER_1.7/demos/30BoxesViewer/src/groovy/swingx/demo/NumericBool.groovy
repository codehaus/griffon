// Groovy source file
public class NumericBool {
    boolean value
    public NumericBool() {
    }
    public NumericBool(val) {
        if (val == "1")
            value = true
         else value = false
    }
    public void reverse() {
        value = !value
    }
    public String toString() {
        if (value == true)
            return "1"
         else return "0"
    }
}